package com.power.oj.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.jfinal.log.Logger;
import com.power.oj.core.service.SessionService;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class OjSessionListener implements SessionListener
{

  private static final Logger log = Logger.getLogger(OjSessionListener.class);
  
  @Override
  public void onExpiration(Session session)
  {
    UserModel userModel = UserService.me().getCurrentUser();
    log.info(userModel.toJson());
    SessionService.me().deleteSession(session);
    
    log.info(session.toString());
    log.info(session.getStartTimestamp().toString());
  }

  @Override
  public void onStart(Session session)
  {
    SessionService.me().saveSession(session);
    
    log.info(session.toString());
  }

  @Override
  public void onStop(Session session)
  {
    UserModel userModel = UserService.me().getCurrentUser();
    log.info(userModel.toJson());
    SessionService.me().deleteSession(session);

    log.info(session.toString());
    log.info(session.getStartTimestamp().toString());
  }

}
