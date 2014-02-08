package com.power.oj.mail;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;

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
    mail.set("mid", mid);
    mail.set("user", from);
    mail.set("peer", to);
    mail.save();
    
    mail = new MailModel();
    mail.set("mid", mid);
    mail.set("user", to);
    mail.set("peer", from);
    return mail.save();
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
  
  public boolean addMailBanlistItem(Integer uid, Integer banUid)
  {
    // TODO check if item exists
    Record record = new Record();
    record.set("uid", uid);
    record.set("ban_uid", banUid);
    record.set("ctime", OjConfig.timeStamp);
    
    return Db.save("mail_banlist", record);
  }

  public boolean deleteMailBanlistItem(Integer uid, Integer banUid)
  {
    // TODO check if item exists
    return Db.update("DELETE FROM mail_banlist WHERE uid=? AND ban_uid=?", uid, banUid) > 0;
  }
  
  public Page<MailModel> getUserMailBanlist(int pageNumber, int pageSize, Integer uid)
  {
    String sql = "SELECT  m.ban_uid AS uid,u.name AS uname";
    String from = "FROM mail_banlist m LEFT JOIN user u ON u.uid=m.ban_uid WHERE m.uid=?";
    
    return dao.paginate(pageNumber, pageSize, sql, from, uid);
  }
  
}
