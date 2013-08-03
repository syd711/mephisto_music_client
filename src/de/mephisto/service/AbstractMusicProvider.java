package de.mephisto.service;

import org.apache.commons.configuration.Configuration;


/**
 * Abstract superclass for all music provider
 */
abstract public class AbstractMusicProvider implements IMusicProvider {
  private Configuration configuration;
  private String providerName;
  private boolean enabled = true;
  private int internalId;
  private boolean removable;

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public String getProviderName() {
    return providerName;
  }

  @Override
  public void setProviderName(String name) {
    this.providerName = name;
  }

  protected Configuration getConfiguration() {
    return configuration;
  }

  @Override
  public String toString() {
    return "Music Provider '" + getProviderName() + "'";
  }

  @Override
  public int getInternalId() {
    return internalId;
  }

  @Override
  public void setInternalId(int internalId) {
    this.internalId = internalId;
  }

  @Override
  public boolean isRemovable() {
    return removable;
  }

  @Override
  public void setRemovable(boolean removable) {
    this.removable = removable;
  }
}
