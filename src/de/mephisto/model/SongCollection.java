package de.mephisto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A collection of songs, identified by a name.
 */
public class SongCollection extends MModel {
  private String name;
  private List<Song> songs = new ArrayList<Song>();

  public SongCollection(String name) {
    this.name = name;
  }

  public void addSong(Song song) {
    songs.add(song);
  }

  public String getName() {
    return name;
  }

  public List<Song> getSongs() {
    Collections.sort(songs, new Comparator<Song>() {
      @Override
      public int compare(Song o1, Song o2) {
        return o1.getTrack() - o2.getTrack();
      }
    });
    return songs;
  }
}
