package com.power.oj.core.bean;

public class Message
{
  private MessageType type;
  private String title;
  private String content;

  public Message()
  {
  }

  public Message(String content)
  {
    this.content = content;
    this.type = MessageType.SUCCESS;
    this.title = "Success!";
  }

  public Message(String content, MessageType type, String title)
  {
    this.content = content;
    this.type = type;
    this.title = title;
  }
  
  public String getType()
  {
    return type.toString();
  }

  public void setType(MessageType type)
  {
    this.type = type;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

}
