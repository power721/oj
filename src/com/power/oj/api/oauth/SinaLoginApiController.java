package com.power.oj.api.oauth;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresGuest;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.service.SessionService;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class SinaLoginApiController extends OjController
{
  private static final Logger log = Logger.getLogger(SinaLoginApiController.class);
  private static final UserService userService = UserService.me();

  @RequiresGuest
  public void index()
  {
    try
    {
      SinaOauth sina = new SinaOauth();
      redirect(sina.getAuthorizeUrl());
    } catch (Exception e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
      redirect("/");
    }
  }

  @RequiresGuest
  public void callback()
  {
    try
    {
      SinaOauth sina = new SinaOauth();
      Map<String, String> userInfo = sina.getUserInfoByCode(getPara("code"));
      String type = "Sina";
      String openid = userInfo.get("openid");
      String nickname = userInfo.get("name");
      String avatar = userInfo.get("avatar_hd");
      WebLoginModel webLogin = WebLoginModel.dao.findByOpenID(openid, type);
      
      if (null == webLogin)
      {
        webLogin = new WebLoginModel();
        webLogin.set(WebLoginModel.OPENID, openid);
        webLogin.set(WebLoginModel.TYPE, type);
        webLogin.set(WebLoginModel.AVATAR, avatar);
        webLogin.set(WebLoginModel.CTIME, OjConfig.timeStamp);
        webLogin.set(WebLoginModel.STATUS, false);
        webLogin.set(WebLoginModel.NICK, nickname).save();
      }
      
      boolean status = webLogin.getBoolean(WebLoginModel.STATUS);
      if (null != webLogin.getInt(WebLoginModel.ID) && !status)
      {
        if (null == webLogin.getInt(WebLoginModel.UID))
        {
          setSessionAttr("nouser", true);
        }
        
        setSessionAttr("id", webLogin.getInt(WebLoginModel.ID));
        setSessionAttr("type", type);
        setSessionAttr("nickname", nickname);
        setSessionAttr("avatar", avatar);
        redirect("/user/bind");
        return;
      }
      
      //if (status)
      {
        UserModel userModel = UserModel.dao.findById(webLogin.getInt(WebLoginModel.UID));
        if (!userService.autoLogin(this, userModel, false))
        {
          setFlashMessage(new FlashMessage("Auto login failed, please inform admin!", MessageType.ERROR, getText("message.error.title")));
        }
      }
    } catch (Exception e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
      setFlashMessage(new FlashMessage(getText("user.signin.error"), MessageType.ERROR, getText("message.error.title")));
    }
    redirect(SessionService.me().getLastAccessURL());
  }
  
}
