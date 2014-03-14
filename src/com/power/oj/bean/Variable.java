package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Variable extends Model<Variable>
{
  private static final long serialVersionUID = 1L;
  
  public static final Variable dao = new Variable();
  
  public static final String ID = "id";
  public static final String CID = "cid";
  public static final String NAME = "name";
  public static final String STRING_VALUE = "stringValue";
  public static final String BOOLEAN_VALUE = "booleanValue";
  public static final String INT_VALUE = "intValue";
  public static final String TEXT_VALUE = "textValue";
  public static final String TYPE = "type";
  public static final String DESCRIPTION = "description";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Variable setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getCid()
  {
    return get(CID);
  }
  
  public Variable setCid(Object value)
  {
    return set(CID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public Variable setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getStringValue()
  {
    return get(STRING_VALUE);
  }
  
  public Variable setStringValue(Object value)
  {
    return set(STRING_VALUE, value);
  }
  
  public <T> T getBooleanValue()
  {
    return get(BOOLEAN_VALUE);
  }
  
  public Variable setBooleanValue(Object value)
  {
    return set(BOOLEAN_VALUE, value);
  }
  
  public <T> T getIntValue()
  {
    return get(INT_VALUE);
  }
  
  public Variable setIntValue(Object value)
  {
    return set(INT_VALUE, value);
  }
  
  public <T> T getTextValue()
  {
    return get(TEXT_VALUE);
  }
  
  public Variable setTextValue(Object value)
  {
    return set(TEXT_VALUE, value);
  }
  
  public <T> T getType()
  {
    return get(TYPE);
  }
  
  public Variable setType(Object value)
  {
    return set(TYPE, value);
  }
  
  public <T> T getDescription()
  {
    return get(DESCRIPTION);
  }
  
  public Variable setDescription(Object value)
  {
    return set(DESCRIPTION, value);
  }
  
}
