package de.mephisto.model;

/**
 * The model that represents an album.
 */
public class Album extends SongCollection {
  private String artist;

  public Album(String artist, String name) {
    super(name);
    this.artist = artist;
  }
}
