package de.mephisto.rest.resources;

import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Playlist;
import de.mephisto.model.PlaylistCollection;
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
  @Path("play/{id}")
  public Playlist play(@PathParam("id") int id) {
    Playlist collection = Dictionary.getInstance().getPlaylist(id);
    collection.setActiveSong(collection.getSongs().get(0));
    LOG.info("Playback of " + collection);
    return collection;
  }

  @GET
  @Path("pause")
  public boolean pause() {
    return true;
  }
}