package de.mephisto.model;

import de.mephisto.service.IMusicProvider;

/**
 * Representation of the provider
 */
public class ProviderInfo {
  private String name;
  private boolean enabled;
  private boolean connected;
  private int id;

  public ProviderInfo(IMusicProvider provider) {
    this.name = provider.getProviderName();
    this.enabled = provider.isEnabled();
    this.connected = provider.isConnected();
    this.id = provider.getInternalId();
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }
}
