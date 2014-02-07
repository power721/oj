package com.power.oj.mail;

import com.power.oj.core.OjController;

public class MailController extends OjController
{
  //private static final UserService userService = UserService.me();
  //private static final MailService mailService = MailService.me();
  
  public void index()
  {
    setTitle(getText("mail.index.title"));
  }
  
}
