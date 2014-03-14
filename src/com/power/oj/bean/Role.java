package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Role extends Model<Role>
{
  private static final long serialVersionUID = 1L;
  
  public static final Role dao = new Role();
  
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String DESCRIPTION = "description";
  public static final String STATUS = "status";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Role setId(Integer value)
  {
    return set(ID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public Role setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getDescription()
  {
    return getStr(DESCRIPTION);
  }
  
  public Role setDescription(String value)
  {
    return set(DESCRIPTION, value);
  }
  
  public Integer getStatus()
  {
    return getInt(STATUS);
  }
  
  public Role setStatus(Integer value)
  {
    return set(STATUS, value);
  }
  
}
