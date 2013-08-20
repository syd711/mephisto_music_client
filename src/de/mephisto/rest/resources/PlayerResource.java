package de.mephisto.rest.resources;

import de.mephisto.Mephisto;
import de.mephisto.data.MusicDictionary;
import de.mephisto.model.Playlist;
import de.mephisto.model.Song;
import de.mephisto.rest.JSONViews;
import org.codehaus.jackson.map.annotate.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST resource for the player actions.
 */
@Path("player")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
  private final static Logger LOG = LoggerFactory.getLogger(PlayerResource.class);

  @GET
  @Path("playlist")
  @JsonView({JSONViews.AlbumsView.class})
  public Playlist getActivePlaylist() {
    return Mephisto.getInstance().getPlayer().getActivePlaylist();
  }

  @GET
  @Path("play/{id}")
  @JsonView({JSONViews.AlbumsView.class})
  public Playlist play(@PathParam("id") int id) {
    Playlist collection = MusicDictionary.getInstance().getPlaylist(id);
    if(Mephisto.getInstance().getPlayer().getActivePlaylist() == null) {
      LOG.info("Playing new playlist " + collection);
      Mephisto.getInstance().getPlayer().play(collection);
      return collection;
    }
    LOG.info("Continuing playback of existing playlist.");
    return Mephisto.getInstance().getPlayer().play(collection);
  }

  @GET
  @Path("playsong/{collectionId}/{id}")
  @JsonView({JSONViews.AlbumsView.class})
  public Playlist playSong(@PathParam("collectionId") int collectionId, @PathParam("id") int id) {
    Playlist collection = MusicDictionary.getInstance().getPlaylist(collectionId);
    Song song = collection.getSong(id);
    Mephisto.getInstance().getPlayer().play(collection, song);
    return collection;
  }

  @GET
  @Path("next")
  @JsonView({JSONViews.AlbumsView.class})
  public Playlist next() {
    LOG.info("Playing next song");
    return Mephisto.getInstance().getPlayer().next();
  }

  @GET
  @Path("previous")
  @JsonView({JSONViews.AlbumsView.class})
  public Playlist previous() {
    LOG.info("Playing previous song");
    return Mephisto.getInstance().getPlayer().previous();
  }

  @GET
  @Path("pause")
  public boolean pause() {
    LOG.info("Pausing player");
    return Mephisto.getInstance().getPlayer().pause();
  }

  @GET
  @Path("volume/set/{volume}")
  public int setVolume(@PathParam("volume") int volume) {
    LOG.info("Applying volume level " + volume);
    Mephisto.getInstance().getPlayer().setVolume(volume);
    return volume;
  }

  @GET
  @Path("volume/get")
  public int getVolume() {
    return Mephisto.getInstance().getPlayer().getVolume();
  }

  @GET
  @Path("volumeenabled")
  public boolean volume() {
    return Mephisto.getInstance().getPlayer().isVolumeControlEnabled();
  }
}