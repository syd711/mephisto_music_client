package de.mephisto.test;

import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Song;
import de.mephisto.player.IMusicPlayer;
import de.mephisto.player.MpdPlayer;
import de.mephisto.service.impl.google.GoogleMusicProvider;
import de.mephisto.util.Config;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Syd
 * Date: 02.07.13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class PlayerTest {

    @Test
    public void testPlayback() {
        GoogleMusicProvider provider = new GoogleMusicProvider();
        provider.setProviderName("google");
        provider.setConfiguration(Config.getProviderConfiguration("google.properties"));
        provider.connect();
        provider.loadMusic();

        Song song = Dictionary.getInstance().getSongs().iterator().next();

        IMusicPlayer player = new MpdPlayer();
//        provider.playSong(player, song);
    }
}
