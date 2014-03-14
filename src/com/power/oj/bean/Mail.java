package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail extends Model<Mail>
{
  private static final long serialVersionUID = 1L;
  
  public static final Mail dao = new Mail();
  
  public static final String ID = "id";
  public static final String MID = "mid";
  public static final String USER_ID = "userId";
  public static final String PEER_UID = "peerUid";
  public static final String STATUS = "status";

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
  
  public Integer getUserId()
  {
    return getInt(USER_ID);
  }
  
  public Mail setUserId(Integer value)
  {
    return set(USER_ID, value);
  }
  
  public Integer getPeerUid()
  {
    return getInt(PEER_UID);
  }
  
  public Mail setPeerUid(Integer value)
  {
    return set(PEER_UID, value);
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
