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

  public <T> T getId()
  {
    return get(ID);
  }
  
  public ContestProblem setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getCid()
  {
    return get(CID);
  }
  
  public ContestProblem setCid(Object value)
  {
    return set(CID, value);
  }
  
  public <T> T getPid()
  {
    return get(PID);
  }
  
  public ContestProblem setPid(Object value)
  {
    return set(PID, value);
  }
  
  public <T> T getTitle()
  {
    return get(TITLE);
  }
  
  public ContestProblem setTitle(Object value)
  {
    return set(TITLE, value);
  }
  
  public <T> T getNum()
  {
    return get(NUM);
  }
  
  public ContestProblem setNum(Object value)
  {
    return set(NUM, value);
  }
  
  public <T> T getAccepted()
  {
    return get(ACCEPTED);
  }
  
  public ContestProblem setAccepted(Object value)
  {
    return set(ACCEPTED, value);
  }
  
  public <T> T getSubmission()
  {
    return get(SUBMISSION);
  }
  
  public ContestProblem setSubmission(Object value)
  {
    return set(SUBMISSION, value);
  }
  
  public <T> T getFirstBloodUid()
  {
    return get(FIRST_BLOOD_UID);
  }
  
  public ContestProblem setFirstBloodUid(Object value)
  {
    return set(FIRST_BLOOD_UID, value);
  }
  
  public <T> T getFirstBloodTime()
  {
    return get(FIRST_BLOOD_TIME);
  }
  
  public ContestProblem setFirstBloodTime(Object value)
  {
    return set(FIRST_BLOOD_TIME, value);
  }
  
}
