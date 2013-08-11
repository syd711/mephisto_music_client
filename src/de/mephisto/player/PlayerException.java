package de.mephisto.player;

/**
 * Custom exception for player errors.
 */
public class PlayerException extends RuntimeException {

  /**
   * @param message the String that is the entity of the response.
   */
  public PlayerException(String message) {
    super(message);
  }
}