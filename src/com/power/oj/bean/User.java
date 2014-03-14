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

  public <T> T getUid()
  {
    return get(UID);
  }
  
  public User setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getTid()
  {
    return get(TID);
  }
  
  public User setTid(Object value)
  {
    return set(TID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public User setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getPassword()
  {
    return get(PASSWORD);
  }
  
  public User setPassword(Object value)
  {
    return set(PASSWORD, value);
  }
  
  public <T> T getNick()
  {
    return get(NICK);
  }
  
  public User setNick(Object value)
  {
    return set(NICK, value);
  }
  
  public <T> T getRealName()
  {
    return get(REAL_NAME);
  }
  
  public User setRealName(Object value)
  {
    return set(REAL_NAME, value);
  }
  
  public <T> T getRegEmail()
  {
    return get(REG_EMAIL);
  }
  
  public User setRegEmail(Object value)
  {
    return set(REG_EMAIL, value);
  }
  
  public <T> T getEmail()
  {
    return get(EMAIL);
  }
  
  public User setEmail(Object value)
  {
    return set(EMAIL, value);
  }
  
  public <T> T getEmailVerified()
  {
    return get(EMAIL_VERIFIED);
  }
  
  public User setEmailVerified(Object value)
  {
    return set(EMAIL_VERIFIED, value);
  }
  
  public <T> T getLanguage()
  {
    return get(LANGUAGE);
  }
  
  public User setLanguage(Object value)
  {
    return set(LANGUAGE, value);
  }
  
  public <T> T getSchool()
  {
    return get(SCHOOL);
  }
  
  public User setSchool(Object value)
  {
    return set(SCHOOL, value);
  }
  
  public <T> T getSolved()
  {
    return get(SOLVED);
  }
  
  public User setSolved(Object value)
  {
    return set(SOLVED, value);
  }
  
  public <T> T getAccepted()
  {
    return get(ACCEPTED);
  }
  
  public User setAccepted(Object value)
  {
    return set(ACCEPTED, value);
  }
  
  public <T> T getSubmission()
  {
    return get(SUBMISSION);
  }
  
  public User setSubmission(Object value)
  {
    return set(SUBMISSION, value);
  }
  
  public <T> T getAtime()
  {
    return get(ATIME);
  }
  
  public User setAtime(Object value)
  {
    return set(ATIME, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public User setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
  public <T> T getMtime()
  {
    return get(MTIME);
  }
  
  public User setMtime(Object value)
  {
    return set(MTIME, value);
  }
  
  public <T> T getLoginTime()
  {
    return get(LOGIN_TIME);
  }
  
  public User setLoginTime(Object value)
  {
    return set(LOGIN_TIME, value);
  }
  
  public <T> T getLoginIP()
  {
    return get(LOGIN_IP);
  }
  
  public User setLoginIP(Object value)
  {
    return set(LOGIN_IP, value);
  }
  
  public <T> T getPhone()
  {
    return get(PHONE);
  }
  
  public User setPhone(Object value)
  {
    return set(PHONE, value);
  }
  
  public <T> T getQq()
  {
    return get(QQ);
  }
  
  public User setQq(Object value)
  {
    return set(QQ, value);
  }
  
  public <T> T getBlog()
  {
    return get(BLOG);
  }
  
  public User setBlog(Object value)
  {
    return set(BLOG, value);
  }
  
  public <T> T getGender()
  {
    return get(GENDER);
  }
  
  public User setGender(Object value)
  {
    return set(GENDER, value);
  }
  
  public <T> T getComeFrom()
  {
    return get(COME_FROM);
  }
  
  public User setComeFrom(Object value)
  {
    return set(COME_FROM, value);
  }
  
  public <T> T getOnline()
  {
    return get(ONLINE);
  }
  
  public User setOnline(Object value)
  {
    return set(ONLINE, value);
  }
  
  public <T> T getAvatar()
  {
    return get(AVATAR);
  }
  
  public User setAvatar(Object value)
  {
    return set(AVATAR, value);
  }
  
  public <T> T getSignature()
  {
    return get(SIGNATURE);
  }
  
  public User setSignature(Object value)
  {
    return set(SIGNATURE, value);
  }
  
  public <T> T getShareCode()
  {
    return get(SHARE_CODE);
  }
  
  public User setShareCode(Object value)
  {
    return set(SHARE_CODE, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public User setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
  public <T> T getToken()
  {
    return get(TOKEN);
  }
  
  public User setToken(Object value)
  {
    return set(TOKEN, value);
  }
  
}
