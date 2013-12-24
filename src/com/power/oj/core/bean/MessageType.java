package com.power.oj.core.bean;

public enum MessageType
{
  ERROR("error"), 
  WRAN("wraning"), 
  INFO("info"), 
  SUCCESS("success");

  private String name;

  private MessageType(String name)
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return name;
  }

}
