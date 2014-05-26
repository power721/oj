package com.power.oj.user;

import com.jfinal.plugin.activerecord.Model;

public class RoleModel extends Model<RoleModel>
{
  private static final long serialVersionUID = 1L;
  
  public static final RoleModel dao = new RoleModel();
  
  public static final String TABLE_NAME = "role";
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String DESCRIPTION = "description";
  public static final String STATUS = "status";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public RoleModel setId(Integer value)
  {
    return set(ID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public RoleModel setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getDescription()
  {
    return getStr(DESCRIPTION);
  }
  
  public RoleModel setDescription(String value)
  {
    return set(DESCRIPTION, value);
  }
  
  public Integer getStatus()
  {
    return getInt(STATUS);
  }
  
  public RoleModel setStatus(Integer value)
  {
    return set(STATUS, value);
  }
  
}
