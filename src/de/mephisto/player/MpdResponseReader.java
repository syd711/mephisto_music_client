package de.mephisto.player;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Thread for listening on the MPD telnet responses, for logging purposes.
 */
public class MpdResponseReader extends Thread {
  private final static Logger LOG = LoggerFactory.getLogger(MpdPlayer.class);

  private TelnetClient client;
  private InputStream in;
  private boolean running;
  private Thread waiter;

  public MpdResponseReader(TelnetClient client) {
    this.client = client;
    this.setRunning(true);
  }

  @Override
  public void run() {
    Thread.currentThread().setName("MPD Response Reader");
    try {
      if (in == null) {
        in = client.getInputStream();
      }
      byte[] buff = new byte[1024];
      int ret_read = 0;

      do {
        ret_read = in.read(buff);
        if (ret_read > 0) {
          String msg = new String(buff, 0, ret_read).trim();
          LOG.info("MPD: " + msg);
          if (msg.contains("OK") && this.waiter != null) {
            this.waiter.notify();
          }
        }
      }
      while (ret_read >= 0 && running);
      LOG.info("Terminating Mpd Response Reader thread.");
    } catch (IOException e) {
      LOG.error("Exception while reading from MPD:" + e.getMessage());
    }
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public void awaitOk() {
    try {
      this.waiter = Thread.currentThread();
      this.waiter.wait();
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
