package com.power.oj.social;

import com.jfinal.plugin.activerecord.Model;

public class FriendGroupModel extends Model<FriendGroupModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = -3134937650560828519L;
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String CTIME = "ctime";
  public static final FriendGroupModel dao = new FriendGroupModel();
}
