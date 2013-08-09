package de.mephisto.player;

import de.mephisto.util.Config;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates the player that is used for playing music and streams.
 */
public class PlayerFactory {
  private final static Logger LOG = LoggerFactory.getLogger(PlayerFactory.class);

  private final static String PLAYER_CONFIG = "player.properties";

  public static IMusicPlayer createPlayer() {
    try {
      Configuration c = Config.getConfiguration(PLAYER_CONFIG);
      String className = c.getString("player.classname");
      IMusicPlayer player = (IMusicPlayer) Class.forName(className).newInstance();
      return player;
    }
    catch (Exception e) {
      LOG.error("Error creating player instance: " + e.getMessage(), e);
    }
    return null;
  }
}
