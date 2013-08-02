package de.mephisto.dictionary;

import de.mephisto.model.Playlist;

import java.util.Comparator;

/**
 * Supports different sort directions for Albums.
 */
public class SongCollectionComparator implements Comparator<Playlist> {

  public SongCollectionComparator() {
  }

  @Override
  public int compare(Playlist o1, Playlist o2) {
    return o1.getName().compareTo(o2.getName());
  }
}
