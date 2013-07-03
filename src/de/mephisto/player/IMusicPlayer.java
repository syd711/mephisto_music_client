package de.mephisto.player;

/**
 * This interface has to be implement to delegate the playback to the corresponding player.
 */
public interface IMusicPlayer {

    /**
     * Playback of the given stream url.
     * @param url
     */
    public void playUrl(String url);
}
