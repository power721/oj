package com.power.oj.util.model;

import java.util.ArrayList;
import java.util.List;

public class TableModel
{
  private boolean model = false;
  private String packageName;
  private String tableName;
  private String modelName;
  private ArrayList<Column> columns;

  public boolean isModel()
  {
    return model;
  }

  public void setModel(boolean model)
  {
    this.model = model;
  }

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
    StringBuilder sb = new StringBuilder();
    sb.append(convert(tableName));
    if (model)
      sb.append("Model");
    modelName = sb.toString();
    System.out.println("Table Name: " + tableName + "  Model Name: " + modelName);
  }

  public ArrayList<Column> getColumns()
  {
    return columns;
  }
  
  public void setColumns(List<String> columnsNames)
  {
    columns = new ArrayList<Column>();
    for (String name : columnsNames)
    {
      columns.add(new Column(name));
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
}
