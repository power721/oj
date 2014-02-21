package com.power.oj.api.oauth;

import com.jfinal.plugin.activerecord.Model;

public class WebLoginModel extends Model<WebLoginModel>
{

  /**
   * 
   */
  private static final long serialVersionUID = 7518045243609913294L;

  public static final WebLoginModel dao = new WebLoginModel();

  public static final String TABLE_NAME = "wwb_login";
  public static final String ID = "id";
  public static final String OPENID = "open_id";
  public static final String UID = "UID";
  public static final String CTIME = "ctime";
  public static final String NICK = "nick";
  public static final String AVATAR = "avatar";
  public static final String TYPE = "type";
  public static final String STATUS = "status";

  public WebLoginModel findByOpenID(String openid, String type)
  {
    String sql = "SELECT wb.* FROM web_login wb WHERE wb.open_id = ? AND wb.type = ? limit 1";
    return dao.findFirst(sql, openid, type);
  }
}
