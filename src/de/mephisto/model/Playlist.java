package de.mephisto.model;

import de.mephisto.rest.JSONViews;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
}
