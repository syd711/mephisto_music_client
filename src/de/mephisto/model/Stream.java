package de.mephisto.model;

/**
 * Model for music streams
 */
public class Stream extends MModel {
  private String url;
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
