package de.mephisto.player;

import de.mephisto.model.Playlist;
import de.mephisto.model.Song;
import de.mephisto.model.Stream;
import de.mephisto.service.IMusicProvider;
import de.mephisto.service.MusicProviderFactory;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Player for a windows system, like winamp.
 */
public class WindowsPlayer extends AbstractMusicPlayer {
  private final static Logger LOG = LoggerFactory.getLogger(MpdPlayer.class);

  @Override
  public Song play(Song song) {
//    try {
//      IMusicProvider provider = MusicProviderFactory.getProvider(song.getProviderName());
//      String url = provider.getUrl(song);
//      String line = "\"c:\\Program Files\\Winamp\\winamp.exe\" " + url;
//      CommandLine cmdLine = CommandLine.parse(line);
//      DefaultExecutor executor = new DefaultExecutor();
//      executor.execute(cmdLine);
//    } catch (IOException e) {
//      LOG.error("Error executing system command for win player: " + e.getMessage());
//    }
    return song;
  }

  @Override
  public void playStream(Stream stream) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void setVolume(int volume) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void stop() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public int getVolume() {
    return 95;
  }

  @Override
  public boolean isVolumeControlEnabled() {
    return true;
  }
}
