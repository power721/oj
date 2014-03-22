package com.power.oj.mail;

import com.power.oj.core.OjController;

public class MailController extends OjController
{
  public void index()
  {
    setTitle(getText("mail.index.title"));
  }
}
