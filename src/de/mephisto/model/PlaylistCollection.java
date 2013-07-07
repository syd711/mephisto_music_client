package de.mephisto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for a list of several albums or collections
 */
public class PlaylistCollection {
  private List<Playlist> collections = new ArrayList<Playlist>();

  public List<Playlist> getItems() {
    return collections;
  }

  public void setItems(List<Playlist> collections) {
    this.collections = collections;
  }

  public int getSize() {
    return collections.size();
  }
}
