package com.power.oj.social;

import com.jfinal.plugin.activerecord.Model;

public class FriendModel extends Model<FriendModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 1123725577114663566L;
  
  public static final String ID = "id";
  public static final String GID = "gid";
  public static final String USER = "user";
  public static final String FRIEND = "friend";
  public static final String CTIME = "ctime";
  public static final FriendModel dao = new FriendModel();
}
