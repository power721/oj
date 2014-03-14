package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class UserExt extends Model<UserExt>
{
  private static final long serialVersionUID = 1L;
  
  public static final UserExt dao = new UserExt();
  
  public static final String UID = "uid";
  public static final String TID = "tid";
  public static final String REAL_NAME = "realName";
  public static final String PHONE = "phone";
  public static final String QQ = "qq";
  public static final String BLOG = "blog";
  public static final String SHARE_CODE = "shareCode";
  public static final String ONLINE = "online";
  public static final String LEVEL = "level";
  public static final String CREDIT = "credit";
  public static final String EXPERIENCE = "experience";
  public static final String CHECKIN = "checkin";
  public static final String CHECKIN_TIMES = "checkinTimes";
  public static final String TOTAL_CHECKIN = "totalCheckin";
  public static final String LAST_SEND_DRIFT = "lastSendDrift";
  public static final String SEND_DRIFT_NUM = "sendDriftNum";
  public static final String LAST_GET_DRIFT = "lastGetDrift";
  public static final String GET_DRIFT_NUM = "getDriftNum";

  public <T> T getUid()
  {
    return get(UID);
  }
  
  public UserExt setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getTid()
  {
    return get(TID);
  }
  
  public UserExt setTid(Object value)
  {
    return set(TID, value);
  }
  
  public <T> T getRealName()
  {
    return get(REAL_NAME);
  }
  
  public UserExt setRealName(Object value)
  {
    return set(REAL_NAME, value);
  }
  
  public <T> T getPhone()
  {
    return get(PHONE);
  }
  
  public UserExt setPhone(Object value)
  {
    return set(PHONE, value);
  }
  
  public <T> T getQq()
  {
    return get(QQ);
  }
  
  public UserExt setQq(Object value)
  {
    return set(QQ, value);
  }
  
  public <T> T getBlog()
  {
    return get(BLOG);
  }
  
  public UserExt setBlog(Object value)
  {
    return set(BLOG, value);
  }
  
  public <T> T getShareCode()
  {
    return get(SHARE_CODE);
  }
  
  public UserExt setShareCode(Object value)
  {
    return set(SHARE_CODE, value);
  }
  
  public <T> T getOnline()
  {
    return get(ONLINE);
  }
  
  public UserExt setOnline(Object value)
  {
    return set(ONLINE, value);
  }
  
  public <T> T getLevel()
  {
    return get(LEVEL);
  }
  
  public UserExt setLevel(Object value)
  {
    return set(LEVEL, value);
  }
  
  public <T> T getCredit()
  {
    return get(CREDIT);
  }
  
  public UserExt setCredit(Object value)
  {
    return set(CREDIT, value);
  }
  
  public <T> T getExperience()
  {
    return get(EXPERIENCE);
  }
  
  public UserExt setExperience(Object value)
  {
    return set(EXPERIENCE, value);
  }
  
  public <T> T getCheckin()
  {
    return get(CHECKIN);
  }
  
  public UserExt setCheckin(Object value)
  {
    return set(CHECKIN, value);
  }
  
  public <T> T getCheckinTimes()
  {
    return get(CHECKIN_TIMES);
  }
  
  public UserExt setCheckinTimes(Object value)
  {
    return set(CHECKIN_TIMES, value);
  }
  
  public <T> T getTotalCheckin()
  {
    return get(TOTAL_CHECKIN);
  }
  
  public UserExt setTotalCheckin(Object value)
  {
    return set(TOTAL_CHECKIN, value);
  }
  
  public <T> T getLastSendDrift()
  {
    return get(LAST_SEND_DRIFT);
  }
  
  public UserExt setLastSendDrift(Object value)
  {
    return set(LAST_SEND_DRIFT, value);
  }
  
  public <T> T getSendDriftNum()
  {
    return get(SEND_DRIFT_NUM);
  }
  
  public UserExt setSendDriftNum(Object value)
  {
    return set(SEND_DRIFT_NUM, value);
  }
  
  public <T> T getLastGetDrift()
  {
    return get(LAST_GET_DRIFT);
  }
  
  public UserExt setLastGetDrift(Object value)
  {
    return set(LAST_GET_DRIFT, value);
  }
  
  public <T> T getGetDriftNum()
  {
    return get(GET_DRIFT_NUM);
  }
  
  public UserExt setGetDriftNum(Object value)
  {
    return set(GET_DRIFT_NUM, value);
  }
  
}
