package de.mephisto.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Model for wrapping several streams.
 */
public class Streams {
  private Collection<Stream> streams = new ArrayList<Stream>();

  public Collection<Stream> getStreams() {
    return streams;
  }

  public void setStreams(Collection<Stream> streams) {
    this.streams = streams;
  }
}
