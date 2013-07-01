package de.mephisto.service.impl.google;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gmusic.api.impl.GoogleMusicAPI;
import de.mephisto.service.IMusicProvider;
import de.mephisto.util.Config;

/**
 * Google Music provider.
 */
public class GoogleMusicProvider implements IMusicProvider {
	private final static String GOOGLE_CONFIG = "google.properties";
	private final static Logger LOG = LoggerFactory.getLogger(GoogleMusicProvider.class);
	
	private GoogleMusicAPI api;
	private Configuration config;

	@Override
	public boolean connect() {
		config = Config.getConfiguration(GOOGLE_CONFIG);
		api = new GoogleMusicAPI();
		try {
			api.login(config.getString("google.login"), config.getString("google.password"));
			return true;
		}
		catch(Exception e) {
			LOG.error("Error connecting to Google:" + e.getMessage());
			return false;
		}
	}

	
}
