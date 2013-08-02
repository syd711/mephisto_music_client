package de.mephisto.rest.resources;

import de.mephisto.Mephisto;
import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Playlist;

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

  @GET
  @Path("playlist")
  public Playlist getActivePlaylist() {
    return Mephisto.getInstance().getPlayer().getActivePlaylist();
  }

  @GET
  @Path("play/{id}")
  public Playlist play(@PathParam("id") int id) {
    Playlist collection = Dictionary.getInstance().getPlaylist(id);
    Mephisto.getInstance().getPlayer().play(collection);
    return collection;
  }

  @GET
  @Path("next")
  public Playlist next() {
    return Mephisto.getInstance().getPlayer().next();
  }

  @GET
  @Path("previous")
  public Playlist previous() {
    return Mephisto.getInstance().getPlayer().previous();
  }

  @GET
  @Path("pause")
  public boolean pause() {
    return Mephisto.getInstance().getPlayer().pause();
  }

  @GET
  @Path("volume/set/{volume}")
  public int setVolume(@PathParam("volume") int volume) {
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