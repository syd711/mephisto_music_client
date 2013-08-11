package de.mephisto.rest;

import de.mephisto.player.PlayerException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PlayerExceptionMapper implements ExceptionMapper<PlayerException> {
  public Response toResponse(PlayerException ex) {
    return Response.status(Response.Status.NOT_ACCEPTABLE).entity(ex.getMessage()).type("text/plain").build();
  }
}