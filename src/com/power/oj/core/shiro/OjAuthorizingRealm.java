package com.power.oj.core.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.user.UserModel;

public class OjAuthorizingRealm extends AuthorizingRealm
{

  private static final Logger log = Logger.getLogger(OjAuthorizingRealm.class);
  
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection printcipals)
  {
    // TODO Auto-generated method stub
    UserModel userModel = (UserModel) printcipals.fromRealm(getName()).iterator().next();

    if (userModel != null)
    {
      String sql = "SELECT p.name AS permission FROM permission p WHERE p.id IN " +
      "(SELECT rp.pid FROM role_permission rp " + 
      "LEFT JOIN roles r ON rp.rid = r.id " + 
      "LEFT JOIN user_role ur ON ur.rid = r.id " + 
      "WHERE ur.uid = ?)";
      List<Record> permissionList = Db.find(sql, userModel.getUid());

      if (permissionList != null && permissionList.size() > 0)
      {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> pers = new HashSet<String>();
        for (Record record : permissionList)
        {
          pers.add(record.getStr("permission"));
          log.info(record.getStr("permission"));
        }
        info.setStringPermissions(pers);
        return info;
      }
    }

    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException
  {
    UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    UserModel userModel = UserModel.dao.getUserByName(token.getUsername());

    if (userModel != null)
    {
      return new SimpleAuthenticationInfo(userModel, userModel.getStr("pass"), getName());
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
