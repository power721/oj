package com.power.oj.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;
import com.power.oj.core.service.SessionService;

public class UserService
{
  private static final Logger log = Logger.getLogger(UserService.class);
  private static final String NAMES_DELIMETER = ",";

  // private static final UserService userService = new UserService();
  // private UserModel dao = UserModel.dao;

  public static boolean login(String name, String password, boolean rememberMe)
  {
    Subject currentUser = getCurrentUser();
    // Session session = currentUser.getSession();
    UsernamePasswordToken token = new UsernamePasswordToken(name, password);
    token.setRememberMe(rememberMe);

    try
    {
      currentUser.login(token);

      // UserModel userModel = getPrincipal();

      // int uid = userModel.getUid();
      // if (userModel.isAdmin(uid))
      // session.setAttribute(OjConstants.ADMIN_USER, uid);

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
    return getCurrentUser() != null && getCurrentUser().isAuthenticated();
  }

  public static boolean isUser()
  {
    return getCurrentUser() != null && getCurrentUser().getPrincipal() != null;
  }

  public static boolean isGuest()
  {
    return !isUser();
  }

  public static boolean hasRole(String roleName)
  {
    return getCurrentUser() != null && roleName != null && roleName.length() > 0 && getCurrentUser().hasRole(roleName);
  }

  public static boolean hasAnyRoles(String roleNames)
  {
    boolean hasAnyRole = false;
    Subject subject = getCurrentUser();
    if (subject != null && roleNames != null && roleNames.length() > 0)
    {
      // Iterate through roles and check to see if the user has one of the
      // roles
      for (String role : roleNames.split(NAMES_DELIMETER))
      {
        if (subject.hasRole(role.trim()))
        {
          hasAnyRole = true;
          break;
        }
      }
    }
    return hasAnyRole;
  }

}
