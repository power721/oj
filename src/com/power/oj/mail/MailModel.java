package com.power.oj.mail;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class MailModel extends Model<MailModel>
{

  /**
   * 
   */
  private static final long serialVersionUID = 854677482560407471L;
  
  public static final MailModel dao = new MailModel();
  public static final String ID = "id";
  public static final String GID = "gid";
  public static final String FROM = "from";
  public static final String TO = "to";
  public static final String TITLE = "title";
  public static final String CONTENT = "content";
  public static final String REPLY = "reply";
  public static final String READ = "read";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";

  public MailModel findMail(Integer id, Integer uid)
  {
    return findFirst("SELECT * FROM mail WHERE id=? AND (`from`=? OR `to`=?) LIMIT 1", id, uid, uid);
  }
  
  public List<MailModel> findUserMails(Integer uid)
  {
    return find("SELECT * FROM mail WHERE `from`=? OR `to`=?", uid, uid);
  }

  public List<MailModel> findUserReceivedMails(Integer uid)
  {
    return find("SELECT * FROM mail WHERE `to`=?", uid);
  }
  
  public List<MailModel> findUserSentMails(Integer uid)
  {
    return find("SELECT * FROM mail WHERE `from`=?", uid);
  }

  public List<MailModel> findUserNewMails(Integer uid)
  {
    return find("SELECT * FROM mail WHERE `to`=? AND `read`=0", uid);
  }

  public Long countUserNewMails(Integer uid)
  {
    return findFirst("SELECT COUNT(*) AS count FROM mail WHERE `to`=? AND `read`=0", uid).getLong("count");
  }
  
  public Integer getMaxId()
  {
    return Db.queryInt("SELECT max(id) FROM mail");
  }
  
  public Integer getMailGroup(Integer from, Integer to)
  {
    return Db.queryInt("SELECT id FROM mail_group WHERE `from`=? AND `to`=?", from, to);
  }
  
}
