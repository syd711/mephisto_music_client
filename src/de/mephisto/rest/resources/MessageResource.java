package de.mephisto.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("message")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {
  @GET
  public ServerInfo message() {
    ServerInfo info = new ServerInfo();
    info.server = System.getProperty("os.name") + " " + System.getProperty("os.version");
    return info;
  }

  static class ServerInfo {
    public String server;
  }
}