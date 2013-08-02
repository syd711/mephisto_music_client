package de.mephisto.player;

import de.mephisto.model.Playlist;
import de.mephisto.model.Song;

/**
 * Abstract superclass for music players.
 */
abstract public class AbstractMusicPlayer implements IMusicPlayer {
  private Playlist activePlaylist;

  @Override
  public Playlist play(Playlist playlist) {
    this.activePlaylist = playlist;
    return next();
  }

  @Override
  public boolean pause() {
    return true;
  }

  @Override
  public Playlist next() {
    Song song = activePlaylist.nextSong();
    if(song != null) {
      play(song);
    }
    return activePlaylist;
  }

  @Override
  public Playlist previous() {
    Song song = activePlaylist.previousSong();
    if(song != null) {
      play(song);
    }
    return activePlaylist;
  }

  @Override
  public Playlist getActivePlaylist() {
    return activePlaylist;
  }

}
