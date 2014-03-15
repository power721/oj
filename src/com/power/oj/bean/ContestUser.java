package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class ContestUser extends Model<ContestUser>
{
  private static final long serialVersionUID = 1L;
  
  public static final ContestUser dao = new ContestUser();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String CID = "cid";
  public static final String CTIME = "ctime";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public ContestUser setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public ContestUser setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public ContestUser setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public ContestUser setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
