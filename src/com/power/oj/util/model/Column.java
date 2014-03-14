package com.power.oj.util.model;

public class Column
{
  private String name;
  private String field;
  private String type;
  
  public Column(String name)
  {
    this.name = name;
    this.field = convertInv(name);
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
  
  private String convertInv(String name)
  {
    boolean flag = false;
    StringBuilder sb = new StringBuilder();
    
    for (int i=0; i<name.length(); ++i)
    {
      char c = name.charAt(i);
      String tmp = String.valueOf(c);
      if (Character.isLowerCase(c))
      {
        flag = true;
      }
      else
      {
        if (flag && Character.isUpperCase(c))
        {
          sb.append("_");
        }
        flag = false;
      }
      
      sb.append(tmp);
    }
    return sb.toString();
  }
}
