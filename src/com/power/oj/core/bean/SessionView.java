package com.power.oj.core.bean;

import java.util.Date;

public class SessionView {
  private String id;
  private String name;
  private String uri;
  private String ipAddress;
  private String userAgent;
  private long ctime;
  private long lastActivity;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public long getCtime() {
    return ctime;
  }

  public void setCtime(Date date) {
    this.ctime = date.getTime() / 1000;
  }

  public long getLastActivity() {
    return lastActivity;
  }

  public void setLastActivity(Date date) {
    this.lastActivity = date.getTime() / 1000;
  }

}
