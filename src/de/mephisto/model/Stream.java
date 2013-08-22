package de.mephisto.model;

/**
 * Model for music streams
 */
public class Stream extends MModel {
  private String url;
  private String name;
  private String title;

  public Stream(int mid) {
    super.setMID(mid);
  }

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
