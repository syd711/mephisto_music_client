package de.mephisto;

import de.mephisto.http.Server;
import de.mephisto.player.IMusicPlayer;
import de.mephisto.player.PlayerFactory;
import de.mephisto.service.MusicProviderFactory;
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

  public static Mephisto getInstance() {
    return instance;
  }

  public IMusicPlayer getPlayer() {
    return player;
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
      MusicProviderFactory.init();

      LOG.info("Starting http server on: " + Server.resolveHttpUrl());
      Server.start();
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
  public static void main(String[] args) {
    instance = new Mephisto();
    instance.initServices();
  }

}
