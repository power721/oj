package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class ContestClarify extends Model<ContestClarify>
{
  private static final long serialVersionUID = 1L;
  
  public static final ContestClarify dao = new ContestClarify();
  
  public static final String TABLE_NAME = "contest_clarify";
  public static final String ID = "id";
  public static final String CID = "cid";
  public static final String UID = "uid";
  public static final String ADMIN = "admin";
  public static final String QUESTION = "question";
  public static final String REPLY = "reply";
  public static final String PUBLIC = "public";
  public static final String CTIME = "ctime";
  public static final String ATIME = "atime";
  public static final String MTIME = "mtime";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public ContestClarify setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public ContestClarify setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public ContestClarify setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getAdmin()
  {
    return getInt(ADMIN);
  }
  
  public ContestClarify setAdmin(Integer value)
  {
    return set(ADMIN, value);
  }
  
  public String getQuestion()
  {
    return getStr(QUESTION);
  }
  
  public ContestClarify setQuestion(String value)
  {
    return set(QUESTION, value);
  }
  
  public String getReply()
  {
    return getStr(REPLY);
  }
  
  public ContestClarify setReply(String value)
  {
    return set(REPLY, value);
  }
  
  public Boolean getPublic()
  {
    return getBoolean(PUBLIC);
  }
  
  public ContestClarify setPublic(Boolean value)
  {
    return set(PUBLIC, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public ContestClarify setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getAtime()
  {
    return getInt(ATIME);
  }
  
  public ContestClarify setAtime(Integer value)
  {
    return set(ATIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public ContestClarify setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
}
