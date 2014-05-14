package com.power.oj.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.mail.EmailMessage;
import jodd.util.MimeTypes;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.util.Tool;
import com.power.oj.util.freemarker.FreemarkerKit;

public final class OjService
{
  private static final Logger log = Logger.getLogger(OjService.class);
  private static final OjService me = new OjService();
  
  private OjService() {}
  
  public static OjService me()
  {
    return me;
  }
  
  public void loadVariable()
  {
    OjConfig.loadVariable();
  }
  
  public void loadLanguage()
  {
    OjConfig.loadLanguage();
  }
  
  public void initJudgeResult()
  {
    OjConfig.initJudgeResult();
  }
  
  public boolean checkEmailConf()
  {
    if (OjConfig.getString("adminEmail") == null)
    {
      return false;
    }
    
    String emailServer = OjConfig.getString("emailServer");
    String emailUser = OjConfig.getString("emailUser");
    String emailPass = OjConfig.getString("emailPass");
    if (emailServer == null || emailUser == null || emailPass == null)
    {
      return false;
    }
    return true;
  }

  public boolean sendVerifyEmail(String name, String email, String token)
  {
    Map<String, Object> paras = new HashMap<String, Object>();
    paras.put(OjConstants.BASE_URL, OjConfig.baseUrl);
    paras.put(OjConstants.SITE_TITLE, OjConfig.siteTitle);
    paras.put("name", name);
    paras.put("token", token);
    paras.put("ctime", OjConfig.timeStamp);
    paras.put("expires", OjConstants.VERIFY_EMAIL_EXPIRES_TIME / OjConstants.MINUTE_IN_MILLISECONDS);

    sendVerifyEmail(name, email, paras);
    
    log.info("Account recovery email send to user " + name);
    return true;
  }

  public boolean sendVerifyEmail(String name, final String email, Map<String, Object> paras)
  {
    final String adminEmail = OjConfig.getString("adminEmail");
    if (adminEmail == null)
    {
      return false;
    }
    
    String html = FreemarkerKit.processString("tpl/verifyEmail.html", paras);
    final EmailMessage htmlMessage = new EmailMessage(html, MimeTypes.MIME_TEXT_HTML);
    
    new Thread(new Runnable() {
      @Override
      public void run()
      {
        Tool.sendEmail(adminEmail, email, "Confirm PowerOJ account!", htmlMessage);
      }}).start();
    
    log.info("Account recovery email send to user " + name);
    return true;
  }

  public boolean sendResetPasswordEmail(String name, final String email, String token)
  {
    final String adminEmail = OjConfig.getString("adminEmail");
    
    Map<String, Object> paras = new HashMap<String, Object>();
    paras.put(OjConstants.BASE_URL, OjConfig.baseUrl);
    paras.put(OjConstants.SITE_TITLE, OjConfig.siteTitle);
    paras.put("name", name);
    paras.put("token", token);
    paras.put("ctime", OjConfig.timeStamp);
    paras.put("expires", OjConstants.RESET_PASSWORD_EXPIRES_TIME / OjConstants.MINUTE_IN_MILLISECONDS);
    
    String html = FreemarkerKit.processString("/tpl/resetEmail.html", paras);
    final EmailMessage htmlMessage = new EmailMessage(html, MimeTypes.MIME_TEXT_HTML);
    
    new Thread(new Runnable() {
      @Override
      public void run()
      {
        Tool.sendEmail(adminEmail, email, "Reset PowerOJ account!", htmlMessage);
      }}).start();
    
    log.info("Account recovery email send to user " + name);
    return true;
  }

  public List<Record> getUserRoles(int uid)
  {
    String sql = "SELECT r.name AS role, r.id AS rid FROM role r LEFT JOIN user_role ur ON ur.rid = r.id WHERE ur.uid = ?";
    List<Record> roleList = Db.find(sql, uid);
    
    return roleList;
  }

  public List<Record> getRoleList()
  {
    return Db.find("SELECT * FROM role ORDER BY id");
  }
  
  public List<Record> getRolePermission(int rid)
  {
    String sql = "SELECT p.name AS permission FROM permission p LEFT JOIN role_permission rp ON rp.pid = p.id WHERE rp.rid = ?";
    List<Record> permissionList = Db.find(sql, rid);
    
    return permissionList;
  }

  public List<Record> getPermissionList()
  {
    return Db.find("SELECT * FROM permission ORDER BY id");
  }
  
  public List<Record> tagList()
  {
    return Db.find("SELECT tag FROM tag WHERE status=1 GROUP by tag ORDER BY COUNT(tag) DESC");
  }
}
