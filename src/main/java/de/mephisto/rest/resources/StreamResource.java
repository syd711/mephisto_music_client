package de.mephisto.rest.resources;

import de.mephisto.Mephisto;
import de.mephisto.data.StreamDictionary;
import de.mephisto.model.Stream;
import de.mephisto.model.Streams;

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
    Streams streams = new Streams();
    streams.setStreams(StreamDictionary.getInstance().getStreams());
    return streams;
  }

  @GET
  @Path("stream")
  public Stream getStream() {
    return Mephisto.getInstance().getPlayer().getActiveStream();
  }

  @GET
  @Path("get/{id}")
  public Stream getStream(@PathParam("id") int mid) {
    return StreamDictionary.getInstance().getStream(mid, true);
  }

  @GET
  @Path("delete/{id}")
  public boolean deleteStream(@PathParam("id") int mid) {
    return StreamDictionary.getInstance().deleteStream(mid);
  }

  @GET
  @Path("add")
  public Stream addStream(@QueryParam("url") String url) {
    return StreamDictionary.getInstance().addStream(url);
  }


  @GET
  @Path("play/{id}")
  public Stream playOrPause(@PathParam("id") int mid) {
    Stream stream = StreamDictionary.getInstance().getStream(mid, false);
    Mephisto.getInstance().getPlayer().play(stream);
    return stream;
  }

  @GET
  @Path("order")
  public boolean saveOrder(@QueryParam("order") String order) {
    return StreamDictionary.getInstance().saveOrder(order);
  }
}