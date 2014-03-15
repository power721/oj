package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class RolePermission extends Model<RolePermission>
{
  private static final long serialVersionUID = 1L;
  
  public static final RolePermission dao = new RolePermission();
  
  public static final String TABLE_NAME = "role_permission";
  public static final String ID = "id";
  public static final String RID = "rid";
  public static final String PID = "pid";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public RolePermission setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getRid()
  {
    return getInt(RID);
  }
  
  public RolePermission setRid(Integer value)
  {
    return set(RID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public RolePermission setPid(Integer value)
  {
    return set(PID, value);
  }
  
}
