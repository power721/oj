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

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Role setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public Role setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getDescription()
  {
    return get(DESCRIPTION);
  }
  
  public Role setDescription(Object value)
  {
    return set(DESCRIPTION, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Role setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
