package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class UserRole extends Model<UserRole>
{
  private static final long serialVersionUID = 1L;
  
  public static final UserRole dao = new UserRole();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String RID = "rid";
  public static final String STATUS = "status";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public UserRole setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public UserRole setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getRid()
  {
    return get(RID);
  }
  
  public UserRole setRid(Object value)
  {
    return set(RID, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public UserRole setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
