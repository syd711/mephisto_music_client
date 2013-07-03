package de.mephisto;

import de.mephisto.player.IMusicPlayer;
import de.mephisto.player.PlayerFactory;
import de.mephisto.service.MusicProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mephisto.http.Server;

import java.io.IOException;

/**
 * Main class of the Mephisto Music Web Client.
 *
 */
public class Mephisto {
	final static Logger LOG = LoggerFactory.getLogger(Mephisto.class);

    private static Mephisto instance;

    private IMusicPlayer player;

    public static Mephisto getInstance() {
        return instance;
    }

    public IMusicPlayer getPlayer() {
        return player;
    }

    //force private singleton
	private Mephisto() throws Exception {

	}

    /**
     * Initializes providers and creates the music dictionary.
     * @throws IOException
     */
    private void initServices() throws IOException {
        LOG.info("Mephisto is starting");
        LOG.info("Starting http server on: " + Server.resolveHttpUrl());
        Server.start();

        LOG.info("Loading Player");
        player = PlayerFactory.createPlayer();

        LOG.info("Loading Music Providers");
        MusicProviderFactory.init();
    }
	

	/**
     * And in the beginning, there was main...
	 * @param args
	 */
	public static void main(String[] args) throws Exception {		
		instance = new Mephisto();
        instance.initServices();
	}

}
