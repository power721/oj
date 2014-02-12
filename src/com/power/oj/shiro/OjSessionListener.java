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
import com.power.oj.user.UserService;

public class OjSessionListener implements SessionListener, AuthenticationListener
{

  private static final Logger log = Logger.getLogger(OjSessionListener.class);
  private static final UserService userService = UserService.me();
  
  @Override
  public void onExpiration(Session session)
  {
    SessionService.me().deleteSession(session);
    
    log.info(session.toString());
  }

  @Override
  public void onStart(Session session)
  {
    // new guest
    SessionService.me().saveSession(session);
    userService.addGuest();
    
    log.info(session.toString());
  }

  @Override
  public void onStop(Session session)
  {
    SessionService.me().deleteSession(session);
    userService.removeGuest();

    log.info(session.toString());
  }

  @Override
  public void onSuccess(AuthenticationToken token, AuthenticationInfo info)
  {
    Integer uid = (Integer) info.getPrincipals().getPrimaryPrincipal();
    
    userService.addOnlineUser(uid, token.getPrincipal().toString());
    userService.removeGuest();
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
    Integer uid = (Integer) principals.getPrimaryPrincipal();
    userService.removeOnlineUser(uid);
    
    log.info(principals.toString());
  }

}
