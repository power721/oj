package com.power.oj.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.subject.PrincipalCollection;

import com.jfinal.log.Logger;
import com.power.oj.core.service.SessionService;

public class OjSessionListener implements SessionListener, AuthenticationListener
{

  private static final Logger log = Logger.getLogger(OjSessionListener.class);
  
  @Override
  public void onExpiration(Session session)
  {
    SessionService.me().deleteSession(session);
    
    log.info(session.toString());
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
    SessionService.me().deleteSession(session);
    
    log.info(session.toString());
  }

  @Override
  public void onSuccess(AuthenticationToken token, AuthenticationInfo info)
  {
    Integer uid = (Integer) info.getPrincipals().getPrimaryPrincipal();
    
    log.info(uid.toString());
    log.info(token.getPrincipal().toString());
  }

  @Override
  public void onFailure(AuthenticationToken token, AuthenticationException ae)
  {
     log.info(token.toString());
     log.info(ae.toString());
  }

  @Override
  public void onLogout(PrincipalCollection principals)
  {
    log.info(principals.toString());
  }

}
