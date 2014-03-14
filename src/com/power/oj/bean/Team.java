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

  public Integer getTid()
  {
    return getInt(TID);
  }
  
  public Team setTid(Integer value)
  {
    return set(TID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Team setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getName1()
  {
    return getStr(NAME1);
  }
  
  public Team setName1(String value)
  {
    return set(NAME1, value);
  }
  
  public String getStuId1()
  {
    return getStr(STU_ID1);
  }
  
  public Team setStuId1(String value)
  {
    return set(STU_ID1, value);
  }
  
  public String getCollege1()
  {
    return getStr(COLLEGE1);
  }
  
  public Team setCollege1(String value)
  {
    return set(COLLEGE1, value);
  }
  
  public String getClass1()
  {
    return getStr(CLASS1);
  }
  
  public Team setClass1(String value)
  {
    return set(CLASS1, value);
  }
  
  public String getGender1()
  {
    return getStr(GENDER1);
  }
  
  public Team setGender1(String value)
  {
    return set(GENDER1, value);
  }
  
  public String getContact1()
  {
    return getStr(CONTACT1);
  }
  
  public Team setContact1(String value)
  {
    return set(CONTACT1, value);
  }
  
  public String getName2()
  {
    return getStr(NAME2);
  }
  
  public Team setName2(String value)
  {
    return set(NAME2, value);
  }
  
  public String getStuId2()
  {
    return getStr(STU_ID2);
  }
  
  public Team setStuId2(String value)
  {
    return set(STU_ID2, value);
  }
  
  public String getCollege2()
  {
    return getStr(COLLEGE2);
  }
  
  public Team setCollege2(String value)
  {
    return set(COLLEGE2, value);
  }
  
  public String getClass2()
  {
    return getStr(CLASS2);
  }
  
  public Team setClass2(String value)
  {
    return set(CLASS2, value);
  }
  
  public String getGender2()
  {
    return getStr(GENDER2);
  }
  
  public Team setGender2(String value)
  {
    return set(GENDER2, value);
  }
  
  public String getContact2()
  {
    return getStr(CONTACT2);
  }
  
  public Team setContact2(String value)
  {
    return set(CONTACT2, value);
  }
  
  public String getName3()
  {
    return getStr(NAME3);
  }
  
  public Team setName3(String value)
  {
    return set(NAME3, value);
  }
  
  public String getStuId3()
  {
    return getStr(STU_ID3);
  }
  
  public Team setStuId3(String value)
  {
    return set(STU_ID3, value);
  }
  
  public String getCollege3()
  {
    return getStr(COLLEGE3);
  }
  
  public Team setCollege3(String value)
  {
    return set(COLLEGE3, value);
  }
  
  public String getClass3()
  {
    return getStr(CLASS3);
  }
  
  public Team setClass3(String value)
  {
    return set(CLASS3, value);
  }
  
  public String getGender3()
  {
    return getStr(GENDER3);
  }
  
  public Team setGender3(String value)
  {
    return set(GENDER3, value);
  }
  
  public String getContact3()
  {
    return getStr(CONTACT3);
  }
  
  public Team setContact3(String value)
  {
    return set(CONTACT3, value);
  }
  
  public String getComment()
  {
    return getStr(COMMENT);
  }
  
  public Team setComment(String value)
  {
    return set(COMMENT, value);
  }
  
  public Integer getYear()
  {
    return getInt(YEAR);
  }
  
  public Team setYear(Integer value)
  {
    return set(YEAR, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Team setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public Team setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public String getHistory()
  {
    return getStr(HISTORY);
  }
  
  public Team setHistory(String value)
  {
    return set(HISTORY, value);
  }
  
  public Boolean getNotice()
  {
    return getBoolean(NOTICE);
  }
  
  public Team setNotice(Boolean value)
  {
    return set(NOTICE, value);
  }
  
  public Boolean isGirlTeam()
  {
    return getBoolean(IS_GIRL_TEAM);
  }
  
  public Team setGirlTeam(Boolean value)
  {
    return set(IS_GIRL_TEAM, value);
  }
  
  public Boolean isRookieTeam()
  {
    return getBoolean(IS_ROOKIE_TEAM);
  }
  
  public Team setRookieTeam(Boolean value)
  {
    return set(IS_ROOKIE_TEAM, value);
  }
  
  public Boolean isSpecialTeam()
  {
    return getBoolean(IS_SPECIAL_TEAM);
  }
  
  public Team setSpecialTeam(Boolean value)
  {
    return set(IS_SPECIAL_TEAM, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public Team setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
