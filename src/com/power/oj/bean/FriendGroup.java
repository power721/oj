package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class FriendGroup extends Model<FriendGroup>
{
  private static final long serialVersionUID = 1L;
  
  public static final FriendGroup dao = new FriendGroup();
  
  public static final String TABLE_NAME = "friend_group";
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String COUNT = "count";
  public static final String CTIME = "ctime";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public FriendGroup setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public FriendGroup setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public FriendGroup setName(String value)
  {
    return set(NAME, value);
  }
  
  public Integer getCount()
  {
    return getInt(COUNT);
  }
  
  public FriendGroup setCount(Integer value)
  {
    return set(COUNT, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public FriendGroup setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
