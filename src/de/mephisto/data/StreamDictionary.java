package de.mephisto.data;

import de.mephisto.model.Stream;
import de.mephisto.model.Streams;
import de.mephisto.util.StreamHelper;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Persistency handler for streams
 */
public class StreamDictionary {
  private final static Logger LOG = LoggerFactory.getLogger(StreamDictionary.class);

  private static StreamDictionary instance = new StreamDictionary();

  private Configuration streamConfig;
  private Map<Integer, Stream> streams = new HashMap<Integer,Stream>();
  private int midCounter = 0;

  private StreamDictionary() {
    //for singleton
  }

  public static StreamDictionary getInstance() {
    return instance;
  }

  /**
   * Load stored streams
   */
  public void init() {
    try {
      streamConfig = new PropertiesConfiguration("conf/streams.properties");

      Iterator<String> keys = streamConfig.getKeys();
      while(keys.hasNext()) {
        String key = keys.next();
        String url = streamConfig.getString(key);

        Stream stream = new Stream();
        stream.setName(url);
        stream.setUrl(url);
        stream.setMID(midCounter);

        streams.put(midCounter, stream);
        midCounter++;
      }
    } catch (ConfigurationException e) {
      LOG.error("Failed to load stream configuration: " + e.getMessage(), e);
    }
  }

  /**
   * Reloads the id3 tag information of all streams.
   */
  public void refreshId3() {
    Iterator<Stream> iterator = streams.values().iterator();
    while(iterator.hasNext()) {
      Stream stream = iterator.next();
      StreamHelper.loadInfo(stream);
    }
  }

  /**
   * Returns the representation of all streams.
   * @return
   */
  public Collection<Stream> getStreams() {
    return streams.values();
  }

  /**
   * Returns the Stream with the given mid.
   * @param mid
   * @return
   */
  public Stream getStream(int mid, boolean refresh) {
    Stream stream = streams.get(mid);
    if(refresh) {
      StreamHelper.loadInfo(stream);
    }
    return stream;
  }

  /**
   * Deletes the Stream with the given id from the list of streams.
   * @param mid
   * @return
   */
  public boolean deleteStream(int mid) {
    streams.remove(mid);
    doSave();
    return true;
  }

  /**
   * Updates the given stream with the given URL.
   * @param stream
   * @param url
   * @return
   */
  public Stream updateStream(Stream stream, String url) {
    stream.setUrl(url);
    doSave();
    StreamHelper.loadInfo(stream);
    return stream;
  }

  /**
   * Creates and store a new Stream instance
   * @param url
   * @return
   */
  public Stream storeStream(String url) {
    Stream stream = new Stream();
    stream.setName(url);
    stream.setUrl(url);
    stream.setMID(midCounter);

    midCounter++;
    doSave();
    StreamHelper.loadInfo(stream);
    return stream;
  }

  /**
   * Stores all streams in the given properties file.
   */
  private void doSave() {
    streamConfig.clear();
    Iterator<Stream> iterator = streams.values().iterator();
    while(iterator.hasNext()) {
      Stream stream = iterator.next();
      streamConfig.addProperty(String.valueOf(stream.getMID()), stream.getUrl());
    }
  }
}
