package de.mephisto.player;

import de.mephisto.Mephisto;
import de.mephisto.model.Song;
import de.mephisto.model.Stream;
import de.mephisto.service.IMusicProvider;
import de.mephisto.util.Config;
import de.mephisto.util.MPDClient;
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

  private Song current;
  private int volume = 99;
  private MPDClient client;
  private Configuration config;
  private MPDStatusListener statusListenerThread;

  public MpdPlayer() {
    this.config = Config.getConfiguration(CONFIG_NAME);
    String host = config.getString(PROPERTY_HOST);
    int port = config.getInt(PROPERTY_PORT, 6600);
    client = new MPDClient(host, port);
    client.connect();

    //listens on the playlist status
    statusListenerThread = new MPDStatusListener();
    statusListenerThread.start();

    if(client.isLocalModeEnabled()) {
      client.executeLocalCommand("volume " + volume);
    }
  }

  @Override
  public boolean pause() {
    setPaused(true);
    client.executeTelnetCommand("pause");
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
      setPaused(false);
      LOG.info("Playback of: " + song);
      //continue paused song
      if (song == current) {
        LOG.info("MPD Player continues playback of " + song);
        client.executeTelnetCommand("play");
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
    LOG.info("Playback of URL: " + url);
    try {
      client.executeTelnetCommand("stop");
      client.executeTelnetCommand("clear");
      client.executeTelnetCommand("add " + url);
      client.executeTelnetCommand("play");
    } catch (PlayerException e) {
      LOG.error("Failed to playback " + url + ": " + e.getMessage());
      client.connect();
      throw e;
    }
  }



  @Override
  public boolean setVolume(int volume) {
    if (client.isLocalModeEnabled()) {
      this.volume = volume;
      client.executeLocalCommand("volume " + volume);
    }
    return true;
  }

  @Override
  public int getVolume() {
    if (client.isLocalModeEnabled()) {
      return this.volume;
    }
    return 0;
  }

  @Override
  public boolean stop() {
    client.executeTelnetCommand("stop");
    return true;
  }

  @Override
  public boolean isVolumeControlEnabled() {
    return client.isLocalModeEnabled();
  }

  /**
   * Thread that checks the mpd status and updates the playlist
   * if a song has finished playing.
   */
  class MPDStatusListener extends Thread {
    private Song current;
    private long millis;

    @Override
    public void run() {
      LOG.info("Started MPD player status thread.");
      while (true) {
        try {
          Thread.sleep(100);
          if(activePlaylist != null) {
            Song song = activePlaylist.getActiveSong();
            if(song != null) {
              if(current == null || song.getMID() != current.getMID()) {
                LOG.info("Detected new song " + song);
                current = song;
                millis = song.getDurationMillis();
              }
              if(!isPaused())  {
                millis-=100; //TODO mpc status query
                if(millis <= 0)  {
                  next();
                  if(activePlaylist.getActiveSong() != null) {
                    play(activePlaylist.getActiveSong());
                  }
                }
              }
            }
          }
        }
        catch (Exception e) {
          LOG.error("Error in status listener thread: " + e.getMessage(), e);
        }
      }
    }
  }
}
