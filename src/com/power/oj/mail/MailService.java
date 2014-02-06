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
  
  public boolean sendMail(Integer from, Integer to, Integer gid, String content)
  {
    if (gid == null)
    {
      gid = dao.getMailGroup(from, to);
      if (gid == null)
      {
        gid = addMailGroup(from, to);
      }
    }
    MailModel mail = new MailModel();
    mail.set("from", from);
    mail.set("to", to);
    mail.set("gid", gid);
    mail.set("content", content);
    mail.set("ctime", OjConfig.timeStamp);
    
    return mail.save();
  }

  public Integer addMailGroup(Integer from, Integer to)
  {
    Integer id = Db.queryInt("SELECT max(id) FROM mail_group");
    if (id == null)
    {
      id = 0;
    }
    
    Record mailGroup = new Record();
    mailGroup.set("id", id + 1);
    mailGroup.set("from", from);
    mailGroup.set("to", to);
    mailGroup.set("ctime", OjConfig.timeStamp);
    Db.save("mail_group", mailGroup);
    
    mailGroup = new Record();
    mailGroup.set("id", id + 1);
    mailGroup.set("from", to);
    mailGroup.set("to", from);
    mailGroup.set("ctime", OjConfig.timeStamp);
    Db.save("mail_group", mailGroup);
    
    return id + 1;
  }
  
  public MailModel findMail(Integer id)
  {
    return dao.findById(id);
  }

  public MailModel findMail(Integer id, Integer uid)
  {
    return dao.findMail(id, uid);
  }
  
  public List<MailModel> findUserMails(Integer uid)
  {
    return dao.findUserMails(uid);
  }

  public List<MailModel> findUserReceivedMails(Integer uid)
  {
    return dao.findUserReceivedMails(uid);
  }
  
  public List<MailModel> findUserSentMails(Integer uid)
  {
    return dao.findUserSentMails(uid);
  }
  
  public List<MailModel> findUserNewMails(Integer uid)
  {
    return dao.findUserNewMails(uid);
  }

  public Long countUserNewMails(Integer uid)
  {
    return dao.countUserNewMails(uid);
  }
  
  public Page<MailModel> getUserMailGroups(int pageNumber, int pageSize, Integer uid)
  {
    String sql = "SELECT m.*,FROM_UNIXTIME(m.ctime, '%Y-%m-%d %H:%i:%s') AS ctime,CONCAT(m.from, '-', m.to) AS p2p,"
        + "u.name AS fromuser,u.avatar AS avatar";
    String from = "FROM mail_group m LEFT JOIN user u ON u.uid=m.from WHERE `to`=? ORDER BY id DESC";
    
    return dao.paginate(pageNumber, pageSize, sql, from, uid);
  }
  
  public Page<MailModel> getMailByGid(int pageNumber, int pageSize, Integer gid)
  {
    String sql = "SELECT m.*,FROM_UNIXTIME(m.ctime, '%Y-%m-%d %H:%i:%s') AS ctime,u1.name AS fromuser,u2.name AS touser";
    String from = "FROM mail m LEFT JOIN user u1 ON u1.uid=m.from LEFT JOIN user u2 ON u2.uid=m.to WHERE gid=? ORDER BY id DESC";
    
    return dao.paginate(pageNumber, pageSize, sql, from, gid);
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
