package de.mephisto.rest.resources;

import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Album;
import de.mephisto.model.PlaylistCollection;
import de.mephisto.model.Streams;
import de.mephisto.rest.JSONViews;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * REST resource for all stream relevant actions.
 */
@Path("streams")
@Produces(MediaType.APPLICATION_JSON)
public class StreamResource {

  @GET
  @Path("all")
  public Streams getStreams() {
    return null;//Dictionary.getInstance().getStreams();
  }
}