package de.mephisto.model;

/**
 * Model for music streams
 */
public class Stream extends MModel {
  private String url;
  private String name;
  private String title;
  private boolean playable = true;
  private boolean infoAvailable = true;

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

  public boolean isPlayable() {
    return playable;
  }

  public void setPlayable(boolean playable) {
    this.playable = playable;
  }

  public boolean isInfoAvailable() {
    return infoAvailable;
  }

  public void setInfoAvailable(boolean infoAvailable) {
    this.infoAvailable = infoAvailable;
  }
}
