package de.mephisto.dictionary;

import de.mephisto.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The collected music, gathered by all music providers.
 */
public class Dictionary {
  private final static Logger LOG = LoggerFactory.getLogger(Dictionary.class);
  private static Dictionary instance = new Dictionary();

  private Map<String, Stream> streams = new HashMap<String, Stream>();
  private Map<String, Song> songs = new HashMap<String, Song>();
  private Map<String, Genre> genres = new HashMap<String, Genre>();
  private Map<String, Album> albums = new HashMap<String, Album>();
  private Map<String, Playlist> playlists = new HashMap<String, Playlist>();
  private Map<Integer, MModel> globalDict = new HashMap<Integer, MModel>();

  private int idCounter = 0;

  /**
   * Returns the singleton instance of the dictionary.
   *
   * @return
   */
  public static Dictionary getInstance() {
    return instance;
  }

  public Collection<Song> getSongs() {
    return songs.values();
  }


  /**
   * Returns a list of all albums.
   * @return
   * @param sortType
   */
  public List<Playlist> getAlbumsWithoutSongs(int sortType) {
    List<Playlist> list = new ArrayList(albums.values());
    Collections.sort(list, new SongCollectionComparator(sortType));
    return list;
  }

  /**
   * Adds a stream to the dictionary.
   * @param stream
   */
  public void addStream(Stream stream) {
    streams.put(stream.getUrl(), stream);
  }

  public Album getAlbum(int id) {
    return (Album)globalDict.get(id);
  }

  /**
   * Adds a song to the dictionary, updates all sub-dictionaries afterwards.
   *
   * @param song The song to add.
   */
  public void addSong(Song song) {
    createMID(song);
    songs.put(song.getId(), song);
    addToAlbum(song);
    addToGenre(song);
    LOG.debug("Add song " + song);
  }

  /**
   * Creates a unique id for a new mmodel.
   * @return
   */
  private void createMID(MModel model) {
    this.idCounter++;
    model.setMID(idCounter);
    globalDict.put(idCounter, model);
  }

  /**
   * Adds a playlist to the dictionary.
   * @param p
   */
  public void addPlaylist(Playlist p) {
    playlists.put(p.getName(), p);
  }

  /**
   * Adds the song to its genre or creates a new one.
   *
   * @param song
   */
  private void addToGenre(Song song) {
    if (!StringUtils.isEmpty(song.getGenre())) {
      Genre genre = null;
      if (!genres.containsKey(song.getGenre())) {
        genre = new Genre(song.getGenre());
        createMID(genre);
        genres.put(song.getAlbum(), genre);
      }
      else {
        genre = genres.get(song.getAlbum());
      }
      genre.getSongs().add(song);
    }
  }

  /**
   * Creates an album for the song if it does not exist yet.
   *
   * @param song The song to add to the album.
   */
  private void addToAlbum(Song song) {
    if (!StringUtils.isEmpty(song.getAlbum())) {
      //create the regular dict entry and add song to album
      Album album = getAlbum(song);
      if(!album.containsSong(song)) {
        album.getSongs().add(song);
        if(!StringUtils.isEmpty(song.getAlbumArtUrl()) && StringUtils.isEmpty(album.getArtUrl())) {
          album.setArtUrl(song.getAlbumArtUrl());
        }
      }
    }
  }

  /**
   * Returns the album for given dict, creates a new one if it
   * does not exist yet.
   * @param song  The song to find the album for.
   * @return
   */
  private Album getAlbum(Song song) {
    Album album = null;
    if (!albums.containsKey(song.getAlbum())) {
      album = new Album(song.getArtist(), song.getAlbum());
      createMID(album);
      albums.put(song.getAlbum(), album);
    }
    else {
      album = albums.get(song.getAlbum());
    }

    if(StringUtils.isEmpty(album.getArtist()) && !StringUtils.isEmpty(song.getArtist())) {
      album.setArtist(song.getArtist());
    }
    return album;
  }
}
