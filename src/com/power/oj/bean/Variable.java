package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Variable extends Model<Variable>
{
  private static final long serialVersionUID = 1L;
  
  public static final Variable dao = new Variable();
  
  public static final String TABLE_NAME = "variable";
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String STRING_VALUE = "stringValue";
  public static final String BOOLEAN_VALUE = "booleanValue";
  public static final String INT_VALUE = "intValue";
  public static final String TEXT_VALUE = "textValue";
  public static final String TYPE = "type";
  public static final String DESCRIPTION = "description";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Variable setId(Integer value)
  {
    return set(ID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public Variable setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getStringValue()
  {
    return getStr(STRING_VALUE);
  }
  
  public Variable setStringValue(String value)
  {
    return set(STRING_VALUE, value);
  }
  
  public Boolean getBooleanValue()
  {
    return getBoolean(BOOLEAN_VALUE);
  }
  
  public Variable setBooleanValue(Boolean value)
  {
    return set(BOOLEAN_VALUE, value);
  }
  
  public Integer getIntValue()
  {
    return getInt(INT_VALUE);
  }
  
  public Variable setIntValue(Integer value)
  {
    return set(INT_VALUE, value);
  }
  
  public String getTextValue()
  {
    return getStr(TEXT_VALUE);
  }
  
  public Variable setTextValue(String value)
  {
    return set(TEXT_VALUE, value);
  }
  
  public String getType()
  {
    return getStr(TYPE);
  }
  
  public Variable setType(String value)
  {
    return set(TYPE, value);
  }
  
  public String getDescription()
  {
    return getStr(DESCRIPTION);
  }
  
  public Variable setDescription(String value)
  {
    return set(DESCRIPTION, value);
  }
  
}
