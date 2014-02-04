package com.power.oj.mail;

import com.power.oj.core.OjController;
import com.power.oj.user.UserService;

public class MailController extends OjController
{
  private static final UserService userService = UserService.me();
  private static final MailService mailService = MailService.me();
  
  public void index()
  {
    setTitle(getText("mail.index.title"));
  }
  
  public void show()
  {
    Integer id = getParaToInt(0);
    Integer uid = userService.getCurrentUid();
    MailModel mailModel = mailService.findMail(id, uid);
    
    setAttr("mail", mailModel);
    
    setTitle(getText("mail.show.title"));
  }
  
}
