package de.mephisto.rest.resources;

import de.mephisto.Mephisto;
import de.mephisto.data.MusicDictionary;
import de.mephisto.model.Album;
import de.mephisto.model.Playlist;
import de.mephisto.model.PlaylistCollection;
import de.mephisto.rest.JSONViews;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.ws.rs.*;
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
  public PlaylistCollection getAlbums() {
    PlaylistCollection collection = new PlaylistCollection();
    collection.setItems(MusicDictionary.getInstance().getAlbums());
    return collection;
  }

  @GET
  @Path("artist/albums/{id}")
  @JsonView({JSONViews.AlbumsView.class})
  public PlaylistCollection getArtistAlbums(@PathParam("id") int mid) {
    PlaylistCollection collection = new PlaylistCollection();
    collection.setItems(MusicDictionary.getInstance().getAlbumsOfArtist(mid));
    return collection;
  }

  @GET
  @Path("genre/albums/{id}")
  @JsonView({JSONViews.AlbumsView.class})
  public PlaylistCollection getGenreAlbums(@PathParam("id") int mid) {
    PlaylistCollection collection = new PlaylistCollection();
    collection.setItems(MusicDictionary.getInstance().getAlbumsOfGenre(mid));
    return collection;
  }


  @GET
  @Path("album/{id}")
  @JsonView({JSONViews.AlbumView.class})
  public Album getAlbum(@PathParam("id") int id) {
    Album album =  MusicDictionary.getInstance().getAlbum(id);
    final Playlist activePlaylist = Mephisto.getInstance().getPlayer().getActivePlaylist();
    if(activePlaylist != null && album.getMID() != activePlaylist.getMID()) {
      album.setActiveSong(null);
    }
    return album;
  }

  @GET
  @Path("search")
  @JsonView({JSONViews.AlbumsView.class})
  public PlaylistCollection search(@QueryParam("term") String term) {
    PlaylistCollection collection = new PlaylistCollection();
    collection.setItems(MusicDictionary.getInstance().search(term));
    return collection;
  }
}