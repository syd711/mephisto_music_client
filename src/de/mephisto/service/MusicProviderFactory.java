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

  private final static String PROVIDER_NAME = "provider.name";
  private final static String PROVIDER_CLASS = "provider.class";
  private final static String PROVIDER_ENABLED = "provider.enabled";
  private final static String PROVIDER_REMOVABLE = "provider.removable";

  private static Map<Integer, IMusicProvider> providerMap = new HashMap<Integer, IMusicProvider>();
  private static int providerIdCounter = 0;

  /**
   * Loads the available providers
   */
  public static void init() {
    providerIdCounter = 0;
    providerMap.clear();

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
          provider.setProviderName(config.getString(PROVIDER_NAME));
          provider.setInternalId(providerIdCounter);
          provider.setRemovable(config.getBoolean(PROVIDER_REMOVABLE, false));
          providerMap.put(providerIdCounter, provider);
          providerIdCounter++;

          LOG.info("Loaded " + provider);
          initProvider(provider);

        } catch (Exception e) {
          LOG.error("Could not load provider class " + className + ": " + e.getMessage(), e);
        }
      }
    }
    LOG.info("Finished initialization of music providers, loaded " + providerMap.size() + " provider(s)");
  }

  private static void initProvider(IMusicProvider provider) {
    if (provider.isEnabled() && provider.connect()) {
      LOG.info("Connected " + provider);
      LOG.info("Loading data for " + provider);
      provider.loadMusic();
    }
    else {
      provider.setEnabled(false);
      LOG.info("Did not connect to " + provider);
    }
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
   * @param internalId
   * @return
   */
  public static IMusicProvider getProvider(int internalId) {
    return providerMap.get(internalId);
  }

  /**
   * Reloads the all enabled providers.
   */
  public static void refresh() {
    for(IMusicProvider provider : providerMap.values()) {
      initProvider(provider);
    }
  }

  /**
   * Checks the disabled provider if they provide any data meanwhile.
   * @return
   */
  public static boolean runDetectionCheck() {
    boolean detected = false;
    for(IMusicProvider provider : providerMap.values()) {
      if(!provider.isEnabled()) {
        if(provider.connect()) {
          detected = true;
        }
      }
    }
    return detected;
  }
}
