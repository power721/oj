package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class ProblemCategory extends Model<ProblemCategory>
{
  private static final long serialVersionUID = 1L;
  
  public static final ProblemCategory dao = new ProblemCategory();
  
  public static final String ID = "id";
  public static final String PID = "pid";
  public static final String CID = "cid";
  public static final String WEIGHT = "weight";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public ProblemCategory setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public ProblemCategory setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public ProblemCategory setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getWeight()
  {
    return getInt(WEIGHT);
  }
  
  public ProblemCategory setWeight(Integer value)
  {
    return set(WEIGHT, value);
  }
  
}
