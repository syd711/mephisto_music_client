package de.mephisto.player;

import de.mephisto.model.Playlist;
import de.mephisto.model.Song;
import de.mephisto.model.Stream;

import java.io.File;

/**
 * Abstract superclass for music players.
 */
abstract public class AbstractMusicPlayer implements IMusicPlayer {
  protected Playlist activePlaylist;
  protected Stream activeStream;
  private boolean paused = false;

  @Override
  public Playlist play(Playlist playlist) {
    if(playlist == activePlaylist) {
      play(playlist, playlist.getActiveSong());
      return activePlaylist;
    }
    else {
      this.activePlaylist = playlist;
      this.activeStream = null;
      return next();
    }

  }

  @Override
  public Song play(Playlist playlist, Song song) {
    this.activeStream = null;
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
    activePlaylist.nextSong();
    return activePlaylist;
  }

  @Override
  public Playlist previous() {
    activePlaylist.previousSong();
    return activePlaylist;
  }

  @Override
  public Playlist getActivePlaylist() {
    return activePlaylist;
  }

  @Override
  public Stream getActiveStream() {
    return activeStream;
  }


  @Override
  public boolean isPlayable(File f) {
    String name = f.getName();
    return name.endsWith(".mp3");
  }

  public boolean isPaused() {
    return paused;
  }

  public void setPaused(boolean paused) {
    this.paused = paused;
  }
}
