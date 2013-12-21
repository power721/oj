package com.power.oj.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.jfinal.ext.plugin.shiro.ShiroMethod;
import com.jfinal.log.Logger;
import com.power.oj.core.service.SessionService;

public class UserService
{
  private static final Logger log = Logger.getLogger(UserService.class);

  // private static final UserService userService = new UserService();
  // private UserModel dao = UserModel.dao;

  public static boolean login(String name, String password, boolean rememberMe)
  {
    Subject currentUser = getCurrentUser();
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
    Subject currentUser = getCurrentUser();
    UserModel userModel = getPrincipal();
    if (userModel != null)
    {
      userModel.set("token", null);
      userModel.update();
    }

    currentUser.logout();
  }

  public static Subject getCurrentUser()
  {
    return SecurityUtils.getSubject();
  }

  public static UserModel getPrincipal()
  {
    Subject currentUser = getCurrentUser();
    if (currentUser == null)
      return null;

    Object principal = currentUser.getPrincipal();
    if (principal instanceof UserModel)
    {
      return (UserModel) principal;
    }

    if (principal != null)
      log.warn(principal.toString());

    return null;
  }

  public static boolean isAuthenticated()
  {
    return ShiroMethod.authenticated();
  }

  public static boolean isUser()
  {
    return ShiroMethod.user();
  }

  public static boolean isGuest()
  {
    return ShiroMethod.guest();
  }

  public static boolean hasRole(String roleName)
  {
    return ShiroMethod.hasRole(roleName);
  }
  
  public static boolean lacksRole(String roleName)
  {
    return ShiroMethod.lacksRole(roleName);
  }
  
  public static boolean hasAnyRoles(String roleNames)
  {
    return ShiroMethod.hasAnyRoles(roleNames);
  }
  
  public static boolean hasAllRoles(String roleNames)
  {
    return ShiroMethod.hasAllRoles(roleNames);
  }
  
  public static boolean hasPermission(String permission)
  {
    return ShiroMethod.hasPermission(permission);
  }
  
  public static boolean lacksPermission(String permission)
  {
    return ShiroMethod.lacksPermission(permission);
  }
  
}
