package de.mephisto.util;

import de.mephisto.model.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Utility to read data from a stream
 */
public class StreamHelper {
  private final static Logger LOG = LoggerFactory.getLogger(StreamHelper.class);

  public static void loadInfo(Stream stream) {
    URL url = null;
    try {
      url = new URL(stream.getUrl());
      StreamInfo info = new StreamInfo(url);
      stream.setTitle(info.getTitle() + " - " + info.getArtist());
    } catch (IOException e) {
      LOG.warn("Failed to retrieve meta information for Stream '" + stream.getUrl() + "': " + e.getMessage(), e);
    }
  }
}
