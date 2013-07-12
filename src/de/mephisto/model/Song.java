package de.mephisto.model;

import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The model that represents a song.
 */
public class Song extends MModel implements Comparable<Song> {
  private String id;
  private String name;
  private int year;
  private String artist;
  private String genre;
  private String album;
  private String albumArtUrl;
  private String url;
  private long durationMillis;
  private float creationDate;
  private int track;
  private String comment;
  private String composer;

  private String providerId;
  @JsonIgnore
  private Object originalModel;

  public Song(String providerId) {
    this.providerId = providerId;
  }


  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlbumArtUrl() {
    return albumArtUrl;
  }

  public void setAlbumArtUrl(String albumArtUrl) {
    this.albumArtUrl = albumArtUrl;
  }

  @Override
  public String toString() {
    return "'" + name + "' by " + artist;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public float getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(float creationDate) {
    this.creationDate = creationDate;
  }

  public long getDurationMillis() {
    return durationMillis;
  }

  public void setDurationMillis(long durationMillis) {
    this.durationMillis = durationMillis;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public Object getOriginalModel() {
    return originalModel;
  }

  public void setOriginalModel(Object originalModel) {
    this.originalModel = originalModel;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public int getTrack() {
    return track;
  }

  public void setTrack(int track) {
    this.track = track;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComposer() {
    return composer;
  }

  public void setComposer(String composer) {
    this.composer = composer;
  }

  public String getDuration() {
    if(this.durationMillis > 0) {
      return DateFormatUtils.format(this.durationMillis, "mm:ss");
    }
    return "";
  }

  @Override
  public int compareTo(Song o) {
    return track-o.getTrack();
  }
}
