package com.power.oj.user;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;

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
import com.power.oj.core.bean.FlashMessage;
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
  /*
   * If most controllers use these services, move them to OjController.
   */
  private static final UserService userService = UserService.me();
  private static final SessionService sessionService = SessionService.me();
  
  public void index()
  {
    setTitle("User Center");
  }

  @ActionKey("/rank")
  public void rank()
  {
    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.userPageSize);
    
    setAttr(OjConstants.USER_LIST, UserModel.dao.getUserRankList(pageNumber, pageSize));
    
    setTitle("Ranklist");
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
      FlashMessage msg = new FlashMessage("The user does not exist!", MessageType.WARN, "Warning!");
      redirect("/", msg);
      return;
    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    userModel.put("createTime", sdf.format(new Date(userModel.getInt("ctime") * 1000L)));
    userModel.put("loginTime", sdf.format(new Date(userModel.getInt("login") * 1000L)));
    userModel.put("rank", UserModel.dao.getUserRank(userModel.getUid()));
    setAttr(OjConstants.USER, userModel);
    
    setTitle("User Profile");
  }

  public void info()
  {
    int uid = 0;
    String name = "";
    UserModel userModel = null;

    if (isParaExists("uid"))
    {
      uid = getParaToInt("uid");
      userModel = UserModel.dao.getUserInfoByUid(uid);
    } else if (isParaExists("name"))
    {
      name = getPara("name");
      userModel = UserModel.dao.getUserInfoByName(name);
    }

    if (userModel == null)
    {
      renderJson("{error:true}");
    } else
    {
      userModel.remove("token").remove("pass").remove("realname").remove("phone").remove("data");
      renderJson(userModel);
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
  }

  @ActionKey("/login")
  public void login()
  {
    if (userService.isAuthenticated())
    {
      FlashMessage msg = new FlashMessage("You already login!", MessageType.WARN, "Warning!");
      redirect(sessionService.getLastAccessURL(), msg);
      return;
    }
    
    setTitle("Login");
  }

  @Before(POST.class)
  @ClearShiro
  public void signin()
  {
    if (userService.isAuthenticated())
    {
      FlashMessage msg = new FlashMessage("You already login!", MessageType.WARN, "Warning!");
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
    // TODO: recodr login fail times

    FlashMessage msg = new FlashMessage("Sorry, you entered invalid username or password.", MessageType.ERROR, "Error!");
    setAttrMessage(msg);
    keepPara("name");
    
    setTitle("Login");
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

  @RequiresPermissions("user:upload:avatar")
  public void avatar()
  {
    setTitle("Upload Avatar");
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
    FlashMessage msg = new FlashMessage("Please check your mailbox and follow the instruction to recover your account.");
    
    userModel.set("token", token).set("mtime", OjConfig.timeStamp).update();
    try
    {
      // TODO: create new thread to send mail
      OjService.me().sendResetPasswordEmail(name, email, token);
    } catch (Exception e)
    {
      log.error(e.getLocalizedMessage());
      msg = new FlashMessage("Sorry.Send mail failed, please inform admin.", MessageType.ERROR, "Error!");
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
      setSessionAttr("name", name);
      setTitle("Reset Password");
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
    
    FlashMessage msg = new FlashMessage("Congratulations! Your password is reseted.");
    redirect("/login", msg);
  }

  @ActionKey("/signup")
  @RequiresGuest
  public void signup()
  {
    setTitle("Signup");
  }
  
  @Before({POST.class, SignupValidator.class})
  @RequiresGuest
  public void save()
  {
    UserModel userModel = getModel(UserModel.class, "user");

    userService.signup(userModel);

    redirect("/user/edit", new FlashMessage("Congratulations! You have a new account now.<br>Please update your information."));
  }

  @RequiresAuthentication
  public void edit()
  {
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    
    setTitle("Account");
  }

  @Before({POST.class, UpdateUserValidator.class})
  @RequiresAuthentication
  public void update()
  {
    UserModel userModel = getModel(UserModel.class, "user");
    
    userService.updateUser(userModel);

    String redirectURL = new StringBuilder(2).append("/user/profile/").append(getAttr(OjConstants.USER_NAME)).toString();
    redirect(redirectURL, new FlashMessage("Your account updated."));
  }

  /******************** admin methods ********************/
  @RequiresPermissions("user:delete")
  public void delete()
  {
    renderText("TODO");
  }

  @RequiresPermissions("user:online")
  public void online()
  {
    setAttr("loginUserNum", sessionService.getUserNumber());
    setAttr(OjConstants.USER_LIST, sessionService.getAccessLog());
    
    setTitle("Online Users");
  }

  @RequiresPermissions("user:build")
  public void build()
  {
    String name = getPara(0);
    UserModel userModel = UserModel.dao.getUserByName(name);
    String redirectURL = new StringBuilder(2).append("/user/profile/").append(name).toString();
    FlashMessage msg = new FlashMessage("The user statistics have been updated.");
    
    if (!userService.build(userModel))
    {
      msg = new FlashMessage("The user statistics build failed.", MessageType.ERROR, "Error!");
      log.error(msg.getContent());
    }

    redirect(redirectURL, msg);
  }
  
}
