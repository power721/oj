package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class RolePermission extends Model<RolePermission>
{
  private static final long serialVersionUID = 1L;
  
  public static final RolePermission dao = new RolePermission();
  
  public static final String ID = "id";
  public static final String RID = "rid";
  public static final String PID = "pid";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public RolePermission setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getRid()
  {
    return get(RID);
  }
  
  public RolePermission setRid(Object value)
  {
    return set(RID, value);
  }
  
  public <T> T getPid()
  {
    return get(PID);
  }
  
  public RolePermission setPid(Object value)
  {
    return set(PID, value);
  }
  
}
