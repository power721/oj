package com.power.oj.core.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.power.oj.user.UserModel;

public class OjAuthorizingRealm extends AuthorizingRealm
{

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection printcipals)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
  {
    UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    UserModel userModel = UserModel.dao.getUserByName(token.getUsername());
    
    if (userModel != null)
    {
      return new SimpleAuthenticationInfo(userModel.getUid(), userModel.getStr("pass"), getName());
    }

    return null;
  }

  /**
   * 更新用户授权信息缓存.
   */
  public void clearCachedAuthorizationInfo(String principal)
  {
    SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
    clearCachedAuthorizationInfo(principals);
  }

  /**
   * 清除所有用户授权信息缓存.
   */
  public void clearAllCachedAuthorizationInfo()
  {
    Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
    if (cache != null)
    {
      for (Object key : cache.keys())
      {
        cache.remove(key);
      }
    }
  }

}
