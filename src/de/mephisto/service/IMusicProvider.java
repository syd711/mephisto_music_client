package de.mephisto.service;


import de.mephisto.model.Song;
import de.mephisto.player.IMusicPlayer;
import org.apache.commons.configuration.Configuration;

/**
 * Interface to be implemented by Music providers like google or spotify.
 */
public interface IMusicProvider {

  /**
   * Initial authentication against the provider, etc.
   *
   * @return True, if the authentication is successful.
   */
  boolean connect();

  /**
   * Loads all music the provider can provide, obviously.
   */
  void loadMusic();

  /**
   * Returns the name of the provider, used as id for the provider factory.
   *
   * @return
   */
  String getProviderId();

  /**
   * The provider may have to provide an url for the playback, so delegate
   * the actual play command to it.
   *
   * @param song
   */
  void playSong(IMusicPlayer player, Song song);


  /**
   * Sets the name of the provider.
   *
   * @param providerId
   */
  void setProviderId(String providerId);


  /**
   * Sets the configuration file for the provider that has been loaded through
   * the provider factory.
   *
   * @param configuration
   */
  void setConfiguration(Configuration configuration);
}
