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
import com.jfinal.kit.PathKit;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.service.OjService;
import com.power.oj.core.service.SessionService;
import com.power.oj.image.ImageScaleImpl;
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
    setTitle(getText("user.index.title"));
  }

  @ActionKey("/rank")
  public void rank()
  {
    int pageNumber = getParaToInt(0, 1);
    int pageSize = getParaToInt(1, OjConfig.userPageSize);
    
    setAttr("pageSize", OjConfig.userPageSize);
    setAttr(OjConstants.USER_LIST, UserModel.dao.getUserRankList(pageNumber, pageSize));
    
    setTitle(getText("user.rank.title"));
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
      FlashMessage msg = new FlashMessage(getText("user.error.none"), MessageType.WARN, getText("message.warn.title"));
      redirect("/", msg);
      return;
    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    userModel.put("createTime", sdf.format(new Date(userModel.getInt("ctime") * 1000L)));
    userModel.put("loginTime", sdf.format(new Date(userModel.getInt("login") * 1000L)));
    userModel.put("rank", UserModel.dao.getUserRank(userModel.getUid()));
    setAttr(OjConstants.USER, userModel);
    
    setTitle(getText("user.profile.title"));
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
    
    setTitle(new StringBuilder(2).append(getText("user.search.title")).append(word).toString());
  }

  @ActionKey("/login")
  public void login()
  {
    if (userService.isAuthenticated())
    {
      FlashMessage msg = new FlashMessage(getText("user.error.login"), MessageType.WARN, getText("message.warn.title"));
      redirect(sessionService.getLastAccessURL(), msg);
      return;
    }
    
    setTitle(getText("user.login.title"));
  }

  @Before(POST.class)
  @ClearShiro
  public void signin()
  {
    if (userService.isAuthenticated())
    {
      FlashMessage msg = new FlashMessage(getText("user.error.login"), MessageType.WARN, getText("message.warn.title"));
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

    FlashMessage msg = new FlashMessage(getText("user.signin.error"), MessageType.ERROR, getText("message.error.title"));
    setAttrMessage(msg);
    keepPara("name");
    
    setTitle(getText("user.login.title"));
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
    setTitle(getText("user.avatar.title"));
  }

  @Before(POST.class)
  @RequiresPermissions("user:upload:avatar")
  public void saveAvatar() throws IOException
  {
    int x1 = getParaToInt("x1");
    int y1 = getParaToInt("y1");
    int x2 = getParaToInt("x2");
    int y2 = getParaToInt("y2");
    int cutWidth = x2 - x1;
    int catHeight = y2 - y1;
    String rootPath = PathKit.getWebRootPath() + File.separator;
    String srcFileName = rootPath + getPara("imageSource");
    String ext = FileKit.getFileType(srcFileName);
    String destFileName = new StringBuilder(4).append(OjConfig.userAvatarPath).append(File.separator).append(userService.getCurrentUid()).append(ext).toString();
    File srcFile = new File(srcFileName);
    File destFile = new File(destFileName);
    ImageScaleImpl imageScale = new ImageScaleImpl();
    UserModel userModel = getCurrentUser();
    FlashMessage msg = new FlashMessage(getText("user.avatar.success"));
   
    try
    {
      imageScale.resizeFix(srcFile, destFile, 96, 96, x1, y1, cutWidth, catHeight);
      FileUtil.delete(srcFile);
      userModel.set("avatar", destFileName.replace(rootPath, "")).update();
      destFileName = destFile.getAbsolutePath().replace(rootPath, "");
    } catch (Exception e)
    {
      log.error(e.getLocalizedMessage());
      msg = new FlashMessage(getText("user.avatar.error"), MessageType.ERROR, getText("message.error.title"));
    }

    String redirectURL = new StringBuilder(2).append("/user/profile/").append(getAttr(OjConstants.USER_NAME)).toString();
    redirect(redirectURL, msg);
  }

  @RequiresGuest
  public void forget()
  {
    setTitle(getText("user.forget.title"));
  }
  
  @Before({POST.class, RecoverAccountValidator.class})
  @RequiresGuest
  public void recover()
  {
    String name = getPara("name");
    String email = getPara("email");
    String token = UUID.randomUUID().toString();
    UserModel userModel = UserModel.dao.getUserByName(name);
    FlashMessage msg = new FlashMessage(getText("user.recover.success"));
    
    userModel.set("token", token).set("mtime", OjConfig.timeStamp).update();
    try
    {
      // TODO: create new thread to send mail
      OjService.me().sendResetPasswordEmail(name, email, token);
    } catch (Exception e)
    {
      log.error(e.getLocalizedMessage());
      msg = new FlashMessage(getText("user.recover.error"), MessageType.ERROR, getText("message.error.title"));
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
      setTitle(getText("user.reset.title"));
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
    
    FlashMessage msg = new FlashMessage(getText("user.resetPassword.success"));
    redirect("/login", msg);
  }

  @ActionKey("/signup")
  @RequiresGuest
  public void signup()
  {
    setTitle(getText("user.signup.title"));
  }
  
  @Before({POST.class, SignupValidator.class})
  @RequiresGuest
  public void save()
  {
    UserModel userModel = getModel(UserModel.class, "user");

    userService.signup(userModel);

    redirect("/user/edit", new FlashMessage(getText("user.save.success")));
  }

  @RequiresAuthentication
  public void edit()
  {
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    
    setTitle(getText("user.edit.title"));
  }

  @Before({POST.class, UpdateUserValidator.class})
  @RequiresAuthentication
  public void update()
  {
    UserModel userModel = getModel(UserModel.class, "user");
    
    userService.updateUser(userModel);

    String redirectURL = new StringBuilder(2).append("/user/profile/").append(getAttr(OjConstants.USER_NAME)).toString();
    redirect(redirectURL, new FlashMessage(getText("user.update.success")));
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
    
    setTitle(getText("user.online.title"));
  }

  @RequiresPermissions("user:build")
  public void build()
  {
    String name = getPara(0);
    UserModel userModel = UserModel.dao.getUserByName(name);
    String redirectURL = new StringBuilder(2).append("/user/profile/").append(name).toString();
    FlashMessage msg = new FlashMessage(getText("user.build.success"));
    
    if (!userService.build(userModel))
    {
      msg = new FlashMessage(getText("user.build.error"), MessageType.ERROR, getText("message.error.title"));
      log.error(msg.getContent());
    }

    redirect(redirectURL, msg);
  }
  
}
