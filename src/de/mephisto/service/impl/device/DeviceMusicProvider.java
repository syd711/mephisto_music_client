package de.mephisto.service.impl.device;

import de.mephisto.Mephisto;
import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Song;
import de.mephisto.service.AbstractMusicProvider;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Google Music provider.
 */
public class DeviceMusicProvider extends AbstractMusicProvider {
  private final static Logger LOG = LoggerFactory.getLogger(DeviceMusicProvider.class);

  private File musicDirectory;

  @Override
  public boolean connect() {
    Configuration config = getConfiguration();
    musicDirectory = new File(config.getString("device.folder"));
    return musicDirectory.exists();
  }

  @Override
  public void loadMusic() {
    LOG.info("Loading all songs for " + this);

    Configuration config = getConfiguration();
    boolean recursive = config.getBoolean("device.recursive", false);

    readDirectory(musicDirectory, recursive);
  }

  /**
   * Recursive read of the given directory.
   *
   * @param dir
   * @param recursive
   */
  private void readDirectory(File dir, boolean recursive) {
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isDirectory() && recursive) {
        readDirectory(file, recursive);
      }
      if (file.isFile() && Mephisto.getInstance().getPlayer().isPlayable(file)) {
        Song song = songFor(file);
        if (song != null) {
          Dictionary.getInstance().addSong(song);
          resolveArtwork(file, song); //done afterwards since we need the generated song id here
          //TODO re-add to album, since the cover is only resolved then for the album only
          Dictionary.getInstance().addToAlbum(song);
        }
      }
    }
  }

  /**
   * Applies a REST url as artwork url
   *
   * @param f
   * @param song
   */
  private void resolveArtwork(File f, Song song) {
    try {
      AudioFile audioFile = AudioFileIO.read(f);
      Tag tag = audioFile.getTag();
      if (!tag.getArtworkList().isEmpty()) {
        Artwork artwork = tag.getFirstArtwork();
        byte[] data = artwork.getBinaryData();
        song.setArtwork(data);
        song.setAlbumArtUrl("/rest/artwork/cover/" + song.getMID());
      }
    } catch (Exception e) {
      LOG.error("Error resolving artwork for " + f.getAbsolutePath() + ": " + e.getMessage());
    }
  }

  @Override
  public String getUrl(Song song) {
    File f = (File) song.getOriginalModel();
    try {
      return f.toURI().toURL().toExternalForm();
    } catch (Exception e) {
      LOG.error("Failed to resolve URL of " + song + ": " + e.getMessage(), e);
      return null;
    }
  }

  /**
   * Puts all music data from the file the local song model.
   *
   * @param f The song to convert.
   * @return The converted song.
   */
  private Song songFor(File f) {
    Song song = new Song(getInternalId());
    song.setOriginalModel(f);

    try {
      AudioFile audioFile = AudioFileIO.read(f);
      Tag tag = audioFile.getTag();
      MP3AudioHeader header = (MP3AudioHeader) audioFile.getAudioHeader();

      String duration = header.getTrackLengthAsString();
      if (!StringUtils.isEmpty(duration)) {
        //TODO grrrr, seems I am too stupid here: date has format mm:ss
        int minutes = Integer.parseInt(duration.substring(0,duration.indexOf(":")));
        int seconds = Integer.parseInt(duration.substring(duration.indexOf(":")+1, duration.length()));
        song.setDurationMillis(seconds*1000 + minutes*60*1000);
      }

      song.setAlbum(tag.getFirst(FieldKey.ALBUM));
      song.setArtist(tag.getFirst(FieldKey.ALBUM_ARTIST));
      if (StringUtils.isEmpty(song.getArtist())) {
        song.setArtist(tag.getFirst(FieldKey.ARTIST));
      }
      if (StringUtils.isEmpty(song.getArtist())) {
        song.setArtist(tag.getFirst(FieldKey.COMPOSER));
      }
      song.setName(tag.getFirst(FieldKey.TITLE));
      song.setGenre(tag.getFirst(FieldKey.GENRE));
      if (!StringUtils.isEmpty(tag.getFirst(FieldKey.YEAR))) {
        song.setYear(Integer.parseInt(tag.getFirst(FieldKey.YEAR)));
      }
      song.setComposer(tag.getFirst(FieldKey.COMPOSER));

    } catch (Exception e) {
      LOG.error("Error reading mp3 tag for " + f.getAbsolutePath() + ": " + e.getMessage());
      return null;
    }

    return song;
  }
}
