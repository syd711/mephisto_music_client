package de.mephisto.player;

import de.mephisto.model.Playlist;
import de.mephisto.model.Song;

import java.io.File;

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
  public Song play(Playlist playlist, Song song) {
    this.activePlaylist = playlist;
    this.activePlaylist.setActiveSong(song);
    return play(song);
  }

  @Override
  public boolean pause() {
    return true;
  }

  @Override
  public Playlist next() {
    Song song = activePlaylist.nextSong();
    if(song != null && song.getProvider().isEnabled()) {
      play(activePlaylist, song);
    }
    return activePlaylist;
  }

  @Override
  public Playlist previous() {
    Song song = activePlaylist.previousSong();
    if(song != null && song.getProvider().isEnabled()) {
      play(activePlaylist, song);
    }
    return activePlaylist;
  }

  @Override
  public Playlist getActivePlaylist() {
    return activePlaylist;
  }

  @Override
  public boolean isPlayable(File f) {
    String name = f.getName();
    return name.endsWith(".mp3");
  }
}
