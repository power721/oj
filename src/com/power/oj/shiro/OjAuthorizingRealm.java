package com.power.oj.shiro;

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
      String sql = "SELECT r.name AS role, r.id AS rid FROM roles r LEFT JOIN user_role ur ON ur.rid = r.id WHERE ur.uid = ?";
      List<Record> roleList = Db.find(sql, userModel.getUid());
      SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
      Set<String> roles = new HashSet<String>();
      Set<String> pers = new HashSet<String>();

      if (roleList != null && roleList.size() > 0)
      {
        log.info(userModel.toString());
        
        for (Record record : roleList)
        {
          roles.add(record.getStr("role"));
          log.info("role: " + record.getStr("role"));
          
          sql = "SELECT p.name AS permission FROM permission p LEFT JOIN role_permission rp ON rp.pid = p.id WHERE rp.rid = ?";
          List<Record> permissionList = Db.find(sql, record.getInt("rid"));

          for (Record permissionRecord : permissionList)
          {
            pers.add(permissionRecord.getStr("permission"));
            log.info("permission: " + permissionRecord.getStr("permission"));
          }
        }
        info.setRoles(roles);
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
