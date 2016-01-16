package com.power.oj.api.oauth;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.service.SessionService;
import com.power.oj.user.UserModel;
import jodd.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresGuest;

import java.util.Map;

public class SinaLoginApiController extends OjController {
    @RequiresGuest
    public void index() {
        try {
            SinaOauth sina = new SinaOauth();
            redirect(sina.getAuthorizeUrl());
        } catch (Exception e) {
            if (OjConfig.isDevMode())
                e.printStackTrace();
            log.error(e.getLocalizedMessage());
            redirect("/");
        }
    }

    @RequiresGuest
    public void callback() {
        try {
            SinaOauth sina = new SinaOauth();
            Map<String, String> userInfo = sina.getUserInfoByCode(getPara("code"));
            String type = "Sina";
            String openid = userInfo.get("openid");
            String nickname = userInfo.get("name");
            String avatar = userInfo.get("avatar_hd");
            WebLoginModel webLogin = WebLoginModel.dao.findByOpenID(openid, type);

            if (null == webLogin) {
                webLogin = new WebLoginModel();
                webLogin.setOpenId(openid);
                webLogin.setType(type);
                webLogin.setAvatar(avatar);
                webLogin.setCtime(OjConfig.timeStamp);
                webLogin.setStatus(false);
                webLogin.setNick(nickname).save();
            }

            boolean status = webLogin.getStatus();
            if (null != webLogin.getId() && !status) {
                if (null == webLogin.getUid()) {
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
                if (!userService.autoLogin(userModel, false)) {
                    setFlashMessage(new FlashMessage("Auto login failed, please inform admin!", MessageType.ERROR,
                        getText("message.error.title")));
                } else {
                    setCookie("auth_key", String.valueOf(userModel.getUid()), OjConstants.COOKIE_AGE);
                    setCookie("oj_username", userModel.getName(), OjConstants.COOKIE_AGE);
                    if (StringUtil.isNotBlank(avatar)) {
                        setCookie("oj_userimg", avatar, OjConstants.COOKIE_AGE);
                    }
                }
            }
        } catch (Exception e) {
            if (OjConfig.isDevMode())
                e.printStackTrace();
            log.error(e.getLocalizedMessage());
            setFlashMessage(
                new FlashMessage(getText("user.signin.error"), MessageType.ERROR, getText("message.error.title")));
        }
        redirect(SessionService.me().getLastAccessURL());
    }

}
