package de.mephisto;

import com.sun.jersey.api.core.ScanningResourceConfig;
import de.mephisto.data.StreamDictionary;
import de.mephisto.http.Server;
import de.mephisto.player.IMusicPlayer;
import de.mephisto.player.PlayerFactory;
import de.mephisto.service.ProviderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main class of the Mephisto Music Web Client.
 */
public class Mephisto {
  final static Logger LOG = LoggerFactory.getLogger(Mephisto.class);

  private static Mephisto instance;

  private IMusicPlayer player;
  private ProviderManager providerManager;

  public static Mephisto getInstance() {
    if(instance == null) {
      instance = new Mephisto();
      instance.initServices();
    }
    return instance;
  }

  public IMusicPlayer getPlayer() {
    return player;
  }

  public ProviderManager getProviderManager() {
    return providerManager;
  }

  public void setPlayer(IMusicPlayer player) {
    this.player = player;
  }

  private Mephisto() {
    //force private singleton
  }

  /**
   * Initializes providers and creates the music dictionary.
   *
   * @throws IOException
   */
  private void initServices() {
    try {
      LOG.info("Mephisto is starting");

      LOG.info("Loading Player");
      player = PlayerFactory.createPlayer();

      LOG.info("Loading Music Providers");
      providerManager = new ProviderManager();

      LOG.info("Loading Streams");
      StreamDictionary.getInstance().init();
    }
    catch (Exception e) {
      LOG.info("Error starting Mephisto: " + e.getMessage(), e);
      System.exit(-1);
    }
  }


  /**
   * And in the beginning, there was main...
   *
   * @param args
   */
  public static void main(String[] args) throws IOException {
    Mephisto.getInstance();

    LOG.info("Starting http server on: " + Server.resolveHttpUrl());
    Server.start();
  }

}
