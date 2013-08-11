package de.mephisto.model;

import de.mephisto.rest.JSONViews;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import java.util.*;

/**
 * A collection of songs, identified by a name.
 */
public class Playlist extends MModel {
  @JsonProperty("songs")
  @JsonView(JSONViews.AlbumView.class)
  private List<Song> songs = new ArrayList<Song>();

  private String name;
  private String artUrl;
  private int playlistSize = -1;
  private Song activeSong;

  public Playlist(String name) {
    this.name = name;
  }

  public String getArtUrl() {
    return artUrl;
  }

  public void setSize(int size) {
    playlistSize = size;
  }

  public String getName() {
    return name;
  }

  public List<Song> getSongs() {
    Collections.sort(songs);
    return songs;
  }

  public int getSize() {
    if(!songs.isEmpty())
      return songs.size();
    return playlistSize;
  }

  @Override
  public String toString() {
    return "Playlist '" + getName() + "', tracks: " + getSize();
  }

  public void setArtUrl(String artUrl) {
    this.artUrl = artUrl;
  }

  public String getDuration() {
    long durationMillis = 0;
    for(Song songs : getSongs()) {
      durationMillis+= songs.getDurationMillis();
    }
    if(durationMillis > 0) {
      durationMillis-=3600000;
      return DateFormatUtils.format(durationMillis, "HH:mm:ss");
    }
    return "";
  }

  /**
   * Checks if the given song is already part of the playlist.
   * @param compare
   * @return
   */
  public boolean containsSong(Song compare) {
    for(Song song : songs) {
      if(song.getName().toLowerCase().equalsIgnoreCase(compare.getName().toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the next song or null if no next song is available.
   * @return
   */
  public Song nextSong() {
    Iterator<Song> it = getSongs().iterator();
    if(activeSong == null) {
      activeSong = it.next();
      return activeSong;
    }
    while(it.hasNext()) {
      Song next = it.next();
      if(next.equals(activeSong) && it.hasNext()) {
        activeSong = it.next();
        return activeSong;
      }
    }
    activeSong = null;
    return null;
  }

  /**
   * Returns the previous song or null if there is no previous song.
   * @return
   */
  public Song previousSong() {
    Iterator<Song> it = getSongs().iterator();
    if(activeSong == null) {
      activeSong = it.next();
      return activeSong;
    }
    Song prev = null;
    while(it.hasNext()) {
      Song next = it.next();
      if(next.equals(activeSong)) {
        activeSong = prev;
        return activeSong;
      }
      prev = next;
    }
    activeSong = null;
    return null;
  }

  public void setActiveSong(Song activeSong) {
    this.activeSong = activeSong;
  }

  public Song getActiveSong() {
    return activeSong;
  }

  public Song getSong(int id) {
    for(Song song : songs) {
      if(song.getMID() == id) {
        return song;
      }
    }
    return null;
  }
}
