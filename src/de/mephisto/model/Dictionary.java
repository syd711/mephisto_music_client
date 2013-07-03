package de.mephisto.model;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The collected music, gathered by all music providers.
 */
public class Dictionary {
    private final static Logger LOG = LoggerFactory.getLogger(Dictionary.class);
    private static Dictionary instance = new Dictionary();

    private Map<String, Stream> streams = new HashMap<String, Stream>();
    private Map<String, Song> songs = new HashMap<String, Song>();
    private Map<String, Genre> genres = new HashMap<String, Genre>();
    private Map<String, Album> albums = new HashMap<String, Album>();

    /**
     * Returns the singleton instance of the dictionary.
     *
     * @return
     */
    public static Dictionary getInstance() {
        return instance;
    }

    public Collection<Song> getSongs() {
        return songs.values();
    }

    /**
     * Adds a song to the dictionary, updates all sub-dictionaries afterwards.
     * @param song The song to add.
     */
    public void addSong(Song song) {
        songs.put(song.getId(), song);
        addToAlbum(song);
        addToGenre(song);
        LOG.debug("Add song " + song);
    }

    /**
     * Adds the song to its genre or creates a new one.
     * @param song
     */
    private void addToGenre(Song song) {
        if (!StringUtils.isEmpty(song.getGenre())) {
            Genre genre = null;
            if (!genres.containsKey(song.getGenre())) {
                genre = new Genre(song.getGenre());
                genres.put(song.getAlbum(), genre);
            } else {
                genre = genres.get(song.getAlbum());
            }
            genre.addSong(song);
        }
    }

    /**
     * Creates an album for the song if it does not exist yet.
     * @param song The song to add to the album.
     */
    private void addToAlbum(Song song) {
        if (!StringUtils.isEmpty(song.getAlbum())) {
            Album album = null;
            if (!albums.containsKey(song.getAlbum())) {
                album = new Album(song.getArtist(), song.getName());
                albums.put(song.getAlbum(), album);
            } else {
                album = albums.get(song.getAlbum());
            }
            album.addSong(song);
        }
    }

    public void addStream(Stream stream) {
        streams.put(stream.getUrl(), stream);
    }
}
