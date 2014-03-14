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

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Mail setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getMid()
  {
    return get(MID);
  }
  
  public Mail setMid(Object value)
  {
    return set(MID, value);
  }
  
  public <T> T getUserId()
  {
    return get(USER_ID);
  }
  
  public Mail setUserId(Object value)
  {
    return set(USER_ID, value);
  }
  
  public <T> T getPeerUid()
  {
    return get(PEER_UID);
  }
  
  public Mail setPeerUid(Object value)
  {
    return set(PEER_UID, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Mail setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
