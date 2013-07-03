package de.mephisto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The model that represents an album.
 */
public class Album extends SongCollection {
    private String artist;

    public Album(String artist, String name) {
        super(name);
        this.artist = artist;
    }
}
