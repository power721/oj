package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Team extends Model<Team>
{
  private static final long serialVersionUID = 1L;
  
  public static final Team dao = new Team();
  
  public static final String TID = "tid";
  public static final String UID = "uid";
  public static final String NAME1 = "name1";
  public static final String STU_ID1 = "stuId1";
  public static final String COLLEGE1 = "college1";
  public static final String CLASS1 = "class1";
  public static final String GENDER1 = "gender1";
  public static final String CONTACT1 = "contact1";
  public static final String NAME2 = "name2";
  public static final String STU_ID2 = "stuId2";
  public static final String COLLEGE2 = "college2";
  public static final String CLASS2 = "class2";
  public static final String GENDER2 = "gender2";
  public static final String CONTACT2 = "contact2";
  public static final String NAME3 = "name3";
  public static final String STU_ID3 = "stuId3";
  public static final String COLLEGE3 = "college3";
  public static final String CLASS3 = "class3";
  public static final String GENDER3 = "gender3";
  public static final String CONTACT3 = "contact3";
  public static final String COMMENT = "comment";
  public static final String YEAR = "year";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String HISTORY = "history";
  public static final String NOTICE = "notice";
  public static final String IS_GIRL_TEAM = "isGirlTeam";
  public static final String IS_ROOKIE_TEAM = "isRookieTeam";
  public static final String IS_SPECIAL_TEAM = "isSpecialTeam";
  public static final String STATUS = "status";

  public <T> T getTid()
  {
    return get(TID);
  }
  
  public Team setTid(Object value)
  {
    return set(TID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Team setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getName1()
  {
    return get(NAME1);
  }
  
  public Team setName1(Object value)
  {
    return set(NAME1, value);
  }
  
  public <T> T getStuId1()
  {
    return get(STU_ID1);
  }
  
  public Team setStuId1(Object value)
  {
    return set(STU_ID1, value);
  }
  
  public <T> T getCollege1()
  {
    return get(COLLEGE1);
  }
  
  public Team setCollege1(Object value)
  {
    return set(COLLEGE1, value);
  }
  
  public <T> T getClass1()
  {
    return get(CLASS1);
  }
  
  public Team setClass1(Object value)
  {
    return set(CLASS1, value);
  }
  
  public <T> T getGender1()
  {
    return get(GENDER1);
  }
  
  public Team setGender1(Object value)
  {
    return set(GENDER1, value);
  }
  
  public <T> T getContact1()
  {
    return get(CONTACT1);
  }
  
  public Team setContact1(Object value)
  {
    return set(CONTACT1, value);
  }
  
  public <T> T getName2()
  {
    return get(NAME2);
  }
  
  public Team setName2(Object value)
  {
    return set(NAME2, value);
  }
  
  public <T> T getStuId2()
  {
    return get(STU_ID2);
  }
  
  public Team setStuId2(Object value)
  {
    return set(STU_ID2, value);
  }
  
  public <T> T getCollege2()
  {
    return get(COLLEGE2);
  }
  
  public Team setCollege2(Object value)
  {
    return set(COLLEGE2, value);
  }
  
  public <T> T getClass2()
  {
    return get(CLASS2);
  }
  
  public Team setClass2(Object value)
  {
    return set(CLASS2, value);
  }
  
  public <T> T getGender2()
  {
    return get(GENDER2);
  }
  
  public Team setGender2(Object value)
  {
    return set(GENDER2, value);
  }
  
  public <T> T getContact2()
  {
    return get(CONTACT2);
  }
  
  public Team setContact2(Object value)
  {
    return set(CONTACT2, value);
  }
  
  public <T> T getName3()
  {
    return get(NAME3);
  }
  
  public Team setName3(Object value)
  {
    return set(NAME3, value);
  }
  
  public <T> T getStuId3()
  {
    return get(STU_ID3);
  }
  
  public Team setStuId3(Object value)
  {
    return set(STU_ID3, value);
  }
  
  public <T> T getCollege3()
  {
    return get(COLLEGE3);
  }
  
  public Team setCollege3(Object value)
  {
    return set(COLLEGE3, value);
  }
  
  public <T> T getClass3()
  {
    return get(CLASS3);
  }
  
  public Team setClass3(Object value)
  {
    return set(CLASS3, value);
  }
  
  public <T> T getGender3()
  {
    return get(GENDER3);
  }
  
  public Team setGender3(Object value)
  {
    return set(GENDER3, value);
  }
  
  public <T> T getContact3()
  {
    return get(CONTACT3);
  }
  
  public Team setContact3(Object value)
  {
    return set(CONTACT3, value);
  }
  
  public <T> T getComment()
  {
    return get(COMMENT);
  }
  
  public Team setComment(Object value)
  {
    return set(COMMENT, value);
  }
  
  public <T> T getYear()
  {
    return get(YEAR);
  }
  
  public Team setYear(Object value)
  {
    return set(YEAR, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Team setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
  public <T> T getMtime()
  {
    return get(MTIME);
  }
  
  public Team setMtime(Object value)
  {
    return set(MTIME, value);
  }
  
  public <T> T getHistory()
  {
    return get(HISTORY);
  }
  
  public Team setHistory(Object value)
  {
    return set(HISTORY, value);
  }
  
  public <T> T getNotice()
  {
    return get(NOTICE);
  }
  
  public Team setNotice(Object value)
  {
    return set(NOTICE, value);
  }
  
  public Boolean IsGirlTeam()
  {
    return getBoolean(IS_GIRL_TEAM);
  }
  
  public Team setGirlTeam(Boolean value)
  {
    return set(IS_GIRL_TEAM, value);
  }
  
  public Boolean IsRookieTeam()
  {
    return getBoolean(IS_ROOKIE_TEAM);
  }
  
  public Team setRookieTeam(Boolean value)
  {
    return set(IS_ROOKIE_TEAM, value);
  }
  
  public Boolean IsSpecialTeam()
  {
    return getBoolean(IS_SPECIAL_TEAM);
  }
  
  public Team setSpecialTeam(Boolean value)
  {
    return set(IS_SPECIAL_TEAM, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Team setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
