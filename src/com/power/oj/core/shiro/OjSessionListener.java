package com.power.oj.core.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.jfinal.log.Logger;

public class OjSessionListener implements SessionListener
{

  private static final Logger log = Logger.getLogger(OjSessionListener.class);
      
  @Override
  public void onExpiration(Session session)
  {
    // TODO Auto-generated method stub
    log.info(session.toString());
  }

  @Override
  public void onStart(Session session)
  {
    // TODO Auto-generated method stub
    log.info(session.toString());
  }

  @Override
  public void onStop(Session session)
  {
    // TODO Auto-generated method stub
    log.info(session.toString());
  }

}
