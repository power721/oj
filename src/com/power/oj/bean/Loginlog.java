package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Loginlog extends Model<Loginlog>
{
  private static final long serialVersionUID = 1L;
  
  public static final Loginlog dao = new Loginlog();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String IP = "ip";
  public static final String INFO = "info";
  public static final String SUCCESS = "success";
  public static final String CTIME = "ctime";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Loginlog setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Loginlog setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public Loginlog setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getIp()
  {
    return get(IP);
  }
  
  public Loginlog setIp(Object value)
  {
    return set(IP, value);
  }
  
  public <T> T getInfo()
  {
    return get(INFO);
  }
  
  public Loginlog setInfo(Object value)
  {
    return set(INFO, value);
  }
  
  public <T> T getSuccess()
  {
    return get(SUCCESS);
  }
  
  public Loginlog setSuccess(Object value)
  {
    return set(SUCCESS, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Loginlog setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
