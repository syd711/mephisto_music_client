package de.mephisto.model;

/**
 * The model that represents an album.
 */
public class Album extends Playlist {
  private String artist;

  public Album(String artist, String name) {
    super(name);
    this.artist = artist;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  @Override
  public String toString() {
    return "Album '" + getName() + "' by '" + artist + "', tracks: " + getSize();
  }
}
