package com.power.oj.mail;

import com.jfinal.plugin.activerecord.Model;

public class MailContentModel extends Model<MailContentModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 3375076623779739301L;
  
  public static final MailContentModel dao = new MailContentModel();
  public static final String ID = "id";
  public static final String FROM = "from";
  public static final String TO = "to";
  public static final String CONTENT = "content";
  public static final String CTIME = "ctime";
}
