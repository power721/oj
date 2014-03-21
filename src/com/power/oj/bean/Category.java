package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Category extends Model<Category>
{
  private static final long serialVersionUID = 1L;
  
  public static final Category dao = new Category();
  
  public static final String TABLE_NAME = "category";
  public static final String ID = "id";
  public static final String PARENT = "parent";
  public static final String NAME = "name";
  public static final String ZH = "zh";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Category setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getParent()
  {
    return getInt(PARENT);
  }
  
  public Category setParent(Integer value)
  {
    return set(PARENT, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public Category setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getZh()
  {
    return getStr(ZH);
  }
  
  public Category setZh(String value)
  {
    return set(ZH, value);
  }
  
}
