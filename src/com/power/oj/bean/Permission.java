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

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Permission setId(Integer value)
  {
    return set(ID, value);
  }
  
  public String getModule()
  {
    return getStr(MODULE);
  }
  
  public Permission setModule(String value)
  {
    return set(MODULE, value);
  }
  
  public Integer getType()
  {
    return getInt(TYPE);
  }
  
  public Permission setType(Integer value)
  {
    return set(TYPE, value);
  }
  
  public Integer getParentID()
  {
    return getInt(PARENT_ID);
  }
  
  public Permission setParentID(Integer value)
  {
    return set(PARENT_ID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public Permission setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getTitle()
  {
    return getStr(TITLE);
  }
  
  public Permission setTitle(String value)
  {
    return set(TITLE, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public Permission setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
