package de.mephisto.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for accessing the different config files.
 */
public class Config {
  private final static Logger LOG = LoggerFactory.getLogger(Config.class);

  private final static String HTTP_CONFIG = "http.properties";
  public final static String CONFIG_FOLDER = "conf/";
  public final static String PROVIDER_CONFIG_FOLDER = "providers/";

  public static Configuration getHttpConfiguration() {
    return getConfiguration(HTTP_CONFIG);
  }

  public static Configuration getProviderConfiguration(String name) {
    return getConfiguration(PROVIDER_CONFIG_FOLDER + name);
  }

  public static Configuration getConfiguration(String name) {
    try {
      Configuration config = new PropertiesConfiguration(
          getConfigFile(name));
      return config;
    } catch (ConfigurationException e) {
      LOG.error("Error loading " + name + " config: " + e.getMessage(), e);
    }
    return null;
  }

  private static String getConfigFile(String config) {
    return CONFIG_FOLDER + config;
  }
}
