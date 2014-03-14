package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Friend extends Model<Friend>
{
  private static final long serialVersionUID = 1L;
  
  public static final Friend dao = new Friend();
  
  public static final String ID = "id";
  public static final String GID = "gid";
  public static final String USER_ID = "userId";
  public static final String FRIEND_UID = "friendUid";
  public static final String CTIME = "ctime";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Friend setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getGid()
  {
    return get(GID);
  }
  
  public Friend setGid(Object value)
  {
    return set(GID, value);
  }
  
  public <T> T getUserId()
  {
    return get(USER_ID);
  }
  
  public Friend setUserId(Object value)
  {
    return set(USER_ID, value);
  }
  
  public <T> T getFriendUid()
  {
    return get(FRIEND_UID);
  }
  
  public Friend setFriendUid(Object value)
  {
    return set(FRIEND_UID, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Friend setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
