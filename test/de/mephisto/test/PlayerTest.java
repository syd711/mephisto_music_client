package de.mephisto.test;

import de.mephisto.Mephisto;
import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Playlist;
import de.mephisto.player.MpdPlayer;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Syd
 * Date: 02.07.13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class PlayerTest {

  @Test
  public void testPlayback() throws InterruptedException {
    Mephisto.getInstance();
    Iterator<Playlist> iterator = Dictionary.getInstance().getAlbums().iterator();
    iterator.next();
    iterator.next();
    Playlist p = iterator.next();

    MpdPlayer player = new MpdPlayer();
    Mephisto.getInstance().setPlayer(player);
    player.play(p);
  }
}
