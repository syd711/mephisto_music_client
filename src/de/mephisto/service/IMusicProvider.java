package de.mephisto.service;

/**
 * Interface to be implemented by Music providers like google or spotify.
 */
public interface IMusicProvider {
	boolean connect();
}
