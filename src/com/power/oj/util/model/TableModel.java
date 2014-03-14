package com.power.oj.util.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableModel
{
  private String packageName;
  private String tableName;
  private String modelName;
  private HashMap<String, String> columnsNames;
  private ArrayList<Column> columns;

  public String getPackageName()
  {
    return packageName;
  }

  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }

  public String getTableName()
  {
    return tableName;
  }

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

  public String getModelName()
  {
    return modelName;
  }

  public void setModelName(String tableName)
  {
    modelName = new StringBuilder().append(convert(tableName)).append("Model").toString();
    System.out.println("Table Name: " + modelName);
  }

  public HashMap<String, String> getColumnsNames()
  {
    return columnsNames;
  }
  
  public ArrayList<Column> getColumns()
  {
    return columns;
  }

  public void setColumnsNames(List<String> columnsNames)
  {
    this.columnsNames = new HashMap<String, String>();
    columns = new ArrayList<Column>();
    for (String name : columnsNames)
    {
      this.columnsNames.put(convertInv(name), name);
      Column column = new Column(name);
      columns.add(column);
    }
  }
  
  private String convert(String name)
  {
    boolean flag = true;
    StringBuilder sb = new StringBuilder();
    
    for (int i=0; i<name.length(); ++i)
    {
      char c = name.charAt(i);
      String tmp = String.valueOf(c);
      if (flag)
      {
        tmp = tmp.toUpperCase();
        flag = false;
      }
      
      if (c == '_')
      {
        flag = true;
      }
      else
      {
        sb.append(tmp);
      }
    }
    return sb.toString();
  }
  
  public static String convertInv(String name)
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
