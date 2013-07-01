package de.mephisto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mephisto.http.Server;

/**
 * Main class of the Mephisto Music Web Client.
 *
 */
public class Mephisto {
	final static Logger logger = LoggerFactory.getLogger(Mephisto.class);

	
	public Mephisto() throws Exception {
		logger.info("Mephisto is starting");		
		logger.info("Starting http server on: " + Server.resolveHttpUrl());
		Server.start();		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {		
		new Mephisto();
	}

}
