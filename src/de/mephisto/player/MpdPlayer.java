package de.mephisto.player;

import de.mephisto.util.Config;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MPD implementation for the playback of mp3, etc.
 */
public class MpdPlayer implements IMusicPlayer {
  private final static Logger LOG = LoggerFactory.getLogger(MpdPlayer.class);

  private final static String CONFIG_NAME = "mpd.properties";

  private final static String PROPERTY_HOST = "mpd.host";
  private final static String PROPERTY_PORT = "mpd.port";

  private Configuration config;
  private TelnetClient client;

  public MpdPlayer() {
    this.config = Config.getConfiguration(CONFIG_NAME);
    this.connect();
  }

  private void connect() {
    try {
      TelnetClient client = new TelnetClient();
      String host = config.getString(PROPERTY_HOST);
      int port = config.getInt(PROPERTY_PORT, 6600);
      client.connect(host, port);
      LOG.info("Initialized " + this);
    } catch (Exception e) {
      LOG.error("Failed to connect to " + this + ": " + e.getMessage());
    }
  }

  @Override
  public void playUrl(String url) {
    LOG.info("Playback of URL: " + url);
    try {
      String add = "mpc add " + url;
      String play = "mpc play";
      client.getOutputStream().write(add.getBytes());
      client.getOutputStream().write(play.getBytes());
    } catch (Exception e) {
      LOG.error("Failed to playback '" + url + "': " + e.getMessage(), e);
    }
  }

  @Override
  public String toString() {
    return "MPD Music Player";
  }
}
