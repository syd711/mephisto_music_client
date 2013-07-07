package de.mephisto.service;

import de.mephisto.util.Config;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MusicProviderFactory {
  private final static Logger LOG = LoggerFactory.getLogger(MusicProviderFactory.class);

  private final static String PROVIDER_ID = "provider.id";
  private final static String PROVIDER_CLASS = "provider.class";
  private final static String PROVIDER_ENABLED = "provider.enabled";

  private static Map<String, IMusicProvider> providerMap = new HashMap<String, IMusicProvider>();

  /**
   * Loads the available providers
   */
  public static void init() {
    File configFolder = new File(Config.CONFIG_FOLDER + Config.PROVIDER_CONFIG_FOLDER);
    File[] providerConfigs = configFolder.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".properties");
      }
    });
    for (File configFile : providerConfigs) {
      Configuration config = Config.getProviderConfiguration(configFile.getName());
      Boolean enabled = config.getBoolean(PROVIDER_ENABLED, true);
      if (enabled) {
        String className = config.getString(PROVIDER_CLASS);
        try {
          Class clazz = Class.forName(className);
          IMusicProvider provider = (IMusicProvider) clazz.newInstance();
          provider.setConfiguration(config);
          provider.setProviderId(config.getString(PROVIDER_ID));
          LOG.info("Loaded " + provider);

          if (provider.connect()) {
            LOG.info("Connected " + provider);
            providerMap.put(provider.getProviderId(), provider);

            LOG.info("Loading data for " + provider);
            provider.loadMusic();
          }
          else {
            LOG.error("Failed to connect to " + provider);
          }

        } catch (Exception e) {
          LOG.error("Could not load provider class " + className + ": " + e.getMessage(), e);
        }
      }
    }
    LOG.info("Finished initialization of music providers, loaded " + providerMap.size() + " provider(s)");
  }

  /**
   * Returns the collection of available providers.
   *
   * @return
   */
  public static Collection<IMusicProvider> getProviders() {
    return providerMap.values();
  }

  /**
   * Returns the music provider with the given id.
   * @param providerId
   * @return
   */
  public static IMusicProvider getProvider(String providerId) {
    return providerMap.get(providerId);
  }
}
