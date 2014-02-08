package com.power.oj.user;

import com.jfinal.plugin.activerecord.Model;

public class UserExtModel extends Model<UserExtModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 6064662146178988559L;
  
  public static final UserExtModel dao = new UserExtModel();
  public static final String UID = "uid";
  public static final String TID = "tid";
  public static final String REALNAME = "realname";
  public static final String PHONE = "phone";
  public static final String QQ = "qq";
  public static final String BLOG = "blog";
  public static final String ONLINE = "online";
  public static final String LEVEL = "level";
  public static final String CREDIT = "credit";
  public static final String SHARE = "share";
  public static final String CHECKIN = "checkin";
  public static final String CHECKIN_TIMES = "checkin_times";
  public static final String LAST_DRIFT = "last_drift";
  public static final String DRIFT = "drift";
}
