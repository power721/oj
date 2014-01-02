package com.power.oj.core.service;

import java.util.List;

import jodd.mail.EmailMessage;
import jodd.util.MimeTypes;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import com.power.oj.core.OjConfig;
import com.power.oj.util.Tool;

public class OjService
{
  private static final Logger log = Logger.getLogger(OjService.class);
  private static final OjService me = new OjService();
  
  private OjService()
  {
    
  }
  
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

  public boolean sendResetPasswordEmail(String name, String email, String token) throws Exception
  {
    String adminEmail = OjConfig.get("adminEmail");
    if (adminEmail == null)
      throw new Exception("Admin Email not set!");

    String resetUrl = new StringBuilder(7).append(OjConfig.baseUrl).append( "/user/reset?name=").append(name).append("&token=").append(token).append("&t=").append(OjConfig.timeStamp).toString();
    EmailMessage htmlMessage = new EmailMessage(
        "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">" +
        "<body><h2>Reset your account!</h2><br><div><p>" +
        "To reset your password, click on the link below (or copy and paste the URL into your browser):<br>" +
        "<a href=\"" + resetUrl + "\" target=\"blank\">" + resetUrl + "</a></p></div></body></html>",
        MimeTypes.MIME_TEXT_HTML);
    
    Tool.sendEmail(adminEmail, email, "Reset PowerOJ account!", htmlMessage);
    
    log.info("Account recovery email send to user " + name);
    return true;
  }

  public List<Record> getUserRoles(int uid)
  {
    String sql = "SELECT r.name AS role, r.id AS rid FROM roles r LEFT JOIN user_role ur ON ur.rid = r.id WHERE ur.uid = ?";
    List<Record> roleList = Db.find(sql, uid);
    
    return roleList;
  }
  
  public List<Record> getRolePermission(int rid)
  {
    String sql = "SELECT p.name AS permission FROM permission p LEFT JOIN role_permission rp ON rp.pid = p.id WHERE rp.rid = ?";
    List<Record> permissionList = Db.find(sql,rid);
    
    return permissionList;
  }
  
  public List<Record> tagList()
  {
    return Db.find("SELECT tag FROM tag WHERE status=1 GROUP by tag ORDER BY COUNT(tag) DESC");
  }
}
