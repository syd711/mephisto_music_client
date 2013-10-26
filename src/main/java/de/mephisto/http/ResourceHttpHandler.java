package de.mephisto.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.mephisto.Mephisto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

public class ResourceHttpHandler implements HttpHandler {
  FileNameMap fileNameMap = URLConnection.getFileNameMap();

  public void handle(HttpExchange exchange) throws IOException {
    Mephisto.getInstance(); //init on first request

    String requestMethod = exchange.getRequestMethod();
    String targetPath = exchange.getRequestURI().getPath();
    if (requestMethod.equalsIgnoreCase("GET")) {

      // Here it's a good place to include custom path handling,
      // like calls to Java functions

      // Handle paths trying to open index.html
      // Instead of this, we could show a list of the files in the folder
      if (targetPath.endsWith("/")) {
        targetPath += "index.html";
      }

      // Check if file exists
      File fileFolder = new File(".");
      File targetFile = new File(fileFolder, targetPath.replace('/',
          File.separatorChar));

      if (targetFile.exists() && targetFile.isFile()) {
        // If it exists and it's a file, serve it
        int bufLen = 10000 * 1024;
        byte[] buf = new byte[bufLen];
        int len = 0;
        Headers responseHeaders = exchange.getResponseHeaders();

        // Get mime type from the ones defined in
        // [jre_home]/lib/content-types.properties
        String mimeType = fileNameMap.getContentTypeFor(targetFile
            .toURI().toURL().toString());

        if (targetFile.getName().endsWith("css")) {
          mimeType = "text/css";
        }
        else if (targetFile.getName().endsWith("js")) {
          mimeType = "text/javascript";
        }
        else if(targetFile.getName().contains(".woff")) {
          mimeType = "application/x-font-woff";
        }
        else if (mimeType == null) {
          mimeType = "application/octet-stream";
        }

        responseHeaders.set("Content-Type", mimeType);

        exchange.sendResponseHeaders(200, targetFile.length());

        FileInputStream fileIn = new FileInputStream(targetFile);
        OutputStream out = exchange.getResponseBody();

        while ((len = fileIn.read(buf, 0, bufLen)) != -1) {
          out.write(buf, 0, len);
        }

        out.close();
        fileIn.close();
      }
      else {
        // If it doesn't exist, send error
        String message = "404 Not Found " + targetFile.getAbsolutePath();
        exchange.sendResponseHeaders(404, 0);
        OutputStream out = exchange.getResponseBody();
        out.write(message.getBytes());
        out.close();
      }
    }
  }

}
