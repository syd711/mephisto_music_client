package de.mephisto.rest.resources;

import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * REST resource for the settings actions.
 */
@Path("artwork")
public class ArtworkResource {
  private final static Logger LOG = LoggerFactory.getLogger(ArtworkResource.class);

  @GET
  @Path("cover/{id}")
  @Produces("image/png")
  public Response getArtwork(@PathParam("id")int id) {
    Song song = Dictionary.getInstance().getSong(id);
    try {
      byte[] data = song.getArtwork();
      BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
      byte[] imageData = baos.toByteArray();
      return Response.ok(imageData).build();
    }
    catch (Exception e) {
      LOG.error("Error build artwork for " + song + ": " + e.getMessage());
    }
    return null;
  }
}