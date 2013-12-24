package com.power.oj.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import com.jfinal.log.Logger;
import com.power.oj.core.service.SessionService;

public class OjSessionListener implements SessionListener
{

  private static final Logger log = Logger.getLogger(OjSessionListener.class);
  
  @Override
  public void onExpiration(Session session)
  {
    SessionService.deleteSession(session);
    
    log.info(session.toString());
    log.info(session.getStartTimestamp().toString());
  }

  @Override
  public void onStart(Session session)
  {
    SessionService.saveSession(session);
    
    log.info(session.toString());
  }

  @Override
  public void onStop(Session session)
  {
    SessionService.deleteSession(session);

    log.info(session.toString());
    log.info(session.getStartTimestamp().toString());
  }

}
