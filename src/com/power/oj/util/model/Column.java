package com.power.oj.util.model;

public class Column
{
  private String name;
  private String field;
  private String type;
  
  public Column(String name)
  {
    this.field = TableModel.convertInv(name);
    this.name = name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return name;
  }

  public void setField(String field)
  {
    this.field = field;
  }
  
  public String getField()
  {
    return field;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}
