package de.mephisto.rest.resources;

import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Album;
import de.mephisto.model.PlaylistCollection;
import de.mephisto.rest.JSONViews;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST resource for all album relevant actions.
 */
@Path("collections")
@Produces(MediaType.APPLICATION_JSON)
public class CollectionResource {

  @GET
  @Path("albums")
  @JsonView({JSONViews.AlbumsView.class})
  public PlaylistCollection getAlbums(@PathParam("sort") int sortType) {
    PlaylistCollection collection = new PlaylistCollection();
    collection.setItems(Dictionary.getInstance().getAlbumsWithoutSongs(sortType));
    return collection;
  }

  @GET
  @Path("album/{id}")
  @JsonView({JSONViews.AlbumView.class})
  public Album getAlbum(@PathParam("id") int id) {
    return Dictionary.getInstance().getAlbum(id);
  }
}