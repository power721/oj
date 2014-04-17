package com.power.oj.core.bean;

import jodd.format.Printf;

public class ResultType
{
  public static final int AC = 0;
  public static final int PE = 1;
  public static final int TLE = 2;
  public static final int MLE = 3;
  public static final int WA = 4;
  public static final int RE = 5;
  public static final int OLE = 6;
  public static final int CE = 7;
  public static final int RF = 8;
  public static final int SE = 98;
  public static final int VE = 99;
  public static final int RUN = 100;
  public static final int WAIT = 10000;

  private int id;
  private String name;
  private String longName;

  public ResultType()
  {

  }

  public ResultType(int id, String name, String longName)
  {
    this.id = id;
    this.name = name;
    this.longName = longName;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getLongName()
  {
    return longName;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setLongName(String longName)
  {
    this.longName = longName;
  }
  
  public String toJson()
  {
    return Printf.str("{id:\"%d\", name:\"%s\", longName:\"%s\"}", id, name, longName);
  }
}
