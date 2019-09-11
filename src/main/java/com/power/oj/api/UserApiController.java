package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.plugin.shiro.ClearShiro;
import com.jfinal.upload.UploadFile;
import com.power.oj.api.oauth.WebLoginModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserExtModel;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import jodd.mail.MailException;
import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresGuest;

import java.io.File;

@Before(CheckGuestInterceptor.class)
public class UserApiController extends OjController {
    public void splash() {
        UserModel userModel = userService.getCurrentUserExt();

        userModel.put("unReadMail", mailService.countUserNewMails(userModel.getUid()));

        setAttr(OjConstants.USER, userModel);

        render("ajax/splash.html");
    }

    @Clear
    @RequiresGuest
    public void bind() {
        String email = getPara("email");
        Integer webId = getParaToInt("id");
        WebLoginModel webLogin = WebLoginModel.dao.findById(webId);

        if (webLogin == null) {
            renderJson("status", "error");
            return;
        }

        UserModel userModel = userService.getUserByEmail(email);
        if (userModel == null) {
            try {
                userModel = userService.signup(email, webLogin);
            } catch (MailException e) {
                log.error("bind user failed!", e);

                renderJson("status", "mail_error");
                return;
            } catch (Exception e) {
                log.error("bind user failed!", e);

                renderJson("status", "signup_error");
                return;
            }
        } else {
            renderJson("status", "mail_exist");
            return;
        }

        webLogin.setUid(userModel.getUid());
        webLogin.setStatus(true); // change after email verified?
        if (webLogin.update()) {
            userService.autoLogin(userModel, false);

            setCookie("auth_key", String.valueOf(userModel.getUid()), OjConstants.COOKIE_AGE);
            setCookie("oj_username", userModel.getName(), OjConstants.COOKIE_AGE);
            String avatar = userModel.getAvatar();
            if (StringUtil.isNotBlank(avatar)) {
                setCookie("oj_userimg", avatar, OjConstants.COOKIE_AGE);
            }
            renderJson("status", "ok");
            return;
        }
        renderJson("status", "error");
    }

    public void profile() {
        UserModel userModel = userService.getCurrentUser();
        if (userModel == null) {
            renderJson("success", false);
        } else {
            userModel.put("success", true);
            userModel.remove("token").remove("password").remove("data");
            renderJson(userModel);
        }
    }

    public void full() {
        UserModel userModel = null;

        if (isParaExists("uid")) {
            userModel = userService.getUserByUid(getParaToInt("uid"));
        } else if (isParaExists("name")) {
            userModel = userService.getUserByName(getPara("name"));
        }

        if (userModel == null) {
            renderJson("success", false);
        } else {
            userModel.put("success", true);
            userModel.remove("token").remove("password").remove("data");
            renderJson(userModel);
        }
    }

    @Clear
    public void info() {
        UserModel userModel = null;

        if (isParaExists("uid")) {
            userModel = userService.getUserInfoByUid(getParaToInt("uid"));
        } else if (isParaExists("name")) {
            userModel = userService.getUserInfoByName(getPara("name"));
        }

        if (userModel == null || (!userService.isAdmin() && !userModel.getUid().equals(UserService.me().getCurrentUid()))) {
            renderJson("success", false);
        } else {
            userModel.put("success", true);
            // TODO check user sensitive information
            userModel.remove("token").remove("password").remove("data");
            if (!userService.isAdmin()) {
                userModel.remove("realName").remove("phone");
            }
            renderJson(userModel);
        }
    }

    public void checkin() {
        int incExp = 0;
        Integer uid = userService.getCurrentUid();
        UserExtModel userModel = UserExtModel.dao.findById(uid);
        if ((incExp = userService.checkin(userModel)) > 0) {
            renderJson("{\"success\":true, \"incexp\":" + incExp + ",\"result\":" + userModel.getCheckinTimes() + "}");
        } else {
            renderJson("{\"success\":false, \"result\":\"User already checkin.\"}");
        }
    }

    @Clear
    public void online() {
        // TODO
        Integer uid = getParaToInt("uid");
        String name = getPara("name");
        try {
            sessionService.updateOnline(uid, name);
        } catch (NullPointerException e) {
            renderJson("{\"success\":false}");
        }

        renderNull();
    }

    @Before(POST.class)
    public void signSubmit() {
        String sign = HtmlEncoder.text(getPara("sign", "").trim());
        UserModel userModel = userService.getCurrentUser();
        if (userModel.setSignature(sign).update()) {
            renderJson("{\"success\":true}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Update sign failed.\"}");
        }
    }

    @Before(POST.class)
    public void pwdSubmit() {
        UserModel userModel = userService.getCurrentUser();
        String origPwd = getPara("origPwd");
        String newPwd = getPara("newPwd");

        if (userService.checkPassword(userModel.getUid(), origPwd)) {
            newPwd = BCrypt.hashpw(newPwd, BCrypt.gensalt());
            if (userModel.setPassword(newPwd).update()) {
                renderJson("{\"success\":true}");
            } else {
                renderJson("{\"success\":false, \"result\":\"Update password failed.\"}");
            }
        } else {
            renderJson("{\"success\":false, \"result\":\"Password is incorrect.\"}");
        }
    }

    public void emailSubmit() {
        UserModel userModel = userService.getCurrentUser();
        Integer uid = userModel.getUid();
        String origPwd = getPara("origPwd");
        String newEmail = getPara("newEmail");
        // TODO: validate Email

        if (userService.checkPassword(uid, origPwd)) {
            if (userService.containsEmailExceptThis(uid, newEmail)) {
                renderJson("{\"success\":false, \"result\":\"This email already used by other.\"}");
                return;
            }

            try {
                if (userService.updateEmail(userModel, newEmail)) {
                    renderJson("{\"success\":true}");
                } else {
                    renderJson("{\"success\":false, \"result\":\"Update email failed.\"}");
                }
            } catch (Exception e) {
                log.error("update email failed!", e);

                renderJson("{\"success\":false, \"result\":\"Send verify email failed.\"}");
            }
        } else {
            renderJson("{\"success\":false, \"result\":\"Password is incorrect.\"}");
        }
    }

    @Before(POST.class)
    public void profileSubmit() {
        UserModel userModel = userService.getCurrentUser();
        userModel.setComeFrom(HtmlEncoder.text(getPara("comeFrom")));
        userModel.setQQ(HtmlEncoder.text(getPara("qq")));
        userModel.setBlog(HtmlEncoder.text(getPara("blog")));
        userModel.setPhone(HtmlEncoder.text(getPara("phone")));
        userModel.setRealName(HtmlEncoder.text(getPara("realName")));
        userModel.setGender(HtmlEncoder.text(getPara("gender"))); // TODO check gender value
        userModel.setSchool(HtmlEncoder.text(getPara("school")));

        if (userModel.update()) {
            renderJson("{\"success\":true}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Update sign failed.\"}");
        }
    }

    @Before(POST.class)
    public void uploadAvatar() {
        UploadFile uploadFile = getFile("Filedata", "", 10 * 1024 * 1024, "UTF-8");
        File file = uploadFile.getFile();
        String fileName = null;

        try {
            fileName = userService.saveAvatar(file);
            setCookie("oj_userimg", fileName, OjConstants.COOKIE_AGE);
            renderJson("{\"success\":true, \"state\":\"SUCCESS\", \"url\":\"" + fileName + "\"}");
        } catch (Exception e) {
            log.error("upload avatar failed!", e);
            renderJson("{\"success\":false, \"state\":\"FAIL\"}");
        }
    }

    @Before(POST.class)
    @Clear
    @ClearShiro
    public void signin() {
        if (ShiroKit.isAuthenticated()) {
            renderJson("{\"success\":false, \"result\":\"User already signined.\"}");
            return;
        }

        String username = getPara("name").trim();
        String password = getPara("password");
        boolean rememberMe = getParaToBoolean("rememberMe", false);

        if (userService.login(username, password, rememberMe)) {
            UserModel userModel = userService.getCurrentUser();
            String avatar = userModel.getAvatar();
            setCookie("auth_key", String.valueOf(userModel.getUid()), OjConstants.COOKIE_AGE);
            setCookie("oj_username", username, OjConstants.COOKIE_AGE);
            if (StringUtil.isNotBlank(avatar)) {
                setCookie("oj_userimg", avatar, OjConstants.COOKIE_AGE);
            }
            renderJson("{\"success\":true}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Invalid username or password.\"}");
        }
    }

    @Before(POST.class)
    @Clear
    @RequiresGuest
    public void signup() {
        UserModel userModel = getModel(UserModel.class, "user");

        if (userService.signup(userModel)) {
            setCookie("oj_username", userModel.getName(), OjConstants.COOKIE_AGE);
            renderJson("{\"success\":true}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Signup failed.\"}");
        }
    }

    @Clear
    public void logout() {
        userService.logout();
        removeCookie("auth_key");
        removeCookie("oj_userimg");

        renderNull();
    }

    public void getSubmissions() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("size", OjConfig.statusPageSize);
        Integer result = getParaToInt("resultId", -1);
        Integer language = getParaToInt("languageId", 0);
        Integer uid = userService.getCurrentUid();

        // TODO get language name
        renderJson(solutionService.getPage(pageNumber, pageSize, result, language, uid));
    }

    @Clear
    public void getUserBasic() {
        Integer uid = getParaToInt("uid");
        String name = getPara("name");

        UserModel userModel;
        if (uid != null) {
            userModel = userService.getUserBasic(uid);
        } else {
            userModel = userService.getUserBasic(name);
        }

        if (userModel != null) {
            renderJson(userModel);
        } else {
            renderNull();
        }
    }

    @Clear
    public void getField() {
        Integer uid = getParaToInt("uid");
        String name = getPara("name");

        renderJson("result", userService.getUserField(uid, name));
    }

    @Clear
    public void getUid() {
        renderJson("result", userService.getUidByName(getPara("name")));
    }

    @Clear
    public void isUserNameExist() {
        renderJson("result", userService.isUserNameExist(getPara("name")));
    }

    @Clear
    public void isEmailExist() {
        renderJson("result", userService.isEmailExist(getPara("email")));
    }

}
