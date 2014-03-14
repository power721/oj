package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Category extends Model<Category>
{
  private static final long serialVersionUID = 1L;
  
  public static final Category dao = new Category();
  
  public static final String ID = "id";
  public static final String PARENT = "parent";
  public static final String NAME = "name";
  public static final String ZH = "zh";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Category setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getParent()
  {
    return get(PARENT);
  }
  
  public Category setParent(Object value)
  {
    return set(PARENT, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public Category setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getZh()
  {
    return get(ZH);
  }
  
  public Category setZh(Object value)
  {
    return set(ZH, value);
  }
  
}
