package de.mephisto.test;

import de.mephisto.Mephisto;
import de.mephisto.data.MusicDictionary;
import de.mephisto.data.StreamDictionary;
import de.mephisto.model.Playlist;
import de.mephisto.model.Stream;
import de.mephisto.player.MpdPlayer;
import de.mephisto.util.StreamHelper;
import org.junit.Test;

import java.util.Iterator;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Syd
 * Date: 02.07.13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class StreamsTest {

  @Test
  public void testPlayback() throws InterruptedException {
    StreamDictionary.getInstance().init();
    assertTrue(!StreamDictionary.getInstance().getStreams().isEmpty());
  }

  @Test
  public void testStreamInfo() {
    Stream stream = new Stream(0);
    stream.setUrl("http://sdfsdfsf");
    StreamHelper.loadInfo(stream);
  }
}
