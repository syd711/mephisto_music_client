package de.mephisto.service.impl.google;

import de.mephisto.dictionary.Dictionary;
import de.mephisto.service.AbstractMusicProvider;
import gmusic.api.impl.GoogleMusicAPI;
import gmusic.api.model.Playlist;
import gmusic.api.model.Playlists;
import gmusic.api.model.Song;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Google Music provider.
 */
public class GoogleMusicProvider extends AbstractMusicProvider {
  private final static Logger LOG = LoggerFactory.getLogger(GoogleMusicProvider.class);

  private GoogleMusicAPI api;

  @Override
  public boolean connect() {
    Configuration config = getConfiguration();
    api = new GoogleMusicAPI();
    try {
      api.login(config.getString("google.login"), config.getString("google.password"));
      return true;
    } catch (Exception e) {
      LOG.error("Error connecting to Google:" + e.getMessage());
      return false;
    }
  }

  @Override
  public void loadMusic() {
    LOG.info("Loading all songs for " + this);

    try {
      Playlists lists = api.getAllPlaylists();
      for(Playlist list : lists.getPlaylists()) {
        de.mephisto.model.Playlist p = playlistFor(list);
        Dictionary.getInstance().addPlaylist(p);
      }

      Collection<Song> songs = api.getAllSongs();
      for (Song song : songs) {
        de.mephisto.model.Song mSong = songFor(song);
        Dictionary.getInstance().addSong(mSong);
      }
      LOG.info(this + " finished loading songs: " + songs.size() + " total");
    } catch (Exception e) {
      LOG.error("Failed to load Google songs: " + e.getMessage(), e);
    }
  }

  @Override
  public String getUrl(de.mephisto.model.Song song) {
    Song gSong = (Song) song.getOriginalModel();
    try {
      return api.getSongURL(gSong).toURL().toString();
    } catch (Exception e) {
      LOG.error("Failed to resolve URL of " + song + ": " + e.getMessage(), e);
      return null;
    }
  }

  /**
   * Converts the google music playlist into the local model.
   * @param list
   * @return
   */
  private de.mephisto.model.Playlist playlistFor(Playlist list) {
    de.mephisto.model.Playlist p = new de.mephisto.model.Playlist(list.getTitle());
    for(Song song : list.getPlaylist()) {
      p.getSongs().add(songFor(song));
    }
    return p;
  }

  /**
   * Puts all music data from the google song api into the
   * local song model.
   *
   * @param song The song to convert.
   * @return The converted song.
   */
  private de.mephisto.model.Song songFor(Song song) {
    de.mephisto.model.Song mSong = new de.mephisto.model.Song(getProviderId());
    mSong.setOriginalModel(song);

    mSong.setId(song.getId());
    mSong.setName(song.getName());

    if(!StringUtils.isEmpty(song.getAlbumArtUrl())) {
      mSong.setAlbumArtUrl("http:" + song.getAlbumArtUrl());
    }

    mSong.setAlbum(song.getAlbum());
    mSong.setArtist(song.getAlbumArtist());
    mSong.setComment(song.getComment());
    mSong.setComposer(song.getComposer());
    mSong.setTrack(song.getTrack());
    mSong.setDurationMillis(song.getDurationMillis());
    mSong.setYear(song.getYear());
    mSong.setGenre(song.getGenre());
    return mSong;
  }
}
