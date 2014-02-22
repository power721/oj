package com.power.oj.api;

import java.io.File;
import org.apache.shiro.authz.annotation.RequiresGuest;

import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.plugin.shiro.ClearShiro;
import com.jfinal.upload.UploadFile;
import com.power.oj.api.oauth.WebLoginModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.service.SessionService;
import com.power.oj.mail.MailService;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserExtModel;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

@Before(GuestInterceptor.class)
public class UserApiController extends OjController
{
  private static final UserService userService = UserService.me();
  private static final MailService mailService = MailService.me();

  public void splash()
  {
    UserModel userModel = userService.getCurrentUserExt();
    
    userModel.put("unReadMail", mailService.countUserNewMails(userModel.getUid()));
    
    setAttr(OjConstants.USER, userModel);
    
    render("ajax/splash.html");
  }

  @ClearInterceptor
  @Before(POST.class)
  @RequiresGuest
  public void bind()
  {
    String email = getPara("email");
    Integer webId = getParaToInt("id");
    WebLoginModel webLogin = WebLoginModel.dao.findById(webId);
    
    if (webLogin == null)
    {
      renderJson("status", "error");
      return;
    }
    
    UserModel userModel = userService.getUserByEmail(email);
    // TODO data from old OJ contains duplicate emails
    if (userModel == null)
    {
      try
      {
        userModel = userService.signup(email, webLogin);
      } catch (Exception e)
      {
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.error(e.getLocalizedMessage());
        renderJson("status", "mail_error");
        return;
      }
    }
    
    webLogin.set(WebLoginModel.UID, userModel.getUid());
    webLogin.set(WebLoginModel.STATUS, true); // change after email verified?
    if (webLogin.update())
    {
      renderJson("status", "ok");
      return;
    }
    renderJson("status", "error");
  }
  
  public void profile()
  {
    UserModel userModel = getAttr(OjConstants.USER);
    userModel.put("success", true);
    userModel.remove("token").remove("pass").remove("data");
    renderJson(userModel);
  }

  @ClearInterceptor
  public void info()
  {
    int uid = 0;
    String name = "";
    UserModel userModel = null;

    if (isParaExists("uid"))
    {
      uid = getParaToInt("uid");
      userModel = userService.getUserInfoByUid(uid);
    } else if (isParaExists("name"))
    {
      name = getPara("name");
      userModel = userService.getUserInfoByName(name);
    }

    if (userModel == null)
    {
      renderJson("{result:'User does not exist!'}");
    } else
    {
      userModel.put("success", true);
      userModel.remove("token").remove("pass").remove("realname").remove("phone").remove("data");
      renderJson(userModel);
    }
  }
  
  public void checkin()
  {
    int incExp = 0;
    Integer uid = userService.getCurrentUid();
    UserExtModel userModel = UserExtModel.dao.findById(uid);
    if ((incExp = userService.checkin(userModel)) > 0)
    {
      int checkinTimes = userModel.getInt("checkin_times");
      renderJson("{\"success\":true, \"incexp\":" + incExp +",\"result\":" + checkinTimes + "}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"User already checkin.\"}");
    }
  }
  
  @ClearInterceptor
  public void online()
  {
    Integer uid = getParaToInt("uid");
    String name = getPara("name");
    SessionService.me().updateOnline(uid, name);
    
    renderNull();
  }
  
  @Before(POST.class)
  public void signSubmit()
  {
    String sign = HtmlEncoder.text(getPara("sign", "").trim());
    UserModel userModel = getAttr(OjConstants.USER);
    if (userModel.set("sign", sign).update())
    {
      renderJson("{\"success\":true}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Update sign failed.\"}");
    }
  }
  
  @Before(POST.class)
  public void pwdSubmit()
  {
    UserModel userModel = getAttr(OjConstants.USER);
    String origPwd = getPara("origPwd");
    String newPwd = getPara("newPwd");
    
    if (userService.checkPassword(userModel.getUid(), origPwd))
    {
      newPwd = BCrypt.hashpw(newPwd, BCrypt.gensalt());
      if (userModel.set("pass", newPwd).update())
      {
        renderJson("{\"success\":true}");
      }
      else
      {
        renderJson("{\"success\":false, \"result\":\"Update password failed.\"}");
      }
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Password is incorrect.\"}");
    }
  }
  
  public void emailSubmit()
  {
    UserModel userModel = getAttr(OjConstants.USER);
    Integer uid = userModel.getUid();
    String origPwd = getPara("origPwd");
    String newEmail = getPara("newEmail");
    
    if (userService.checkPassword(uid, origPwd))
    {
      if (userService.containsEmailExceptThis(uid, newEmail))
      {
        renderJson("{\"success\":false, \"result\":\"This email already used by other.\"}");
        return;
      }
      
      userModel.set("email", newEmail).set("email_verified", false);
      if (userModel.update())
      {
        renderJson("{\"success\":true}");
      }
      else
      {
        renderJson("{\"success\":false, \"result\":\"Update email failed.\"}");
      }
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Password is incorrect.\"}");
    }
  }
  
  @Before(POST.class)
  public void profileSubmit()
  {
    UserModel userModel = getAttr(OjConstants.USER);
    userModel.set("comefrom", getPara("comefrom"));
    userModel.set("qq", getPara("qq"));
    userModel.set("blog", getPara("blog"));
    userModel.set("phone", getPara("phone"));
    userModel.set("realname", getPara("realname"));
    userModel.set("gender", getPara("gender")); // TODO check gender value
    userModel.set("school", getPara("school"));
    
    if (userModel.update())
    {
      renderJson("{\"success\":true}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Update sign failed.\"}");
    }
  }

  @Before(POST.class)
  public void uploadAvatar()
  {
    UploadFile uploadFile = getFile("Filedata", "", 10 * 1024 * 1024, "UTF-8");
    File file = uploadFile.getFile();
    String fileName = null;
    
    try
    {
      fileName = userService.saveAvatar(file);
      setCookie("oj_userimg", fileName, OjConstants.COOKIE_AGE);
    } catch (Exception e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
      renderJson("{\"success\":false, \"state\":\"FAIL\"}");
      return;
    }

    renderJson("{\"success\":true, \"state\":\"SUCCESS\", \"url\":\"" + fileName + "\"}");
  }

  @Before(POST.class)
  @ClearInterceptor
  @ClearShiro
  public void signin()
  {
    String username = getPara("username");
    String password = getPara("password");
    boolean rememberMe = getParaToBoolean("rememberMe", false);

    if (userService.login(username, password, rememberMe))
    {
      UserModel userModel = userService.getCurrentUser();
      setCookie("auth_key", String.valueOf(userModel.getUid()), OjConstants.COOKIE_AGE);
      setCookie("oj_username", username, OjConstants.COOKIE_AGE);
      renderJson("{\"success\":true}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"User signin failed.\"}");
    }
  }

  public void getSubmissions()
  {
    int pageNumber = getParaToInt("page", 1);
    int pageSize = getParaToInt("size", OjConfig.statusPageSize);
    Integer result = getParaToInt("resultId", -1);
    Integer language = getParaToInt("languageId", -1);
    Integer uid = userService.getCurrentUid();
    
    renderJson(SolutionService.me().getPage(pageNumber, pageSize, result, language, uid));
  }
  
}
