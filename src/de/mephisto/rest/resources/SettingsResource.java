package de.mephisto.rest.resources;

import de.mephisto.Mephisto;
import de.mephisto.dictionary.Dictionary;
import de.mephisto.model.ProviderInfo;
import de.mephisto.model.Settings;
import de.mephisto.player.PlayerFactory;
import de.mephisto.service.IMusicProvider;
import de.mephisto.service.MusicProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * REST resource for the settings actions.
 */
@Path("settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsResource {
  private final static Logger LOG = LoggerFactory.getLogger(SettingsResource.class);

  @GET
  @Path("providers/reload")
  public boolean reloadProviders() {
    LOG.info("Resetting providers, dictionary and player...");
    Mephisto.getInstance().getPlayer().stop();
    Mephisto.getInstance().setPlayer(PlayerFactory.createPlayer());

    Dictionary.getInstance().reset();

    MusicProviderFactory.init();
    return true;
  }

  @GET
  @Path("providers/detect")
  public boolean detectProviders() {
    LOG.info("Checking for removable devices");
    if(MusicProviderFactory.runDetectionCheck()) {
      Mephisto.getInstance().getPlayer().stop();
      Dictionary.getInstance().reset();
      MusicProviderFactory.refresh();
      return true;
    }

    return false;
  }

  @GET
  @Path("get")
  public Settings get() {
    Settings settings = new Settings();
    settings.setAlbums(Dictionary.getInstance().getAlbums().size());
    settings.setSongs(Dictionary.getInstance().getSongs().size());

    Collection<IMusicProvider> providers = MusicProviderFactory.getProviders();
    for (IMusicProvider provider : providers) {
      ProviderInfo info = new ProviderInfo(provider);
      settings.getProviders().add(info);
    }
    return settings;
  }

  @GET
  @Path("provider/{id}/enable/{enable}")
  public boolean enableProvider(@PathParam("id") int providerId, @PathParam("enable") boolean enable) {
    Mephisto.getInstance().getPlayer().stop();
    Dictionary.getInstance().reset();

    IMusicProvider provider = MusicProviderFactory.getProvider(providerId);
    LOG.info("Settings " + provider + " to " + enable);
    provider.setEnabled(enable);

    MusicProviderFactory.refresh();
    return true;
  }
}