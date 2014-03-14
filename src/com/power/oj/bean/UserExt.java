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

  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public UserExt setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getTid()
  {
    return getInt(TID);
  }
  
  public UserExt setTid(Integer value)
  {
    return set(TID, value);
  }
  
  public String getRealName()
  {
    return getStr(REAL_NAME);
  }
  
  public UserExt setRealName(String value)
  {
    return set(REAL_NAME, value);
  }
  
  public String getPhone()
  {
    return getStr(PHONE);
  }
  
  public UserExt setPhone(String value)
  {
    return set(PHONE, value);
  }
  
  public String getQq()
  {
    return getStr(QQ);
  }
  
  public UserExt setQq(String value)
  {
    return set(QQ, value);
  }
  
  public String getBlog()
  {
    return getStr(BLOG);
  }
  
  public UserExt setBlog(String value)
  {
    return set(BLOG, value);
  }
  
  public Boolean getShareCode()
  {
    return getBoolean(SHARE_CODE);
  }
  
  public UserExt setShareCode(Boolean value)
  {
    return set(SHARE_CODE, value);
  }
  
  public Integer getOnline()
  {
    return getInt(ONLINE);
  }
  
  public UserExt setOnline(Integer value)
  {
    return set(ONLINE, value);
  }
  
  public Integer getLevel()
  {
    return getInt(LEVEL);
  }
  
  public UserExt setLevel(Integer value)
  {
    return set(LEVEL, value);
  }
  
  public Integer getCredit()
  {
    return getInt(CREDIT);
  }
  
  public UserExt setCredit(Integer value)
  {
    return set(CREDIT, value);
  }
  
  public Integer getExperience()
  {
    return getInt(EXPERIENCE);
  }
  
  public UserExt setExperience(Integer value)
  {
    return set(EXPERIENCE, value);
  }
  
  public Integer getCheckin()
  {
    return getInt(CHECKIN);
  }
  
  public UserExt setCheckin(Integer value)
  {
    return set(CHECKIN, value);
  }
  
  public Integer getCheckinTimes()
  {
    return getInt(CHECKIN_TIMES);
  }
  
  public UserExt setCheckinTimes(Integer value)
  {
    return set(CHECKIN_TIMES, value);
  }
  
  public Integer getTotalCheckin()
  {
    return getInt(TOTAL_CHECKIN);
  }
  
  public UserExt setTotalCheckin(Integer value)
  {
    return set(TOTAL_CHECKIN, value);
  }
  
  public Integer getLastSendDrift()
  {
    return getInt(LAST_SEND_DRIFT);
  }
  
  public UserExt setLastSendDrift(Integer value)
  {
    return set(LAST_SEND_DRIFT, value);
  }
  
  public Integer getSendDriftNum()
  {
    return getInt(SEND_DRIFT_NUM);
  }
  
  public UserExt setSendDriftNum(Integer value)
  {
    return set(SEND_DRIFT_NUM, value);
  }
  
  public Integer getLastGetDrift()
  {
    return getInt(LAST_GET_DRIFT);
  }
  
  public UserExt setLastGetDrift(Integer value)
  {
    return set(LAST_GET_DRIFT, value);
  }
  
  public Integer getGetDriftNum()
  {
    return getInt(GET_DRIFT_NUM);
  }
  
  public UserExt setGetDriftNum(Integer value)
  {
    return set(GET_DRIFT_NUM, value);
  }
  
}
