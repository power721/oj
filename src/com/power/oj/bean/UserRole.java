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

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public UserRole setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public UserRole setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getRid()
  {
    return getInt(RID);
  }
  
  public UserRole setRid(Integer value)
  {
    return set(RID, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public UserRole setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
