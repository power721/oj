package com.power.oj.shiro;

import jodd.util.BCrypt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class OjHashedCredentialsMatcher extends HashedCredentialsMatcher
{
  @Override
  public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info)
  {
    if (authenticationToken instanceof UsernamePasswordToken)
    {
      UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
      String password = new String(token.getPassword());
      String hashedPassword = info.getCredentials().toString();
      
      if (password == hashedPassword)
      {
        return true;
      }
      return BCrypt.checkpw(password, hashedPassword);
    }

    return false;
  }
}
