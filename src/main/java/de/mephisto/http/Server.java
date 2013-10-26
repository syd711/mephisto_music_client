package de.mephisto.http;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import de.mephisto.util.Config;

import java.io.IOException;

/**
 * Manages the HTTP server.
 */
public class Server {
  private final static String HOST = "http.host";
  private final static String PORT = "http.port";
  private final static String REST_CONTEXT = "http.rest.context";
  private final static String RESOURCES_CONTEXT = "http.resources.context";

  /**
   * Starts the http server
   *
   * @throws IllegalArgumentException
   * @throws IOException
   */
  public static void start() throws IllegalArgumentException, IOException {
    String resourceContext = Config.getHttpConfiguration().getString(RESOURCES_CONTEXT);
    HttpServer server = HttpServerFactory.create(resolveHttpUrl());
    server.createContext("/" + resourceContext, new ResourceHttpHandler());
    server.start();
  }

  /**
   * Return the URL the http server is starting on.
   *
   * @return
   */
  public static String resolveHttpUrl() {
    String host = Config.getHttpConfiguration().getString(HOST);
    String port = Config.getHttpConfiguration().getString(PORT);
    String context = Config.getHttpConfiguration().getString(REST_CONTEXT);
    return "http://" + host + ":" + port + "/" + context;
  }
}
