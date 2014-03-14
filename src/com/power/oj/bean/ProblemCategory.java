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

  public <T> T getId()
  {
    return get(ID);
  }
  
  public ProblemCategory setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getPid()
  {
    return get(PID);
  }
  
  public ProblemCategory setPid(Object value)
  {
    return set(PID, value);
  }
  
  public <T> T getCid()
  {
    return get(CID);
  }
  
  public ProblemCategory setCid(Object value)
  {
    return set(CID, value);
  }
  
  public <T> T getWeight()
  {
    return get(WEIGHT);
  }
  
  public ProblemCategory setWeight(Object value)
  {
    return set(WEIGHT, value);
  }
  
}
