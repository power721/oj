package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Permission extends Model<Permission>
{
  private static final long serialVersionUID = 1L;
  
  public static final Permission dao = new Permission();
  
  public static final String ID = "id";
  public static final String MODULE = "module";
  public static final String TYPE = "type";
  public static final String PARENT_ID = "parentID";
  public static final String NAME = "name";
  public static final String TITLE = "title";
  public static final String STATUS = "status";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Permission setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getModule()
  {
    return get(MODULE);
  }
  
  public Permission setModule(Object value)
  {
    return set(MODULE, value);
  }
  
  public <T> T getType()
  {
    return get(TYPE);
  }
  
  public Permission setType(Object value)
  {
    return set(TYPE, value);
  }
  
  public <T> T getParentID()
  {
    return get(PARENT_ID);
  }
  
  public Permission setParentID(Object value)
  {
    return set(PARENT_ID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public Permission setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getTitle()
  {
    return get(TITLE);
  }
  
  public Permission setTitle(Object value)
  {
    return set(TITLE, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Permission setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
