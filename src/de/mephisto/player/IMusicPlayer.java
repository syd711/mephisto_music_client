package de.mephisto.player;

import de.mephisto.model.Playlist;
import de.mephisto.model.Stream;

/**
 * This interface has to be implement to delegate the playback to the corresponding player.
 */
public interface IMusicPlayer {

  /**
   * Playback of the given song collection
   *
   * @param songs
   */
  public void playSongCollection(Playlist songs);

  /**
   * Playback of the given stream
   * @param stream
   */
  public void playStream(Stream stream);
}
