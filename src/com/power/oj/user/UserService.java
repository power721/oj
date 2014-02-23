package com.power.oj.user;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import jodd.io.FileUtil;
import jodd.io.ZipUtil;
import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.api.oauth.WebLoginModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.service.OjService;
import com.power.oj.core.service.SessionService;
import com.power.oj.image.ImageScaleImpl;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.util.FileKit;
import com.power.oj.util.Tool;

public class UserService
{
  private static final Logger log = Logger.getLogger(UserService.class);
  private static final UserService me = new UserService();
  private static final UserModel dao = UserModel.dao;
  
  private UserService() {}
  
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
    Subject currentUser = ShiroKit.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(name, password);
    token.setRememberMe(rememberMe);

    try
    {
      currentUser.login(token);

      updateLogin(name, true);
      SessionService.me().updateLogin();
    } catch (AuthenticationException e)
    {
      updateLogin(name, false);
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn("User signin failed.");
      return false;
    }

    return true;
  }
  
  public boolean autoLogin(UserModel userModel, boolean rememberMe)
  {
    String name = userModel.getStr("name");
    String password = userModel.getStr("pass");
    Subject currentUser = ShiroKit.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(name, password);
    token.setRememberMe(rememberMe);

    try
    {
      currentUser.login(token);

      updateLogin(name, true);
      SessionService.me().updateLogin();
    } catch (AuthenticationException e)
    {
      updateLogin(name, false);
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn("User signin failed.");
      return false;
    }

    return true;
  }
  
  public void addExp(UserExtModel userExtModel, int incExp)
  {
    int exp = userExtModel.getInt("exp") + incExp;
    int credit = userExtModel.getInt("credit") + incExp;
    int level = Arrays.binarySearch(OjConfig.level.toArray(), exp);
    level = level<0 ? -level : level+2;
    
    userExtModel.set("credit", credit).set("exp", exp).set("level", level);
  }
  
  public int checkin(UserExtModel userExtModel)
  {
    int timestamp = Tool.getDayTimestamp();
    int checkin = userExtModel.getInt("checkin");
    int checkinTimes = userExtModel.getInt("checkin_times");
    int level = userExtModel.getInt("level");
    
    if (checkin < timestamp)
    {
      checkinTimes = (checkin + OjConstants.DAY_TIMESTAMP < timestamp) ? 1 : checkinTimes + 1;
      int incExp = Math.min(checkinTimes, level);
      int totalCheckin = userExtModel.getInt("total_checkin") + 1;
      
      addExp(userExtModel, incExp);
      userExtModel.set("checkin", OjConfig.timeStamp).set("checkin_times", checkinTimes).set("total_checkin", totalCheckin).update();
      return incExp;
    }
    
    return 0;
  }
  
  public boolean isCheckin(UserModel userModel)
  {
    int checkin = userModel.getInt("checkin");
    
    if (checkin < Tool.getDayTimestamp())
    {
      return false;
    }
    else
    {
      userModel.put("isCheckin", checkin);
      return true;
    }
  }
  
  /**
   * User logout in Shiro session.
   */
  public void logout(OjController controller)
  {
    UserModel userModel = getCurrentUser();
    if (userModel != null)
    {
      int online = userModel.getInt("online");
      int login = userModel.getInt("login");
      
      log.info("online: " + online + " login: " + login + " current: " + OjConfig.timeStamp);
      online += (OjConfig.timeStamp - login) / 60;
      userModel.set("online", online).update();
    }
    
    controller.removeCookie("auth_key");
    controller.removeCookie("oj_userimg");
    controller.removeCookie("oj_time");

    ShiroKit.getSubject().logout();
  }
  
  /**
   * user signup
   * @param userModel
   * @return
   * @throws Exception 
   */
  public boolean signup(UserModel userModel) throws Exception
  {
    String name = HtmlEncoder.text(userModel.getStr("name"));
    String password = BCrypt.hashpw(userModel.getStr("pass"), BCrypt.gensalt());
    String email = userModel.getStr("email");
    String token = UUID.randomUUID().toString();
    
    long ctime = OjConfig.timeStamp;
    UserModel newUser = new UserModel();
    newUser.set("name", name).set("pass", password).set("email", email).set("reg_email", email).set("ctime", ctime);
    newUser.set("token", token).set("mtime", OjConfig.timeStamp);
    
    if (newUser.save())
    {
      int uid = newUser.getUid();
      Db.update("INSERT INTO user_role (rid,uid) SELECT id,? FROM role WHERE name='user'", uid);
      
      UserExtModel userExt = new UserExtModel();
      userExt.set("uid", uid).save();
      //password = userModel.getStr("pass");
      //return login(name, password, false);
      
      OjService.me().sendVerifyEmail(name, email, token);
      return true;
    }
    
    return false;
  }
  
  public UserModel signup(String email, WebLoginModel webLogin) throws Exception
  {
    String name = email;
    String pass = Tool.randomPassword(9);
    String password = BCrypt.hashpw(pass, BCrypt.gensalt());
    String avatar = webLogin.getStr("avatar");
    String token = UUID.randomUUID().toString();
    long ctime = OjConfig.timeStamp;
    
    UserModel newUser = new UserModel();
    newUser.set("name", name).set("pass", password).set("email", email).set("reg_email", email);
    newUser.set("nick", webLogin.get("nick")).set("avatar", avatar).set("ctime", ctime);
    newUser.set("token", token).set("mtime", OjConfig.timeStamp);
    
    if (newUser.save())
    {
      webLogin.set(WebLoginModel.STATUS, true).update();
      
      int uid = newUser.getUid();
      Db.update("INSERT INTO user_role (rid,uid) SELECT id,? FROM role WHERE name='user'", uid);
      
      UserExtModel userExt = new UserExtModel();
      userExt.set("uid", uid).save();
    }
    Map<String, Object> paras = new HashMap<String, Object>();
    paras.put(OjConstants.BASE_URL, OjConfig.baseUrl);
    paras.put(OjConstants.SITE_TITLE, OjConfig.siteTitle);
    paras.put("nick", webLogin.get("nick"));
    paras.put("name", name);
    paras.put("token", token);
    paras.put("password", pass);
    paras.put("ctime", OjConfig.timeStamp);
    paras.put("expires", OjConstants.VERIFY_EMAIL_EXPIRES_TIME / OjConstants.MINUTE_IN_MILLISECONDS);
    
    autoLogin(newUser, false);
    OjService.me().sendVerifyEmail(name, email, paras);
    
    return newUser;
  }
  
  /**
   * update user
   * @param userModel
   * @return
   */
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
    newUser.set("qq", userModel.getStr("qq"));
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
      if (userModel.getBoolean("email_verified"))
      {
        userModel.set("token", null); // resetPassword token
      }
      userModel.set("login", OjConfig.timeStamp).set("login_ip", ip).update();
      
      loginLog.set("uid", userModel.getUid());
    }
    
    loginLog.set("name", name).set("ip", ip).set("ctime", OjConfig.timeStamp).set("success", success);
    return Db.save("loginlog", loginLog);
  }
  
  public boolean updateEmail(UserModel userModel, String email) throws Exception
  {
    String name = userModel.getStr("name");
    String token = UUID.randomUUID().toString();
    
    userModel.set("email", email).set("email_verified", false);
    userModel.set("token", token).set("mtime", OjConfig.timeStamp);
    OjService.me().sendVerifyEmail(name, email, token);
    
    return userModel.update();
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
    
    Integer uid = userModel.getUid();
    
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

    record = Db.findFirst("SELECT COUNT(distinct pid) AS count FROM solution WHERE uid=? AND result=0 LIMIT 1", uid);
    if (record != null)
    {
      userModel.set("solved", record.getLong("count"));
    }

    return userModel.update();
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
    
    userModel.set("token", null).set("email_verified", true).set("pass", BCrypt.hashpw(password, BCrypt.gensalt()));
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
      if (OjConfig.timeStamp - userModel.getInt("mtime") <= OjConstants.RESET_PASSWORD_EXPIRES_TIME)
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
  
  public boolean verifyEmail(String name, String token)
  {
    UserModel userModel = dao.getUserByName(name);
    
    if (userModel != null && token != null && token.equals(userModel.getStr("token")) && !userModel.getBoolean("email_verified"))
    {
      if (OjConfig.timeStamp - userModel.getInt("mtime") <= OjConstants.VERIFY_EMAIL_EXPIRES_TIME)
      {
        log.info(String.valueOf(OjConfig.timeStamp - userModel.getInt("mtime")));
        userModel.set("token", null).set("email_verified", true).update();
        
        if (ShiroKit.isGuest())
        {
          autoLogin(userModel, false);
        }
        return true;
      }
      else
      {
        userModel.set("token", null).update();
        log.info("token expires");
        return false;
      }
    }
    
    if (OjConfig.getDevMode())
    {
      log.info(name + " " + token);
      log.info(userModel.toString());
    }
    log.info("token invlidate");
    return false;
  }
  
  /**
   * upload and resize user avatar.
   * @param file file of the avatar.
   * @param maxWidth max width of the thumbnail.
   * @param maxHeight max height of the thumbnail.
   * @param controller the controller to set attr.
   * @throws Exception
   */
  public void uploadAvatar(File file, int maxWidth, int maxHeight, OjController controller) throws Exception
  {
    ImageScaleImpl imageScale = new ImageScaleImpl();
    imageScale.resizeFix(file, file, maxWidth, maxHeight);
    
    BufferedImage srcImgBuff = ImageIO.read(file);
    controller.setAttr("width", srcImgBuff.getWidth());
    controller.setAttr("height", srcImgBuff.getHeight());
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
    destFileName = destFileName.replace(rootPath, "").replace("\\", "/");
    userModel.set("avatar", destFileName).update();
  }
  
  public String saveAvatar(File srcFile) throws Exception
  {
    UserModel userModel = getCurrentUser();
    String rootPath = PathKit.getWebRootPath() + File.separator;
    String srcFileName = srcFile.getAbsolutePath();
    String ext = FileKit.getFileType(srcFileName);
    String destFileName = new StringBuilder(4).append(OjConfig.userAvatarPath).append(File.separator).append(userModel.getUid()).append(ext).toString();
    
    File destFile = new File(destFileName);
    
    FileUtil.moveFile(srcFile, destFile);
    
    destFileName = destFileName.replace(rootPath, "").replace("\\", "/");
    userModel.set("avatar", destFileName).update();
    
    return destFileName;
  }
  
  /**
   * Search user by key word in scope.
   * @param scope "all", "name", "nick", "school", "email".
   * @param word key word.
   * @return the list of users.
   */
  public Page<UserModel> searchUser(int pageNumber, int pageSize, String scope, String word)
  {
    String select = "SELECT @num:=@num+1 AS num,uid,name,nick,school,solved,submit";
    Page<UserModel> userList = null;
    List<Object> paras = new ArrayList<Object>();
    paras.add((pageNumber - 1) * pageSize);

    if (StringUtil.isBlank(word))
    {
      word = "No Such User!";
    }
    {
      word = new StringBuilder(3).append("%").append(word).append("%").toString();
      StringBuilder sb = new StringBuilder("FROM user,(SELECT @num:=?)r WHERE (");
      
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
      
      userList = dao.paginate(pageNumber, pageSize, select, sb.toString(), paras.toArray());
    }
    
    return userList;
  }
  
  /**
   * get user profile by name.
   * @param name string of user name.
   * @return UseModel with submitted problems.
   */
  public UserModel getUserProfile(String name)
  {
	  UserModel userModel = null;
	    
	    if (name == null)
	    {
	      userModel = getCurrentUser();
	    } else
	    {
	      userModel = getUserByName(name);
	    }
	    
	    if (userModel != null)
	    {
	    	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        userModel.put("createTime", sdf.format(new Date(userModel.getInt("ctime") * 1000L)));
	        userModel.put("loginTime", sdf.format(new Date(userModel.getInt("login") * 1000L)));
	        userModel.put("rank", getUserRank(userModel.getUid()));
	        userModel.put("problems", dao.getSubmittedProblems(userModel.getUid()));
	    }
	    
	    return userModel;
  }
  
  /**
   * get user's login logs.
   * @param pageNumber
   * @param pageSize
   * @return
   */
  public Page<Record> getLoginlog(int pageNumber, int pageSize)
  {
    UserModel userModel = getCurrentUser();
   
    Integer uid = userModel.getUid();
    String name = userModel.get("name");
    Page<Record> logs = Db.paginate(pageNumber, pageSize, "SELECT @num:=@num+1 AS num,l.*",
                        "FROM loginlog l,(SELECT @num:=?)r WHERE uid=? OR name=? ORDER BY ctime DESC", 
                        (pageNumber - 1) * pageSize, uid, name);
    return logs;
  }
  
  /**
   * archive user source code.
   * @return zip file.
   * @throws IOException
   */
  public File archiveCode() throws IOException
  {
    UserModel userModel = getCurrentUser();
    Integer uid = userModel.getUid();
    List<Record> codes = dao.getSolvedProblems(uid);
    String userDir = new StringBuilder(3).append(OjConfig.downloadPath).append(File.separator).append(userModel.getStr("name")).toString();
    File userDirFile = new File(userDir);
    FileUtil.mkdirs(userDirFile);
    
    for (Record code : codes)
    {
      String problemDir = new StringBuilder(3).append(userDir).append(File.separator).append(code.get("pid")).toString();
      FileUtil.mkdirs(problemDir);
      
      String ext = OjConfig.language_type.get(code.get("language")).getStr("ext");
      StringBuilder sb = new StringBuilder(10).append(problemDir).append(File.separator).append(code.get("sid")).append("_").append(code.get("time"));
      sb.append("MS_").append(code.get("memory")).append("KB").append(".").append(ext);
      
      File file = new File(sb.toString());
      if (file.createNewFile() == false)
      {
        continue;
      }
      
      FileUtil.writeString(file, code.getStr("source"));
    }
    
    ZipUtil.zip(userDirFile);
    File zipFile = new File(userDirFile.getAbsolutePath() + ".zip");
    
    return zipFile;
  }

  /**
   * Get current uid form Shiro.
   * @return the uid of current user or null.
   */
  public Integer getCurrentUid()
  {
    Subject currentUser = ShiroKit.getSubject();
    if (currentUser == null)
      return null;

    Object principal = currentUser.getPrincipal();
    if (principal == null)
      return null;
    
    return (Integer) principal;
  }

  /**
   * Get current user by uid.
   * @return current user or null.
   */
  public UserModel getCurrentUser()
  {
    return getUserByUid(getCurrentUid());
  }
  
  public UserModel getCurrentUserExt()
  {
    UserModel userModel = dao.getUserExt(getCurrentUid());
    int exp = userModel.getInt("exp");
    int level = userModel.getInt("level");
    int lastExp = 0;
    if (level > 1)
    {
      lastExp = OjConfig.level.get(level-2);
    }
    int nextExp = (1<<31) - 1;
    if (level - 1 < OjConfig.level.size())
    {
      nextExp = OjConfig.level.get(level-1);
    }
    
    userModel.put("nextExp", nextExp);
    userModel.put("percent", (int)((exp-lastExp)/(double)(nextExp-lastExp) * 100));
    
    return userModel;
  }
  
  public UserModel getUserByUid(Integer uid)
  {
    return dao.findById(uid);
  }

  public UserModel getUserByName(String name)
  {
    return dao.getUserByName(name);
  }

  public Integer getUidByName(String name)
  {
    UserModel userModel = getUserByName(name);
    
    if (userModel != null)
    {
      return userModel.getUid();
    }
    return null;
  }

  public UserModel getUserByEmail(String email)
  {
    return dao.getUserByEmail(email);
  }

  public UserModel getUserByNameAndEmail(String name, String email)
  {
    return dao.getUserByNameAndEmail(name, email);
  }

  public UserModel getUserInfoByName(String name)
  {
    return dao.getUserInfoByName(name);
  }

  public UserModel getUserInfoByUid(Integer uid)
  {
    return dao.getUserInfoByUid(uid);
  }

  public int getUserRank(Integer uid)
  {
    return dao.getUserRank(uid);
  }

  public Page<UserModel> getUserRankList(int pageNumber, int pageSize)
  {
    return dao.getUserRankList(pageNumber, pageSize);
  }

  public Page<UserModel> getUserRankListDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir, String sSearch)
  {
    List<Object> param = new ArrayList<Object>();
    String sql = "SELECT *";
    StringBuilder sb = new StringBuilder().append("FROM user WHERE 1=1");

    if (StringUtil.isNotEmpty(sSearch))
    {
      sb.append(" AND (name LIKE ? OR realname LIKE ?)");
      param.add(new StringBuilder(3).append("%").append(sSearch).append("%").toString());
      param.add(new StringBuilder(3).append("%").append(sSearch).append("%").toString());
    }
    sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", uid");

    return dao.paginate(pageNumber, pageSize, sql, sb.toString(), param.toArray());
  }

  public boolean checkPassword(Integer uid, String password)
  {
    String stored_hash = dao.findById(uid, "pass").getStr("pass");
    return BCrypt.checkpw(password, stored_hash);
  }

  public boolean containsEmail(String email)
  {
    return dao.getUserByEmail(email) != null;
  }

  public boolean containsUsername(String username)
  {
    return dao.getUserByName(username) != null;
  }

  public boolean isAdmin()
  {
    return ShiroKit.hasPermission("admin");
  }

  public boolean containsEmailExceptThis(Integer uid, String email)
  {
    return dao.containsEmailExceptThis(uid, email);
  }

}
