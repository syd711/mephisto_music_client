package de.mephisto.player;

import de.mephisto.Mephisto;
import de.mephisto.model.Song;
import de.mephisto.model.Stream;
import de.mephisto.service.IMusicProvider;
import de.mephisto.service.ProviderManager;
import de.mephisto.util.Config;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * MPD implementation for the playback of mp3, etc.
 */
public class MpdPlayer extends AbstractMusicPlayer {
  private final static Logger LOG = LoggerFactory.getLogger(MpdPlayer.class);

  private final static String CONFIG_NAME = "mpd.properties";
  private final static int RETRY_COUNT = 1;

  private final static String PROPERTY_HOST = "mpd.host";
  private final static String PROPERTY_PORT = "mpd.port";

  private Configuration config;
  private TelnetClient client;
  private InputStream in = null;
  private byte[] buff = new byte[1024];
  private int ret_read = 0;
  private Song current;

  public MpdPlayer() {
    this.config = Config.getConfiguration(CONFIG_NAME);
    this.connect();
  }

  private void connect() {
    try {
      if(in != null) {
        in.close();
      }
      if(client != null) {
        client.disconnect();
      }
      client = new TelnetClient();
      String host = config.getString(PROPERTY_HOST);
      int port = config.getInt(PROPERTY_PORT, 6600);
      client.connect(host, port);
      in = client.getInputStream();
      LOG.info("Initialized " + this);
    } catch (Exception e) {
      LOG.error("Failed to connect to " + this + ": " + e.getMessage());
    }
  }

  @Override
  public boolean pause() {
    try {
      executeCommand("pause");
    } catch (IOException e) {
      LOG.error("Failed to execute pause command: " + e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public boolean playStream(Stream stream) {
    playUrl(stream.getUrl(), RETRY_COUNT);
    return true;
  }

  @Override
  public String toString() {
    return "MPD Music Player";
  }

  @Override
  public Song play(Song song) {
    if(song != null) {
      LOG.info("Playback of: " + song);
      try {
        //continue paused song
        if(song == current) {
          LOG.info("MPD Player continues playback of " + song);
          executeCommand("play");
          return song;
        }

        current = song;
        IMusicProvider provider = Mephisto.getInstance().getProviderManager().getProvider(song.getProviderId());
        String url = provider.getUrl(song);
        playUrl(url, RETRY_COUNT);
      } catch (Exception e) {
        LOG.error("MPD player failed to playback " + song + ": " + e.getMessage() + ", trying to establish connection again.");
        connect();
      }
    }
    else {
      LOG.info("Reached end of " + getActivePlaylist());
    }
    return song;
  }

  /**
   * Public for testing.
   * @param url
   * @throws IOException
   */
  public void playUrl(String url, int retryCount) {
    LOG.info("Playback of URL: " + url);
    try {
      executeCommand("stop");
      executeCommand("clear");
      executeCommand("add " + url);
      executeCommand("play");
    }
    catch (Exception e) {
      LOG.error("Failed to playback " + url + ": " + e.getMessage());
      if(retryCount >= 0){
        connect();
        retryCount--;
        playUrl(url, retryCount);
      }
      else {
        LOG.error("Failed to reconnect to mpd");
      }
    }
  }

  private void executeCommand(String cmd) throws IOException {
//    LOG.info("Executing telnet command '" + cmd + "'");
//    cmd+="\n";
//    client.getOutputStream().write(cmd.getBytes());
//    client.getOutputStream().flush();
//
//    awaitOk();
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  private boolean awaitOk() {
    try {
      do {
        ret_read = in.read(buff);
        if (ret_read > 0) {
          String msg = new String(buff, 0, ret_read).trim();
          LOG.info("MPD: " + msg);
          if (msg.contains("OK") ) {
            return true;
          }
        }
      }
      while (ret_read >= 0 );
      LOG.info("Terminating Mpd Response Reader thread.");
    } catch (IOException e) {
      LOG.error("Exception while reading from MPD:" + e.getMessage());
    }
    return false;
  }


  @Override
  public boolean setVolume(int volume) {
    return true;
  }

  @Override
  public int getVolume() {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean stop() {
    try {
      executeCommand("stop");
    } catch (IOException e) {
      LOG.error("Failed to execute stop command: " + e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public boolean isVolumeControlEnabled() {
    return true;
  }
}
