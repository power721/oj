package com.power.oj.user;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import jodd.io.FileUtil;
import jodd.util.HtmlEncoder;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.plugin.shiro.ClearShiro;
import com.jfinal.upload.UploadFile;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.Message;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.service.OjService;
import com.power.oj.core.service.SessionService;
import com.power.oj.user.validator.RecoverAccountValidator;
import com.power.oj.user.validator.ResetPasswordValidator;
import com.power.oj.user.validator.SignupValidator;
import com.power.oj.user.validator.UpdateUserValidator;
import com.power.oj.util.FileKit;

/**
 * 
 * @author power
 * 
 */
public class UserController extends OjController
{
  private static final UserService userService = UserService.me();
  private static final SessionService sessionService = SessionService.me();
  
  public void index()
  {
    render("index.html");
  }

  @ActionKey("/login")
  public void login()
  {
    if (userService.isAuthenticated())
    {
      Message msg = new Message("You already login.", MessageType.ERROR, "Error!");
      redirect(sessionService.getLastAccessURL(), msg);
      return;
    }
    
    setTitle("Login");
    render("login.html");
  }

  @Before(POST.class)
  @ClearShiro
  public void signin()
  {
    if (userService.isAuthenticated())
    {
      Message msg = new Message("You already login.", MessageType.ERROR, "Error!");
      redirect(sessionService.getLastAccessURL(), msg);
      return;
    }
    
    String name = getPara("name").trim();
    String password = getPara("password");
    boolean rememberMe = getParaToBoolean("rememberMe", false);

    if (userService.login(name, password, rememberMe))
    {
      redirect(sessionService.getLastAccessURL());
      return;
    }

    Message msg = new Message("Sorry, you entered invalid username or password.", MessageType.ERROR, "Error!");
    setAttrMessage(msg);
    setTitle("Login");
    keepPara("name");
    
    render("login.html");
  }

  @ClearInterceptor(ClearLayer.ALL)
  @ActionKey("/logout")
  public void logout()
  {
    String lastAccessURL = sessionService.getLastAccessURL();
    userService.logout();

    redirect(lastAccessURL);
  }

  public void profile()
  {
    String name = getPara(0);
    UserModel userModel = null;
    
    if (name == null)
    {
      userModel = getCurrentUser();
    } else
    {
      userModel = UserModel.dao.getUserByName(name);
    }
    
    if (userModel == null)
    {
      Message msg = new Message("The user does not exist!", MessageType.WARN, "Warning!");
      redirect("/", msg);
      return;
    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    setAttr("createTime", sdf.format(new Date(userModel.getInt("ctime") * 1000L)));
    setAttr("loginTime", sdf.format(new Date(userModel.getInt("login") * 1000L)));
    userModel.put("rank", UserModel.dao.getUserRank(userModel.getUid()));
    setAttr(OjConstants.USER, userModel);
    
    setTitle("User Profile");
    render("profile.html");
  }
  
  @RequiresPermissions("user:upload:avatar")
  public void avatar()
  {
    render("avatar.html");
  }

  @Before(POST.class)
  @RequiresPermissions("user:upload:avatar")
  public void uploadAvatar()
  {
    UploadFile uploadFile = getFile("Filedata", "", 10 * 1024 * 1024, "UTF-8");
    UserModel userModel = getCurrentUser();
    int uid = getParaToInt("uid", 0);

    if (uid != 0)
    {
      String ext = FileKit.getFileType(uploadFile.getOriginalFileName());
      String fileName = new StringBuilder(4).append(OjConfig.userAvatarPath).append(uid).append(".").append(userModel.getStr("avatar")).toString();

      try
      {
        FileUtil.deleteFile(fileName);
      } catch (IOException e)
      {
        log.warn(e.getLocalizedMessage());
      }

      fileName = new StringBuilder(3).append(OjConfig.userAvatarPath).append(uid).append(ext).toString();
      try
      {
        FileUtil.moveFile(uploadFile.getFile(), new File(fileName));
        userModel.set("avatar", ext.substring(1)).update();
        renderJson("FILEID:/oj/assets/images/user/" + uid + ext);
        return;
      } catch (IOException e)
      {
        log.error(e.getLocalizedMessage());
      }
    }

    renderJson("ERROR:true");
  }

  @RequiresGuest
  public void forget()
  {
    setTitle("Account Recovery");
  }
  
  @Before({POST.class, RecoverAccountValidator.class})
  @RequiresGuest
  public void recover()
  {
    String name = getPara("name");
    String email = getPara("email");
    String token = UUID.randomUUID().toString();
    UserModel userModel = UserModel.dao.getUserByName(name);
    Message msg = new Message("Please check your mailbox and follow the instruction to recover your account.");
    
    userModel.set("token", token).set("mtime", OjConfig.timeStamp).update();
    try
    {
      OjService.me().sendResetPasswordEmail(name, email, token);
    } catch (Exception e)
    {
      log.error(e.getLocalizedMessage());
      msg = new Message("Sorry.Send mail failed, please inform admin.", MessageType.ERROR, "Error!");
    }
    
    redirect(sessionService.getLastAccessURL(), msg);
  }
  
  @RequiresGuest
  public void reset()
  {
    String name = getPara("name");
    String token = getPara("token");
   
    if (userService.checkResetToken(name, token))
    {
      setTitle("Reset Password");
      setSessionAttr("name", name);
      render("reset.html");
      return;
    }
    
    renderError(404);
  }
  
  @Before({POST.class, ResetPasswordValidator.class})
  @RequiresGuest
  public void resetPassword()
  {
    String name = getSessionAttr("name");
    String password = getPara("pass");
    if (userService.resetPassword(name, password))
    {
      removeSessionAttr("name");
    }
    
    Message msg = new Message("Congratulations! Your password is reseted.");
    redirect("/login", msg);
  }

  @ActionKey("/signup")
  @RequiresGuest
  public void signup()
  {
    setTitle("Signup");
    render("signup.html");
  }
  
  @Before({POST.class, SignupValidator.class})
  @RequiresGuest
  public void save()
  {
    UserModel userModel = getModel(UserModel.class, "user");
    String password = userModel.getStr("pass");
    userModel.saveUser();

    userModel = userModel.findById(userModel.getUid());
    UsernamePasswordToken token = new UsernamePasswordToken(userModel.getStr("name"), password);
    Subject currentUser = userService.getSubject();
    currentUser.login(token);

    redirect("/user/edit", new Message("Congratulations! You have a new account now.<br>Please update your information."));
  }

  @RequiresAuthentication
  public void edit()
  {
    setTitle("Account");
    
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);

    render("edit.html");
  }

  @Before({POST.class, UpdateUserValidator.class})
  @RequiresAuthentication
  public void update()
  {
    UserModel userModel = getModel(UserModel.class, "user");
    userModel.updateUser();

    String redirectURL = new StringBuilder(2).append("/user/profile/").append(getAttr(OjConstants.USER_NAME)).toString();
    redirect(redirectURL, new Message("The changes have been saved."));
  }

  @RequiresPermissions("user:delete")
  public void delete()
  {
    renderText("TODO");
  }

  public void info()
  {
    int uid = 0;
    String name = "";
    UserModel user = null;

    if (isParaExists("uid"))
    {
      uid = getParaToInt("uid");
      user = UserModel.dao.getUserInfoByUid(uid);
    } else if (isParaExists("name"))
    {
      name = getPara("name");
      user = UserModel.dao.getUserInfoByName(name);
    }

    if (user == null)
    {
      renderJson("{error:true}");
    } else
    {
      user.remove("token").remove("pass").remove("realname").remove("phone").remove("data");
      renderJson(user);
    }
  }

  public void search()
  {
    String word = HtmlEncoder.text(getPara("word").trim());
    String scope = getPara("scope");
    setAttr(OjConstants.USER_LIST, userService.searchUser(scope, word));
    setAttr("word", word);
    setAttr("scope", scope != null ? scope : "all");
    setTitle(new StringBuilder(2).append("Search user: ").append(word).toString());

    render("search.html");
  }

  /*
   * public void searchUser() { String word =
   * HtmlEncoder.text(getPara("word").trim()); String scope = getPara("scope");
   * int pageNumber = getParaToInt("p", 1); Page<UserModel> userList = null;
   * 
   * if(StringUtil.isNotBlank(word)) { StringBuilder sb = new
   * StringBuilder("FROM user WHERE "); if(StringUtil.isNotBlank(scope))
   * sb.append(scope).append(" LIKE '%").append(word).append("%' "); else
   * sb.append ("name LIKE '%").append(word).append("%' OR nick LIKE '%").append
   * (word).append("%' OR email LIKE '%").append(word).append("%'");
   * sb.append(" AND status=1 ORDER BY solved desc,submit,uid");
   * 
   * userList = UserModel.dao.paginate(pageNumber, 50,
   * "SELECT uid,name,nick,school,solved,submit", sb.toString()); }
   * 
   * renderJson(userList); }
   */

  @RequiresPermissions("user:online")
  public void online()
  {
    setTitle("Online Users");
    setAttr("loginUserNum", sessionService.getUserNumber());
    setAttr(OjConstants.USER_LIST, sessionService.getAccessLog());

    render("online.html");
  }

  @ActionKey("/rank")
  public void rank()
  {
    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.userPageSize);
    
    setTitle("Ranklist");
    setAttr(OjConstants.USER_LIST, UserModel.dao.getUserRankList(pageNumber, pageSize));

    render("rank.html");
  }

  @RequiresPermissions("user:build")
  public void build()
  {
    int uid = getParaToInt(0);
    UserModel userModel = UserModel.dao.findById(uid);
    String redirectURL = new StringBuilder(2).append("/user/profile/").append(userModel.getStr("name")).toString();
    Message msg = new Message("The user statistics have been updated.");
    
    if (!userService.build(userModel))
    {
      msg = new Message("The user statistics build failed.", MessageType.ERROR, "Error!");
      log.error(msg.getContent());
    }

    redirect(redirectURL, msg);
  }
}
