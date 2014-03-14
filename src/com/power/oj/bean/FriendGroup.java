package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class FriendGroup extends Model<FriendGroup>
{
  private static final long serialVersionUID = 1L;
  
  public static final FriendGroup dao = new FriendGroup();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String COUNT = "count";
  public static final String CTIME = "ctime";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public FriendGroup setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public FriendGroup setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public FriendGroup setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getCount()
  {
    return get(COUNT);
  }
  
  public FriendGroup setCount(Object value)
  {
    return set(COUNT, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public FriendGroup setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
