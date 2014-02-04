package com.power.oj.mail;

import java.util.List;

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
  
}
