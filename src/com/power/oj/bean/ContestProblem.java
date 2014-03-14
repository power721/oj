package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class ContestProblem extends Model<ContestProblem>
{
  private static final long serialVersionUID = 1L;
  
  public static final ContestProblem dao = new ContestProblem();
  
  public static final String ID = "id";
  public static final String CID = "cid";
  public static final String PID = "pid";
  public static final String TITLE = "title";
  public static final String NUM = "num";
  public static final String ACCEPTED = "accepted";
  public static final String SUBMISSION = "submission";
  public static final String FIRST_BLOOD_UID = "firstBloodUid";
  public static final String FIRST_BLOOD_TIME = "firstBloodTime";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public ContestProblem setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public ContestProblem setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public ContestProblem setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public String getTitle()
  {
    return getStr(TITLE);
  }
  
  public ContestProblem setTitle(String value)
  {
    return set(TITLE, value);
  }
  
  public Integer getNum()
  {
    return getInt(NUM);
  }
  
  public ContestProblem setNum(Integer value)
  {
    return set(NUM, value);
  }
  
  public Integer getAccepted()
  {
    return getInt(ACCEPTED);
  }
  
  public ContestProblem setAccepted(Integer value)
  {
    return set(ACCEPTED, value);
  }
  
  public Integer getSubmission()
  {
    return getInt(SUBMISSION);
  }
  
  public ContestProblem setSubmission(Integer value)
  {
    return set(SUBMISSION, value);
  }
  
  public Integer getFirstBloodUid()
  {
    return getInt(FIRST_BLOOD_UID);
  }
  
  public ContestProblem setFirstBloodUid(Integer value)
  {
    return set(FIRST_BLOOD_UID, value);
  }
  
  public Integer getFirstBloodTime()
  {
    return getInt(FIRST_BLOOD_TIME);
  }
  
  public ContestProblem setFirstBloodTime(Integer value)
  {
    return set(FIRST_BLOOD_TIME, value);
  }
  
}
