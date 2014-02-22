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

public class QQLoginApiController extends OjController
{
  private static final Logger log = Logger.getLogger(QQLoginApiController.class);
  private static final UserService userService = UserService.me();

  @RequiresGuest
  public void index()
  {
    try
    {
      QQOauth qq = new QQOauth();
      redirect(qq.getAuthorizeUrl());
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
      QQOauth qq = new QQOauth();
      Map<String, String> userInfo = qq.getUserInfoByCode(getPara("code"));
      String type = "QQ";
      String openid = userInfo.get("openid");
      String nickname = userInfo.get("nickname");
      String avatar = userInfo.get("figureurl_2");
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
      
      if (status)
      {
        UserModel userModel = UserModel.dao.findById(webLogin.getInt(WebLoginModel.UID));
        userService.autoLogin(userModel, false);
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
