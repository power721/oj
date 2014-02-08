package com.power.oj.mail;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserExtModel;
import com.power.oj.util.Tool;

public class MailService
{
  private static final MailService me = new MailService();
  private static final MailModel dao = MailModel.dao;
  
  private MailService() {}
  public static MailService me()
  {
    return me;
  }
  
  public boolean sendMail(Integer from, Integer to, String content)
  {
    MailContentModel mailContent = new MailContentModel();
    
    mailContent.set("from", from);
    mailContent.set("to", to);
    mailContent.set("content", content);
    mailContent.set("ctime", OjConfig.timeStamp);
    if(!mailContent.save())
    {
      return false;
    }
    
    Integer mid = mailContent.getInt("id");
    MailModel mail = new MailModel();
    mail.set("mid", mid).set("user", from).set("peer", to);
    mail.save();
    
    if (!isUserInMailBanlist(from, to))
    {
      mail = new MailModel();
      mail.set("mid", mid).set("user", to).set("peer", from);
      mail.save();
    }
    
    return true;
  }

  public int sendDrift(Integer from, String content)
  {
    UserExtModel userExtModel = UserExtModel.dao.findById(from);
    int timestamp = Tool.getDayTimestamp();
    int drift = userExtModel.getInt("send_drift");
    int last_drift = userExtModel.getInt("last_send_drift");
    
    if (last_drift + OjConstants.DAY_TIMESTAMP < timestamp)
      drift = 0;
    
    if (drift < 10)
    {
      MailContentModel mailContent = new MailContentModel();
    
      mailContent.set("from", from);
      mailContent.set("to", 0);
      mailContent.set("content", content);
      mailContent.set("ctime", OjConfig.timeStamp);
      mailContent.save();
      
      drift += 1;
      userExtModel.set("send_drift", drift).set("last_send_drift", OjConfig.timeStamp).update();
    }
    
    return drift;
  }

  public int getDrift(Integer uid)
  {
    UserExtModel userExtModel = UserExtModel.dao.findById(uid);
    int timestamp = Tool.getDayTimestamp();
    int drift = userExtModel.getInt("get_drift");
    int last_drift = userExtModel.getInt("last_get_drift");
    
    if (last_drift + OjConstants.DAY_TIMESTAMP < timestamp)
      drift = 0;
    
    if (drift < 5)
    {
      MailContentModel mailContent = MailContentModel.dao.findFirst("SELECT * FROM mail_content WHERE `to`=0 AND `from`!=? LIMIT 1", uid);
      
      if (mailContent != null)
      {
        MailModel mail = new MailModel();
        mail.set("mid", mailContent.get("id")).set("user", uid).set("peer", mailContent.get("from"));
        mail.save();
        
        mailContent.set("to", uid).update();
        drift += 1;
        userExtModel.set("get_drift", drift).set("last_get_drift", OjConfig.timeStamp).update();
        return drift;
      }
      return 0;
    }
    
    return -1;
  }
  
  public List<MailModel> findUserNewMails(Integer uid)
  {
    return dao.findUserNewMails(uid);
  }

  public Long countUserNewMails(Integer uid)
  {
    return dao.countUserNewMails(uid);
  }

  public Long countUserNewMails(Integer user, Integer peer)
  {
    return dao.countUserNewMails(user, peer);
  }
  
  public boolean hasNewMails(Integer user, Integer peer)
  {
    return dao.hasNewMails(user, peer);
  }

  public void resetUserNewMails(Integer user, Integer peer)
  {
    dao.resetUserNewMails(user, peer);
  }
  
  public Page<MailModel> getUserMailGroups(int pageNumber, int pageSize, Integer uid)
  {
    String sql = "SELECT m.*,mc.content,mc.ctime,CONCAT(m.user, '-', m.peer) AS p2p,u.name AS peeruser,u.avatar AS avatar";
    String from = "FROM (SELECT * FROM mail WHERE user=? AND status!=2 ORDER BY id DESC)m LEFT JOIN mail_content mc ON mc.id=m.mid LEFT JOIN user u ON u.uid=m.peer GROUP BY peer ORDER BY m.id DESC";
    
    return dao.paginate(pageNumber, pageSize, sql, from, uid);
  }

  public Page<MailModel> getMails(int pageNumber, int pageSize, Integer user, Integer peer)
  {
    resetUserNewMails(user, peer);
    
    String sql = "SELECT m.id,mc.from,mc.to,mc.content,mc.ctime,u1.name AS fromuser,u2.name AS touser";
    String from = "FROM mail m LEFT JOIN mail_content mc ON mc.id=m.mid LEFT JOIN user u1 ON u1.uid=mc.from LEFT JOIN user u2 ON u2.uid=mc.to WHERE user=? AND peer=? AND m.status!=2 ORDER BY id DESC";
    
    return dao.paginate(pageNumber, pageSize, sql, from, user, peer);
  }
  
  public int deleteMail(Integer uid, Integer id)
  {
    return dao.deleteMail(uid, id);
  }
  
  public int deleteMailGroup(Integer user, Integer peer)
  {
    return dao.deleteMailGroup(user, peer);
  }
  
  public int deleteUserAllMails(Integer uid)
  {
    return dao.deleteUserAllMails(uid);
  }
  
  public boolean addMailBanlistItem(Integer user, Integer banUser)
  {
    if (isUserInMailBanlist(banUser, user))
    {
      return true;
    }
    
    Record record = new Record();
    record.set("user", user);
    record.set("ban_user", banUser);
    record.set("ctime", OjConfig.timeStamp);
    
    return Db.save("mail_banlist", record);
  }

  public int deleteMailBanlistItem(Integer user, Integer banUser)
  {
    return Db.update("DELETE FROM mail_banlist WHERE user=? AND ban_user=?", user, banUser);
  }
  
  public boolean isUserInMailBanlist(Integer banUser, Integer user)
  {
    return Db.queryInt("SELECT id FROM mail_banlist WHERE user=? AND ban_user=?", user, banUser) != null;
  }
  
  public Page<MailModel> getUserMailBanlist(int pageNumber, int pageSize, Integer user)
  {
    String sql = "SELECT m.ban_user AS uid,u.name AS uname";
    String from = "FROM mail_banlist m LEFT JOIN user u ON u.uid=m.ban_user WHERE m.user=?";
    
    return dao.paginate(pageNumber, pageSize, sql, from, user);
  }
  
}
