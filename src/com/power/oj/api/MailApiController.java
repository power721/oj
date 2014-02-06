package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.mail.MailModel;
import com.power.oj.mail.MailService;
import com.power.oj.user.UserService;

@Before(GuestInterceptor.class)
public class MailApiController extends OjController
{
  private static final UserService userService = UserService.me();
  private static final MailService mailService = MailService.me();
  
  public void unRead()
  {
    Integer uid = userService.getCurrentUid();
    long unReadMail = mailService.countUserNewMails(uid);
    
    renderJson("{\"success\":true, \"unReadMail\":" + unReadMail +"}");
  }
  
  public void getGroups()
  {
    int pageNumber = getParaToInt("page", 1);
    int pageSize = getParaToInt("size", OjConfig.mailPageSize);
    Integer uid = userService.getCurrentUid();
    
    Page<MailModel> mailList = mailService.getUserMailGroups(pageNumber, pageSize, uid);
    
    renderJson(mailList);
  }
  
  public void getMails()
  {
    int pageNumber = getParaToInt("page", 1);
    int pageSize = getParaToInt("size", OjConfig.mailPageSize);
    Integer gid = getParaToInt("gid");

    Page<MailModel> mailList = mailService.getMailByGid(pageNumber, pageSize, gid);
    
    renderJson(mailList);
  }
  
  public void isHaveUnreaded()
  {
    Integer gid = getParaToInt("gid");
    
  }
  
  public void newMail()
  {
    Integer from = userService.getCurrentUid();
    Integer to = getParaToInt("userId");
    Integer gid = getParaToInt("gid");
    String content = getPara("content");
    
    if (mailService.sendMail(from, to, gid, content))
    {
      renderJson("{\"success\":true, \"status\":200,\"result\":\"\"}");
    }
    else
    {
      renderJson("{\"success\":false, \"status\":500,\"result\":\"Save new mail failed.\"}");
    }
  }
  
}
