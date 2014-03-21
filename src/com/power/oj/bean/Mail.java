package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail extends Model<Mail>
{
  private static final long serialVersionUID = 1L;
  
  public static final Mail dao = new Mail();
  
  public static final String TABLE_NAME = "mail";
  public static final String ID = "id";
  public static final String MID = "mid";
  public static final String USER = "user";
  public static final String PEER = "peer";
  public static final String STATUS = "status";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Mail setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getMid()
  {
    return getInt(MID);
  }
  
  public Mail setMid(Integer value)
  {
    return set(MID, value);
  }
  
  public Integer getUser()
  {
    return getInt(USER);
  }
  
  public Mail setUser(Integer value)
  {
    return set(USER, value);
  }
  
  public Integer getPeer()
  {
    return getInt(PEER);
  }
  
  public Mail setPeer(Integer value)
  {
    return set(PEER, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public Mail setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
