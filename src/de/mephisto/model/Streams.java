package de.mephisto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for wrapping several streams.
 */
public class Streams {
  private List<Stream> streams = new ArrayList<Stream>();

  public List<Stream> getStreams() {
    return streams;
  }

  public void setStreams(List<Stream> streams) {
    this.streams = streams;
  }
}
