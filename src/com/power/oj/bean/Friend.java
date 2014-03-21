package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Friend extends Model<Friend>
{
  private static final long serialVersionUID = 1L;
  
  public static final Friend dao = new Friend();
  
  public static final String TABLE_NAME = "friend";
  public static final String ID = "id";
  public static final String GID = "gid";
  public static final String USER_ID = "userId";
  public static final String FRIEND_UID = "friendUid";
  public static final String CTIME = "ctime";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Friend setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getGid()
  {
    return getInt(GID);
  }
  
  public Friend setGid(Integer value)
  {
    return set(GID, value);
  }
  
  public Integer getUserId()
  {
    return getInt(USER_ID);
  }
  
  public Friend setUserId(Integer value)
  {
    return set(USER_ID, value);
  }
  
  public Integer getFriendUid()
  {
    return getInt(FRIEND_UID);
  }
  
  public Friend setFriendUid(Integer value)
  {
    return set(FRIEND_UID, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Friend setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
