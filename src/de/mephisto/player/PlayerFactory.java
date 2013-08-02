package de.mephisto.player;

/**
 * Creates the player that is used for playing music and streams.
 */
public class PlayerFactory {

  public static IMusicPlayer createPlayer() {
    return new WindowsPlayer();
//    return new MpdPlayer();
  }
}
