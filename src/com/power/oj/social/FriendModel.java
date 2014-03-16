package com.power.oj.social;

import com.jfinal.plugin.activerecord.Model;

public class FriendModel extends Model<FriendModel>
{
  private static final long serialVersionUID = 1L;
  
  public static final FriendModel dao = new FriendModel();
  
  public static final String TABLE_NAME = "friend";
  public static final String ID = "id";
  public static final String GID = "gid";
  public static final String USER_ID = "userId";
  public static final String FRIEND_UID = "friendUid";
  public static final String CTIME = "ctime";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public FriendModel setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getGid()
  {
    return getInt(GID);
  }
  
  public FriendModel setGid(Integer value)
  {
    return set(GID, value);
  }
  
  public Integer getUserId()
  {
    return getInt(USER_ID);
  }
  
  public FriendModel setUserId(Integer value)
  {
    return set(USER_ID, value);
  }
  
  public Integer getFriendUid()
  {
    return getInt(FRIEND_UID);
  }
  
  public FriendModel setFriendUid(Integer value)
  {
    return set(FRIEND_UID, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public FriendModel setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
