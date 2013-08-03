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
  String getProviderName();

  /**
   * The provider may have to provide an url for the playback.
   *
   * @param song
   */
  String getUrl(Song song);


  /**
   * Sets the name of the provider.
   *
   * @param name
   */
  void setProviderName(String name);


  /**
   * Sets the configuration file for the provider that has been loaded through
   * the provider factory.
   *
   * @param configuration
   */
  void setConfiguration(Configuration configuration);

  /**
   * Status update
   * @param enabled
   */
  void setEnabled(boolean enabled);
  boolean isEnabled();

  /**
   * Used to access the provider via REST
   * @return
   */
  int getInternalId();
  void setInternalId(int id);

  /**
   * Returns true if the provider is working on a removable device.
   * @return
   */
  boolean isRemovable();
  void setRemovable(boolean b);
}
