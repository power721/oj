package com.power.oj.shiro;

import jodd.util.BCrypt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;

public class OjHashedCredentialsMatcher extends HashedCredentialsMatcher
{
  private static final Logger log = Logger.getLogger(OjHashedCredentialsMatcher.class);
      
  @Override
  public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info)
  {
    if (authenticationToken instanceof UsernamePasswordToken)
    {
      UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
      String password = new String(token.getPassword());
      String hashedPassword = info.getCredentials().toString();
      if (OjConfig.getDevMode())
      {
        log.info(authenticationToken.toString());
        log.info(info.toString());
        log.info(password);
        log.info(hashedPassword);
      }
      
      if (hashedPassword.equals(password))
      {
        return true;
      }
      return BCrypt.checkpw(password, hashedPassword);
    }

    return false;
  }
}
