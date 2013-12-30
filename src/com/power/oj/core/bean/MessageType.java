package com.power.oj.core.bean;

public enum MessageType
{
  ERROR("error"), 
  WARN("warning"), 
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
