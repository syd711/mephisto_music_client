package de.mephisto.player;

import de.mephisto.model.Playlist;
import de.mephisto.model.Song;
import de.mephisto.model.Stream;
import de.mephisto.service.IMusicProvider;
import de.mephisto.service.MusicProviderFactory;
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
      client = new TelnetClient();
      String host = config.getString(PROPERTY_HOST);
      int port = config.getInt(PROPERTY_PORT, 6600);
      client.connect(host, port);
      LOG.info("Initialized " + this);
    } catch (Exception e) {
      LOG.error("Failed to connect to " + this + ": " + e.getMessage());
    }
  }

  @Override
  public void playSongCollection(Playlist songs) {
    LOG.info("Playback of: " + songs);
    try {
      Song song = songs.getSongs().iterator().next();
      IMusicProvider provider = MusicProviderFactory.getProvider(song.getProviderId());
      String url = provider.getUrl(song);
      String add = "mpc add " + url;
      String play = "mpc play";
      client.getOutputStream().write(add.getBytes());
      client.getOutputStream().write(play.getBytes());
    } catch (Exception e) {
      LOG.error("Failed to playback '" + songs + "': " + e.getMessage(), e);
    }
  }

  @Override
  public void playStream(Stream stream) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String toString() {
    return "MPD Music Player";
  }
}
