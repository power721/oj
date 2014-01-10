package com.power.oj.user;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import jodd.io.FileUtil;
import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.service.SessionService;
import com.power.oj.image.ImageScaleImpl;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.util.FileKit;

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
  
  public boolean signup(UserModel userModel)
  {
    String name = HtmlEncoder.text(userModel.getStr("name"));
    String password = BCrypt.hashpw(userModel.getStr("pass"), BCrypt.gensalt());
    String email = userModel.getStr("email");
    
    long ctime = OjConfig.timeStamp;
    UserModel newUser = new UserModel();
    newUser.set("name", name).set("pass", password).set("email", email).set("ctime", ctime);
    //newUser.set("atime", ctime).set("mtime", ctime);
    
    if (newUser.save())
    {
      //int uid = userModel.getUid();
     
      //Db.update("INSERT INTO user_role (rid,uid) SELECT id,? FROM roles WHERE name='user'", uid);
      
      password = userModel.getStr("pass");
      return login(name, password, false);
    }
    
    return false;
  }
  
  public boolean updateUser(UserModel userModel)
  {
    UserModel newUser = new UserModel();
    String password = userModel.getStr("pass");
    
    if (StringUtil.isNotBlank(password))
    {
      password = BCrypt.hashpw(password, BCrypt.gensalt());
      newUser.set("pass", password);
    }
    
    newUser.set("uid", getCurrentUid());
    newUser.set("nick", HtmlEncoder.text(userModel.getStr("nick")));
    newUser.set("school", HtmlEncoder.text(userModel.getStr("school")));
    newUser.set("realname", HtmlEncoder.text(userModel.getStr("realname")));
    newUser.set("blog", HtmlEncoder.text(userModel.getStr("blog")));
    newUser.set("email", HtmlEncoder.text(userModel.getStr("email")));
    newUser.set("phone", HtmlEncoder.text(userModel.getStr("phone")));
    newUser.set("gender", HtmlEncoder.text(userModel.getStr("gender")));
    newUser.set("language", userModel.getInt("language"));
    newUser.set("qq", userModel.getLong("qq"));
    newUser.set("mtime", OjConfig.timeStamp);
    
    return newUser.update();
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
    String ip = SessionService.me().getHost();
    
    if (success)
    {
      UserModel userModel = getCurrentUser();
      userModel.set("token", null).set("login", OjConfig.timeStamp).set("login_ip", ip).update();
      
      loginLog.set("uid", userModel.getUid());
    }
    
    loginLog.set("name", name).set("ip", ip).set("ctime", OjConfig.timeStamp).set("succeed", success);
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

  public void uploadAvatar(File file, int width, int height, OjController c) throws Exception
  {
    ImageScaleImpl imageScale = new ImageScaleImpl();
    
    imageScale.resizeFix(file, file, width, height);
    BufferedImage srcImgBuff = ImageIO.read(file);
    width = srcImgBuff.getWidth();
    height = srcImgBuff.getHeight();
    
    c.setAttr("width", width);
    c.setAttr("height", height);
  }
  
  /**
   * cut and save user avatar.
   * @param imageSource source of image file.
   * @param x1 left of selection area.
   * @param y1 top of selection area.
   * @param x2 right of selection area.
   * @param y2 bottom of selection area.
   * @throws Exception
   */
  public void saveAvatar(String imageSource, int x1, int y1, int x2, int y2) throws Exception
  {
    int cutWidth = x2 - x1;
    int catHeight = y2 - y1;
    UserModel userModel = getCurrentUser();
    String rootPath = PathKit.getWebRootPath() + File.separator;
    String srcFileName = rootPath + imageSource;
    String ext = FileKit.getFileType(srcFileName);
    String destFileName = new StringBuilder(4).append(OjConfig.userAvatarPath).append(File.separator).append(userModel.getUid()).append(ext).toString();
    File srcFile = new File(srcFileName);
    File destFile = new File(destFileName);
    ImageScaleImpl imageScale = new ImageScaleImpl();

    imageScale.resizeFix(srcFile, destFile, OjConstants.AVATAR_WIDTH, OjConstants.AVATAR_HEIGHT, x1, y1, cutWidth, catHeight);
    FileUtil.delete(srcFile);
    userModel.set("avatar", destFileName.replace(rootPath, "")).update();
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
    if (userModel == null)
      return false;
    
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
