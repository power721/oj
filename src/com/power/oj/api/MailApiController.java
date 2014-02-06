package com.power.oj.api;

import com.power.oj.core.OjController;
import com.power.oj.mail.MailService;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;

public class MailApiController extends OjController
{
  private static final UserService userService = UserService.me();
  private static final MailService mailService = MailService.me();
  
  public void unRead()
  {
    if (ShiroKit.isGuest())
    {
      renderJson("{\"success\":false, \"result\":\"User does not login.\"}");
      return;
    }
    
    Integer uid = userService.getCurrentUid();
    long unReadMail = mailService.countUserNewMails(uid);
    
    renderJson("{\"success\":true, \"unReadMail\":" + unReadMail +"}");
  }
  
}
