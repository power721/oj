package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jodd.mail.Email;
import jodd.mail.EmailMessage;
import jodd.mail.MailException;
import jodd.mail.SendMailSession;
import jodd.mail.SimpleAuthenticator;
import jodd.mail.SmtpServer;
import jodd.util.MimeTypes;
import jodd.util.collection.IntHashMap;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.util.FileKit;

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
    OjConfig.variable = new HashMap<String, VariableModel>();
    for (VariableModel variableModel : VariableModel.dao.find("SELECT * FROM variable"))
    {
      OjConfig.variable.put(variableModel.getStr("name"), variableModel);
    }
    
    OjConfig.siteTitle = OjConfig.get("siteTitle", "Power OJ");

    String uploadPath = OjConfig.get("uploadPath", "upload/");
    OjConfig.uploadPath = FileKit.parsePath(uploadPath);

    String downloadPath = OjConfig.get("downloadPath", "download/");
    OjConfig.downloadPath = FileKit.parsePath(downloadPath);
    
    String userAvatarPath = OjConfig.get("userAvatarPath", "assets/images/user/");
    OjConfig.userAvatarPath = FileKit.parsePath(userAvatarPath);
    
    String problemImagePath = OjConfig.get("problemImagePath", "assets/images/problem/");
    OjConfig.problemImagePath = FileKit.parsePath(problemImagePath);
    
    OjConfig.contestPageSize = OjConfig.getInt("contestPageSize", 20);
    OjConfig.contestRankPageSize = OjConfig.getInt("contestRankPageSize", 50);
    OjConfig.problemPageSize = OjConfig.getInt("problemPageSize", 50);
    OjConfig.userPageSize = OjConfig.getInt("userPageSize", 20);
    OjConfig.statusPageSize = OjConfig.getInt("statusPageSize", 20);
  }
  
  public void loadLanguage()
  {
    OjConfig.language_type = new IntHashMap();
    OjConfig.language_name = new IntHashMap();
    OjConfig.program_languages = LanguageModel.dao.find("SELECT * FROM program_language WHERE status=1");
    for (LanguageModel Language : OjConfig.program_languages)
    {
      OjConfig.language_type.put(Language.getInt("id"), Language);
      OjConfig.language_name.put(Language.getInt("id"), Language.getStr("name"));
    }
  }
  
  public void initJudgeResult()
  {
    OjConfig.judge_result = new ArrayList<ResultType>();
    OjConfig.judge_result.add(new ResultType(ResultType.AC, "AC", "Accepted"));
    OjConfig.judge_result.add(new ResultType(ResultType.PE, "PE", "Presentation Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.TLE, "TLE", "Time Limit Exceed"));
    OjConfig.judge_result.add(new ResultType(ResultType.MLE, "MLE", "Memory Limit Exceed"));
    OjConfig.judge_result.add(new ResultType(ResultType.WA, "WA", "Wrong Answer"));
    OjConfig.judge_result.add(new ResultType(ResultType.RE, "RE", "Runtime Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.OLE, "OLE", "Output Limit Exceed"));
    OjConfig.judge_result.add(new ResultType(ResultType.CE, "CE", "Compile Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.SE, "SE", "System Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.VE, "VE", "Validate Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.Wait, "Wait", "Waiting"));

    OjConfig.result_type = new IntHashMap();
    for (Iterator<ResultType> it = OjConfig.judge_result.iterator(); it.hasNext();)
    {
      ResultType resultType = it.next();
      OjConfig.result_type.put(resultType.getId(), resultType);
    }
  }

  public void sendEmail(String from, String to, String subject, String content)
  {
    EmailMessage textMessage = new EmailMessage(content, MimeTypes.MIME_TEXT_PLAIN);
    sendEmail(from, to, subject, textMessage);
  }
  
  public void sendEmail(String from, String to, String subject, EmailMessage content)
  {
    Email email = new Email();

    email.setFrom(from);
    email.setTo(to);
    email.setSubject(subject);
    email.addMessage(content);
    
    String emailServer = OjConfig.get("emailServer");
    String emailUser = OjConfig.get("emailUser");
    String emailPass = OjConfig.get("emailPass");
    SmtpServer smtpServer = new SmtpServer(emailServer, new SimpleAuthenticator(emailUser, emailPass));
    
    SendMailSession session = smtpServer.createSession();
    try
    {
      session.open();
      session.sendMail(email);
      log.info("Send mail from: " + from + " to: " + to + " subject: " + subject);
    }
    catch(MailException e)
    {
      log.error(e.getLocalizedMessage());
    }
    finally
    {
      session.close();
    }
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
    
    sendEmail(adminEmail, email, "Reset PowerOJ account!", htmlMessage);
    
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
