package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Loginlog extends Model<Loginlog>
{
  private static final long serialVersionUID = 1L;
  
  public static final Loginlog dao = new Loginlog();
  
  public static final String TABLE_NAME = "loginlog";
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String IP = "ip";
  public static final String CTIME = "ctime";
  public static final String SUCCESS = "success";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Loginlog setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Loginlog setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public Loginlog setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getIp()
  {
    return getStr(IP);
  }
  
  public Loginlog setIp(String value)
  {
    return set(IP, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Loginlog setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Boolean getSuccess()
  {
    return getBoolean(SUCCESS);
  }
  
  public Loginlog setSuccess(Boolean value)
  {
    return set(SUCCESS, value);
  }
  
}
