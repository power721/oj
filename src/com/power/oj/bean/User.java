package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User>
{
  private static final long serialVersionUID = 1L;
  
  public static final User dao = new User();
  
  public static final String UID = "uid";
  public static final String TID = "tid";
  public static final String NAME = "name";
  public static final String PASSWORD = "password";
  public static final String NICK = "nick";
  public static final String REAL_NAME = "realName";
  public static final String REG_EMAIL = "regEmail";
  public static final String EMAIL = "email";
  public static final String EMAIL_VERIFIED = "emailVerified";
  public static final String LANGUAGE = "language";
  public static final String SCHOOL = "school";
  public static final String SOLVED = "solved";
  public static final String ACCEPTED = "accepted";
  public static final String SUBMISSION = "submission";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String LOGIN_TIME = "loginTime";
  public static final String LOGIN_IP = "loginIP";
  public static final String PHONE = "phone";
  public static final String QQ = "qq";
  public static final String BLOG = "blog";
  public static final String GENDER = "gender";
  public static final String COME_FROM = "comeFrom";
  public static final String ONLINE = "online";
  public static final String AVATAR = "avatar";
  public static final String SIGNATURE = "signature";
  public static final String SHARE_CODE = "shareCode";
  public static final String STATUS = "status";
  public static final String TOKEN = "token";

  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public User setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getTid()
  {
    return getInt(TID);
  }
  
  public User setTid(Integer value)
  {
    return set(TID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public User setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getPassword()
  {
    return getStr(PASSWORD);
  }
  
  public User setPassword(String value)
  {
    return set(PASSWORD, value);
  }
  
  public String getNick()
  {
    return getStr(NICK);
  }
  
  public User setNick(String value)
  {
    return set(NICK, value);
  }
  
  public String getRealName()
  {
    return getStr(REAL_NAME);
  }
  
  public User setRealName(String value)
  {
    return set(REAL_NAME, value);
  }
  
  public String getRegEmail()
  {
    return getStr(REG_EMAIL);
  }
  
  public User setRegEmail(String value)
  {
    return set(REG_EMAIL, value);
  }
  
  public String getEmail()
  {
    return getStr(EMAIL);
  }
  
  public User setEmail(String value)
  {
    return set(EMAIL, value);
  }
  
  public Boolean getEmailVerified()
  {
    return getBoolean(EMAIL_VERIFIED);
  }
  
  public User setEmailVerified(Boolean value)
  {
    return set(EMAIL_VERIFIED, value);
  }
  
  public Integer getLanguage()
  {
    return getInt(LANGUAGE);
  }
  
  public User setLanguage(Integer value)
  {
    return set(LANGUAGE, value);
  }
  
  public String getSchool()
  {
    return getStr(SCHOOL);
  }
  
  public User setSchool(String value)
  {
    return set(SCHOOL, value);
  }
  
  public Integer getSolved()
  {
    return getInt(SOLVED);
  }
  
  public User setSolved(Integer value)
  {
    return set(SOLVED, value);
  }
  
  public Integer getAccepted()
  {
    return getInt(ACCEPTED);
  }
  
  public User setAccepted(Integer value)
  {
    return set(ACCEPTED, value);
  }
  
  public Integer getSubmission()
  {
    return getInt(SUBMISSION);
  }
  
  public User setSubmission(Integer value)
  {
    return set(SUBMISSION, value);
  }
  
  public Integer getAtime()
  {
    return getInt(ATIME);
  }
  
  public User setAtime(Integer value)
  {
    return set(ATIME, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public User setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public User setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public Integer getLoginTime()
  {
    return getInt(LOGIN_TIME);
  }
  
  public User setLoginTime(Integer value)
  {
    return set(LOGIN_TIME, value);
  }
  
  public String getLoginIP()
  {
    return getStr(LOGIN_IP);
  }
  
  public User setLoginIP(String value)
  {
    return set(LOGIN_IP, value);
  }
  
  public String getPhone()
  {
    return getStr(PHONE);
  }
  
  public User setPhone(String value)
  {
    return set(PHONE, value);
  }
  
  public String getQq()
  {
    return getStr(QQ);
  }
  
  public User setQq(String value)
  {
    return set(QQ, value);
  }
  
  public String getBlog()
  {
    return getStr(BLOG);
  }
  
  public User setBlog(String value)
  {
    return set(BLOG, value);
  }
  
  public String getGender()
  {
    return getStr(GENDER);
  }
  
  public User setGender(String value)
  {
    return set(GENDER, value);
  }
  
  public String getComeFrom()
  {
    return getStr(COME_FROM);
  }
  
  public User setComeFrom(String value)
  {
    return set(COME_FROM, value);
  }
  
  public Integer getOnline()
  {
    return getInt(ONLINE);
  }
  
  public User setOnline(Integer value)
  {
    return set(ONLINE, value);
  }
  
  public String getAvatar()
  {
    return getStr(AVATAR);
  }
  
  public User setAvatar(String value)
  {
    return set(AVATAR, value);
  }
  
  public String getSignature()
  {
    return getStr(SIGNATURE);
  }
  
  public User setSignature(String value)
  {
    return set(SIGNATURE, value);
  }
  
  public Boolean getShareCode()
  {
    return getBoolean(SHARE_CODE);
  }
  
  public User setShareCode(Boolean value)
  {
    return set(SHARE_CODE, value);
  }
  
  public Integer getStatus()
  {
    return getInt(STATUS);
  }
  
  public User setStatus(Integer value)
  {
    return set(STATUS, value);
  }
  
  public String getToken()
  {
    return getStr(TOKEN);
  }
  
  public User setToken(String value)
  {
    return set(TOKEN, value);
  }
  
}
