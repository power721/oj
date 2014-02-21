package com.power.oj.api.oauth;

import java.util.Map;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;

public class QQLoginApiController extends OjController
{
  private static final Logger log = Logger.getLogger(QQLoginApiController.class);

  public void index()
  {
    try
    {
      QQOauth qq = new QQOauth();
      redirect(qq.getAuthorizeUrl());
    } catch (Exception e)
    {
      log.error(e.getMessage());
      redirect("/");
    }
  }

  public void callback()
  {
    try
    {
      QQOauth qq = new QQOauth();
      Map<String, String> userInfo = qq.getUserInfoByCode(getPara("code"));
      String type = "qq";
      String openid = userInfo.get("openid");
      String nickname = userInfo.get("nickname");
      String photoUrl = userInfo.get("figureurl_2");
      WebLoginModel login = WebLoginModel.dao.findByOpenID(openid, type);
      
      if (null == login)
      {
        login = new WebLoginModel();
        login.set(WebLoginModel.OPENID, openid);
        login.set(WebLoginModel.TYPE, type);
        login.set(WebLoginModel.AVATAR, photoUrl);
        login.set(WebLoginModel.CTIME, OjConfig.timeStamp);
        login.set(WebLoginModel.STATUS, false);
        login.set(WebLoginModel.NICK, nickname).save();
      }
      
      boolean status = login.getBoolean(WebLoginModel.STATUS);
      if (null != login.getInt(WebLoginModel.ID) && !status)
      {
        if (null == login.getInt(WebLoginModel.UID))
        {
          setAttr("nouser", true);
        }
        
        setAttr("id", login.getInt(WebLoginModel.ID));
        setAttr("type", type);
        setAttr("nickname", nickname);
        setAttr("photourl", photoUrl);
        render("binding");
        return;
      }
      if (status)
      {
        // TODO login
        //User user = User.dao.findById(login.getInt(WebLoginModel.UID));
        //setSessionAttr(Consts.USER_SESSION, user);
      }
    } catch (Exception e)
    {
      log.error(e.getLocalizedMessage());
    }
    redirect("/admin");
  }
}
