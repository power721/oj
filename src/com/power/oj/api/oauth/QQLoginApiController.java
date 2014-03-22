package com.power.oj.api.oauth;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresGuest;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.service.SessionService;
import com.power.oj.user.UserModel;

public class QQLoginApiController extends OjController
{
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
        webLogin.setOpenId(openid);
        webLogin.setType(type);
        webLogin.setAvatar(avatar);
        webLogin.setCtime(OjConfig.timeStamp);
        webLogin.setStatus(false);
        webLogin.setNick(nickname).save();
      }
      
      boolean status = webLogin.getStatus();
      if (null != webLogin.getId() && !status)
      {
        if (null == webLogin.getUid())
        {
          setSessionAttr("nouser", true);
        }
        
        setSessionAttr("id", webLogin.getId());
        setSessionAttr("type", type);
        setSessionAttr("nickname", nickname);
        setSessionAttr("avatar", avatar);
        redirect("/user/bind");
        return;
      }
      
      //if (status)
      {
        UserModel userModel = UserModel.dao.findById(webLogin.getUid());
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
