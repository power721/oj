package com.power.oj.user;

import java.util.ArrayList;
import java.util.List;

import jodd.util.BCrypt;
import jodd.util.StringUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.service.SessionService;
import com.power.oj.shiro.ShiroKit;

public class UserService
{
  private static final Logger log = Logger.getLogger(UserService.class);

  private static final UserService me = new UserService();
  private static final UserModel dao = UserModel.dao;
  
  private UserService()
  {
    
  }
  
  public static UserService me()
  {
    return me;
  }

  /**
   * User login with name and password.
   * @param name user name.
   * @param password user password.
   * @param rememberMe ture if remember user.
   * @return true if login successfully, otherwise false.
   */
  public boolean login(String name, String password, boolean rememberMe)
  {
    Subject currentUser = getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(name, password);
    token.setRememberMe(rememberMe);

    try
    {
      currentUser.login(token);

      updateLogin(name, true);
      SessionService.me().updateLogin();
    } catch (AuthenticationException ae)
    {
      updateLogin(name, false);
      log.warn("User signin failed.");
      return false;
    }

    return true;
  }

  /**
   * User logout in Shiro session.
   */
  public void logout()
  {
    Subject currentUser = getSubject();
    /*UserModel userModel = getCurrentUser();
    if (userModel != null)
    {
      userModel.update();
    }*/

    currentUser.logout();
  }
  
  /**
   * Update user login time and loginlog.
   * @param name user name.
   * @param success true if user login sucessfully.
   * @return
   */
  public boolean updateLogin(String name, boolean success)
  {
    Record loginLog = new Record();
    if (success)
    {
      UserModel userModel = getCurrentUser();
      userModel.updateLogin();
      loginLog.set("uid", userModel.getUid());
    }
    
    loginLog.set("name", name).set("ip", SessionService.me().getHost()).set("ctime", OjConfig.timeStamp).set("succeed", success);
    return Db.save("loginlog", loginLog);
  }
  
  /**
   * Reset user password for recover account.
   * @param name user name.
   * @param password new password.
   * @return
   */
  public boolean resetPassword(String name, String password)
  {
    UserModel userModel = dao.getUserByName(name);
    
    userModel.set("token", null).set("pass", BCrypt.hashpw(password, BCrypt.gensalt()));
    return userModel.update();
  }
  
  /**
   * Check if the token is valid for reset password.
   * @param name user name.
   * @param token reset token.
   * @return true if the token is valid.
   */
  public boolean checkResetToken(String name, String token)
  {
    UserModel userModel = dao.getUserByName(name);
    
    if (userModel != null && token != null && token.equals(userModel.getStr("token")))
    {
      if (OjConfig.timeStamp - userModel.getInt("mtime") <= OjConstants.resetPasswordExpiresTime)
      {
        return true;
      }
      else
      {
        userModel.set("token", null).update();
      }
    }
    
    return false;
  }

  /**
   * Search user by key word in scope.
   * @param scope "all", "name", "nick", "school", "email".
   * @param word key word.
   * @return the list of users.
   */
  public List<UserModel> searchUser(String scope, String word)
  {
    List<UserModel> userList = null;
    List<Object> paras = new ArrayList<Object>();

    if (StringUtil.isNotBlank(word))
    {
      word = new StringBuilder(3).append("%").append(word).append("%").toString();
      StringBuilder sb = new StringBuilder("SELECT uid,name,nick,school,solved,submit FROM user WHERE (");
      
      if (StringUtil.isNotBlank(scope))
      {
        String scopes[] =
        { "name", "nick", "school", "email" };
        if (StringUtil.equalsOneIgnoreCase(scope, scopes) == -1)
          return null;
        sb.append(scope).append(" LIKE ? ");
        paras.add(word);
      } else
      {
        sb.append("name LIKE ? OR nick LIKE ? OR school LIKE ? OR email LIKE ?");
        for (int i = 0; i < 4; ++i)
          paras.add(word);
      }
      sb.append(") AND status=1 ORDER BY solved desc,submit,uid");
      
      userList = dao.find(sb.toString(), paras.toArray());
    }
    
    return userList;
  }

  /**
   * Get current user by uid.
   * @return current user or null.
   */
  public UserModel getCurrentUser()
  {
    return dao.findById(getCurrentUid());
  }

  /**
   * Get current uid form Shiro.
   * @return the uid of current user or null.
   */
  public Integer getCurrentUid()
  {
    Subject currentUser = getSubject();
    if (currentUser == null)
      return null;

    Object principal = currentUser.getPrincipal();
    if (principal == null)
      return null;
    
    return (Integer) principal;
  }

  /**
   * Build user statistics.
   * @param userModel the user.
   * @return
   */
  public boolean build(UserModel userModel)
  {
    int uid = userModel.getUid();
    
    Record record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE uid=? LIMIT 1", uid);

    if (record != null)
    {
      userModel.set("submit", record.getLong("count"));
    }

    record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE uid=? AND result=0 LIMIT 1", uid);
    if (record != null)
    {
      userModel.set("accept", record.getLong("count"));
    }

    record = Db.findFirst("SELECT COUNT(pid) AS count FROM solution WHERE uid=? AND result=0 LIMIT 1", uid);
    if (record != null)
    {
      userModel.set("solved", record.getLong("count"));
    }

    return userModel.update();
  }

  /*
   * Methods for Shiro.
   */
  public Subject getSubject()
  {
    return SecurityUtils.getSubject();
  }

  public boolean isAuthenticated()
  {
    return ShiroKit.isAuthenticated();
  }
  
  public boolean isRemembered()
  {
    return ShiroKit.isRemembered();
  }
  
  public boolean isUser()
  {
    return ShiroKit.isUser();
  }

  public boolean isGuest()
  {
    return ShiroKit.isGuest();
  }
  
  public boolean isAdmin()
  {
    return ShiroKit.hasPermission("admin");
  }

  public boolean hasRole(String roleName)
  {
    return ShiroKit.hasRole(roleName);
  }
  
  public boolean lacksRole(String roleName)
  {
    return ShiroKit.lacksRole(roleName);
  }
  
  public boolean hasAnyRoles(String roleNames)
  {
    return ShiroKit.hasAnyRoles(roleNames);
  }
  
  public boolean hasAllRoles(String roleNames)
  {
    return ShiroKit.hasAllRoles(roleNames);
  }
  
  public boolean hasPermission(String permission)
  {
    return ShiroKit.hasPermission(permission);
  }
  
  public boolean lacksPermission(String permission)
  {
    return ShiroKit.lacksPermission(permission);
  }
  
}
