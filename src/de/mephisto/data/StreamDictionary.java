package de.mephisto.data;

import de.mephisto.model.Stream;
import de.mephisto.player.PlayerFactory;
import de.mephisto.util.Config;
import de.mephisto.util.StreamHelper;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Persistency handler for streams
 */
public class StreamDictionary {
  private final static Logger LOG = LoggerFactory.getLogger(StreamDictionary.class);
  private final static String CONFIG_FILE = Config.getConfiguration(PlayerFactory.PLAYER_CONFIG).getString("streams.properties");

  private static StreamDictionary instance = new StreamDictionary();

  private PropertiesConfiguration streamConfig;
  private Map<Integer, Stream> streams = new LinkedHashMap<Integer, Stream>();
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
      streamConfig = new PropertiesConfiguration(CONFIG_FILE);

      Iterator<String> keys = streamConfig.getKeys();
      while (keys.hasNext()) {
        String key = keys.next();
        if(key.contains(".url")) {
          String url = streamConfig.getString(key);

          Stream stream = new Stream(midCounter);
          stream.setName(url);
          stream.setUrl(url);

          streams.put(midCounter, stream);
          midCounter++;
        }
      }
      refreshId3();
      LOG.info("Created stream dictionary with " + streams.size() + " stations.");
    } catch (ConfigurationException e) {
      LOG.error("Failed to load stream configuration: " + e.getMessage(), e);
    }
  }

  /**
   * Reloads the id3 tag information of all streams.
   */
  private void refreshId3() {
    Iterator<Stream> iterator = streams.values().iterator();
    while (iterator.hasNext()) {
      Stream stream = iterator.next();
      StreamHelper.loadInfo(stream);
    }
  }

  /**
   * Returns the representation of all streams.
   *
   * @return
   */
  public Collection<Stream> getStreams() {
    refreshId3();
    return streams.values();
  }

  /**
   * Returns the Stream with the given mid.
   *
   * @param mid
   * @return
   */
  public Stream getStream(int mid, boolean refresh) {
    Stream stream = streams.get(mid);
    if (refresh) {
      StreamHelper.loadInfo(stream);
    }
    return stream;
  }

  /**
   * Deletes the Stream with the given id from the list of streams.
   *
   * @param mid
   * @return
   */
  public boolean deleteStream(int mid) {
    streams.remove(mid);
    doSave();
    return true;
  }

  /**
   * Creates and store a new Stream instance
   *
   * @param url
   * @return
   */
  public Stream addStream(String url) {
    Stream stream = new Stream(midCounter);
    stream.setName(url);
    stream.setUrl(url);
    streams.put(stream.getMID(), stream);

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
    while (iterator.hasNext()) {
      Stream stream = iterator.next();
      streamConfig.addProperty(stream.getMID() + ".url", stream.getUrl());
      streamConfig.addProperty(stream.getMID() + ".name", stream.getName());
    }
    try {
      streamConfig.save(new File(CONFIG_FILE));
    } catch (ConfigurationException e) {
      LOG.error("Failed to store streams: " + e.getMessage());
    }
  }

  /**
   * Saves the streams by the csv order value.
   *
   * @param order
   * @return
   */
  public boolean saveOrder(String order) {
    Map<Integer, Stream> sorted = new LinkedHashMap<Integer, Stream>();
    String[] ids = order.split(",");
    for (String id : ids) {
      if (id.length() > 0) {
        int mid = Integer.parseInt(id);
        Stream s = streams.get(mid);
        sorted.put(mid, s);
      }
    }
    this.streams = sorted;
    doSave();
    return true;
  }
}
