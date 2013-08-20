package de.mephisto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the settings
 */
public class Settings {
  private int songs;
  private int albums;
  private int streams;
  private List<ProviderInfo> providers = new ArrayList<ProviderInfo>();

  public int getSongs() {
    return songs;
  }

  public void setSongs(int songs) {
    this.songs = songs;
  }

  public int getAlbums() {
    return albums;
  }

  public void setAlbums(int albums) {
    this.albums = albums;
  }

  public List<ProviderInfo> getProviders() {
    return providers;
  }

  public void setProviders(List<ProviderInfo> providers) {
    this.providers = providers;
  }

  public int getStreams() {
    return streams;
  }

  public void setStreams(int streams) {
    this.streams = streams;
  }
}
