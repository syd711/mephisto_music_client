package de.mephisto.player;

import de.mephisto.Mephisto;
import de.mephisto.model.Song;
import de.mephisto.model.Stream;
import de.mephisto.service.IMusicProvider;
import de.mephisto.util.Config;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * MPD implementation for the playback of mp3, etc.
 */
public class MpdPlayer extends AbstractMusicPlayer {
  private final static Logger LOG = LoggerFactory.getLogger(MpdPlayer.class);

  private final static String CONFIG_NAME = "mpd.properties";

  private final static String PROPERTY_HOST = "mpd.host";
  private final static String PROPERTY_PORT = "mpd.port";

  private Configuration config;
  private TelnetClient client;
  private InputStream in = null;
  private byte[] buff = new byte[1024];
  private int ret_read = 0;
  private Song current;
  private boolean localModeEnabled;

  public MpdPlayer() {
    this.config = Config.getConfiguration(CONFIG_NAME);
    this.connect();
  }

  private void connect() {
    try {
      if (in != null) {
        in.close();
      }
      if (client != null) {
        client.disconnect();
      }
      client = new TelnetClient();
      String host = config.getString(PROPERTY_HOST);
      localModeEnabled = host.equalsIgnoreCase("localhost");
      int port = config.getInt(PROPERTY_PORT, 6600);
      client.connect(host, port);
      in = client.getInputStream();

      if(localModeEnabled) {
        executeLocalCommand("volume 95");
      }
      LOG.info("Initialized " + this);
    } catch (Exception e) {
      LOG.error("Failed to connect to " + this + ": " + e.getMessage());
    }
  }

  @Override
  public boolean pause() {
    setPaused(true);
    executeTelnetCommand("pause");
    return true;
  }

  @Override
  public boolean paused() {
    return isPaused();
  }

  @Override
  public Stream play(Stream stream) {
    activePlaylist = null;
    activeStream = stream;
    playUrl(stream.getUrl());
    return stream;
  }

  @Override
  public String toString() {
    return "MPD Music Player";
  }

  @Override
  public Song play(Song song) {
    if (song != null) {
      LOG.info("Playback of: " + song);
      //continue paused song
      if (song == current) {
        LOG.info("MPD Player continues playback of " + song);
        executeTelnetCommand("play");
        return song;
      }

      current = song;
      IMusicProvider provider = Mephisto.getInstance().getProviderManager().getProvider(song.getProviderId());
      String url = provider.getUrl(song);
      playUrl(url);
    }
    else {
      LOG.info("Reached end of " + getActivePlaylist());
      //let set the first song as the active one
      getActivePlaylist().nextSong();
    }
    return song;
  }

  /**
   * Public for testing.
   *
   * @param url
   * @throws IOException
   */
  public void playUrl(String url) {
    setPaused(false);
    LOG.info("Playback of URL: " + url);
    try {
      executeTelnetCommand("stop");
      executeTelnetCommand("clear");
      executeTelnetCommand("add " + url);
      executeTelnetCommand("play");
    } catch (PlayerException e) {
      LOG.error("Failed to playback " + url + ": " + e.getMessage());
      connect();
      throw e;
    }
  }

  /**
   * Executes a MPD command via telnet.
   *
   * @param cmd
   */
  private void executeTelnetCommand(String cmd) {
    try {
      LOG.info("Executing telnet command '" + cmd + "'");
      cmd += "\n";
      if(client.getOutputStream() == null) {
        connect();
      }

      if (client.getOutputStream() != null) {
        client.getOutputStream().write(cmd.getBytes());
        client.getOutputStream().flush();
      }
      else {
        throw new PlayerException("Exception executing MPD telnet command: Could not acquire telnet output steam, please check the MPD server connection.");
      }

      awaitOk();
    } catch (IOException e) {
      LOG.error("Exception executing MPD telnet command '" + cmd + "':" + e.getMessage());
      throw new PlayerException("Exception executing MPD telnet command '"+ cmd + "':" + e.getMessage() + ", please check the MPD server connection.");
    }
  }

  /**
   * Invokes a system command for the mpc client.
   *
   * @param cmd
   */
  private String executeLocalCommand(String cmd) {
    try {
      LOG.info("Executing mpc command '" + cmd + "'");
      cmd = "mpc " + cmd + "\n";
      Process p = Runtime.getRuntime().exec("cmd /c " + cmd);
      p.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = reader.readLine();
      StringBuilder builder = new StringBuilder();
      while (line != null) {
        line = reader.readLine();
        builder.append(line);
      }

      return builder.toString();
    } catch (Exception e) {
      LOG.error("Exception executing MPD command:" + e.getMessage());
      throw new PlayerException("Exception executing MPD command:" + e.getMessage());
    }
  }

  /**
   * Waits until an acknowledge flag is logged by the mpc.
   *
   * @return
   */
  private boolean awaitOk() {
    try {
//      do {
//        ret_read = in.read(buff);
//        if (ret_read > 0) {
//          String msg = new String(buff, 0, ret_read).trim();
//          LOG.info("MPD: " + msg);
//          if (msg.contains("OK")) {
//            return true;
//          }
//        }
//      }
//      while (ret_read >= 0);
      Thread.sleep(100);
    } catch (Exception e) {
      LOG.error("Exception while reading from MPD:" + e.getMessage());
      throw new PlayerException("Exception while reading from MPD:" + e.getMessage() + ", check MPD server is running");
    }
    return false;
  }


  @Override
  public boolean setVolume(int volume) {
    if (localModeEnabled) {
      executeLocalCommand("volume " + volume);
    }
    return true;
  }

  @Override
  public int getVolume() {
    if (localModeEnabled) {
      String cmdResult = executeLocalCommand("volume");
      return Integer.parseInt(cmdResult);
    }
    return 0;
  }

  @Override
  public boolean stop() {
    executeTelnetCommand("stop");
    return true;
  }

  @Override
  public boolean isVolumeControlEnabled() {
    return localModeEnabled;
  }
}
