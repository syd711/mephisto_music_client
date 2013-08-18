package de.mephisto.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility to read data from a stream
 */
public class StreamDataUtil {

  public static void main(String[] args) throws IOException {
    URL url = new URL("http://mp3channels.webradio.rockantenne.de/alternative");
    StreamInfo info = new StreamInfo(url);
    System.out.println(info.getTitle());
  }
}
