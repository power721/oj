package com.power.oj.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.jfinal.log.Logger;
import com.power.oj.core.service.SessionService;
import com.power.oj.shiro.ShiroKit;

public class UserService
{
  private static final Logger log = Logger.getLogger(UserService.class);

  // private static final UserService userService = new UserService();
  private static final UserModel dao = UserModel.dao;

  public static boolean login(String name, String password, boolean rememberMe)
  {
    Subject currentUser = getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(name, password);
    token.setRememberMe(rememberMe);

    try
    {
      currentUser.login(token);

      SessionService.updateLogin();
    } catch (AuthenticationException ae)
    {
      log.warn("User signin failed.");
      return false;
    }

    return true;
  }

  public static void logout()
  {
    Subject currentUser = getSubject();
    UserModel userModel = getPrincipal();
    if (userModel != null)
    {
      userModel.set("token", null);
      userModel.update();
    }

    currentUser.logout();
  }

  public static Subject getSubject()
  {
    return SecurityUtils.getSubject();
  }

  public static UserModel getPrincipal()
  {
    Subject currentUser = getSubject();
    if (currentUser == null)
      return null;

    Object principal = currentUser.getPrincipal();
    if (principal == null)
      return null;

    return dao.findById(principal);
  }

  public static boolean isAuthenticated()
  {
    return ShiroKit.isAuthenticated();
  }
  
  public static boolean isRemembered()
  {
    return ShiroKit.isRemembered();
  }
  
  public static boolean isUser()
  {
    return ShiroKit.isUser();
  }

  public static boolean isGuest()
  {
    return ShiroKit.isGuest();
  }

  public static boolean hasRole(String roleName)
  {
    return ShiroKit.hasRole(roleName);
  }
  
  public static boolean lacksRole(String roleName)
  {
    return ShiroKit.lacksRole(roleName);
  }
  
  public static boolean hasAnyRoles(String roleNames)
  {
    return ShiroKit.hasAnyRoles(roleNames);
  }
  
  public static boolean hasAllRoles(String roleNames)
  {
    return ShiroKit.hasAllRoles(roleNames);
  }
  
  public static boolean hasPermission(String permission)
  {
    return ShiroKit.hasPermission(permission);
  }
  
  public static boolean lacksPermission(String permission)
  {
    return ShiroKit.lacksPermission(permission);
  }
  
}
