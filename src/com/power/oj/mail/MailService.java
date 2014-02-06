package com.power.oj.mail;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;

public class MailService
{
  private static final MailService me = new MailService();
  private static final MailModel dao = MailModel.dao;
  
  private MailService() {}
  public static MailService me()
  {
    return me;
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
    String sql = "SELECT *,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";
    String from = "FROM mail WHERE (`from`=? OR `to`=?) GROUP BY group_id";
    
    return dao.paginate(pageNumber, pageSize, sql, from, uid, uid);
  }
  
}
