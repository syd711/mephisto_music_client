package de.mephisto.player;

import de.mephisto.model.Playlist;
import de.mephisto.model.Song;
import de.mephisto.model.Stream;

import java.io.File;

/**
 * This interface has to be implement to delegate the playback to the corresponding player.
 */
public interface IMusicPlayer {

  /**
   * Playback of the given song collection
   *
   * @param songs
   */
  public Playlist play(Playlist songs);

  /**
   * Playback of a single song
   */
  public Song play(Song song);

  /**
   * Playback of a single song
   */
  public Song play(Playlist playlist, Song song);

  /**
   * Pause current playback
   */
  public boolean pause();

  /**
   * Playback of the next song of the active playlist
    */
  public Playlist next();

  /**
   * Sets the volume for the player, values between 0-100.
   * @param volume
   * @return
   */
  public boolean setVolume(int volume);

  /**
   * Returns the current volume of the player.
   * @return
   */
  public int getVolume();

  /**
   * Returns the playlist the player is playing.
   * @return
   */
  public Playlist getActivePlaylist();

  /**
   * Returns true if the music play implementation
   * can modify the playback volume.
   * @return
   */
  public boolean isVolumeControlEnabled();

  /**
   * Playback of the previous song.
   */
  public Playlist previous();

  /**
   * Playback of the given stream
   * @param stream
   */
  public boolean playStream(Stream stream);

  /**
   * Stops the player, used for reloading all data.
   */
  public boolean stop();

  /**
   * Returns true if the given file can be played
   */
  public boolean isPlayable(File f);
}
