package com.power.oj.util;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import jodd.mail.Email;
import jodd.mail.EmailMessage;
import jodd.mail.MailException;
import jodd.mail.SendMailSession;
import jodd.mail.SimpleAuthenticator;
import jodd.mail.SmtpServer;
import jodd.util.MimeTypes;
import jodd.util.StringTemplateParser;
import jodd.util.StringTemplateParser.MacroResolver;

import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;

/**
 * Utils for common usage.
 * 
 * @author power
 * 
 */
public class Tool
{
  private final static Logger log = Logger.getLogger(Tool.class);

  private static final char[] CHAR_STR =
  { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
      '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '=', '+', '<', '>', '/' };

  /**
   * 
   * @param url
   *          string of url
   * @return string of formatted base url
   */
  public static String formatBaseURL(String url)
  {
    while (url != null && url.endsWith("/"))
      url = url.substring(0, url.length() - 1);
    return url;
  }

  /**
   * Get the client IP address.
   * 
   * @param request
   *          the HttpServletRequest object.
   * @return the IP address.
   */
  public static String getRemoteAddr(HttpServletRequest request)
  {
    if (StringKit.notBlank(request.getHeader("X-Real-IP")))
    {
      return request.getHeader("X-Real-IP");
    } else if (StringKit.notBlank(request.getHeader("x-forwarded-for")))
    {
      return request.getHeader("x-forwarded-for");
    }

    return request.getRemoteAddr();
  }

  /**
   * Send Email with string content.
   * 
   * @param from
   *          the email address of sender.
   * @param to
   *          the email address of receiver.
   * @param subject
   *          the title of email.
   * @param content
   *          string of email body.
   * @throws Exception
   */
  public static void sendEmail(String from, String to, String subject, String content) throws Exception
  {
    EmailMessage textMessage = new EmailMessage(content, MimeTypes.MIME_TEXT_PLAIN);
    sendEmail(from, to, subject, textMessage);
  }

  /**
   * Send Email with EmailMessage content.
   * 
   * @param from
   *          the email address of sender.
   * @param to
   *          the email address of receiver.
   * @param subject
   *          the title of email.
   * @param content
   *          EmailMessage of email body.
   * @throws Exception
   */
  public static void sendEmail(String from, String to, String subject, EmailMessage content) throws MailException
  {
    // TODO: create new thread to send mail
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
    } finally
    {
      session.close();
    }
  }

  public static String parseStringTemplate(final String template, final Map<String, String> map)
  {
    StringTemplateParser stp = new StringTemplateParser();
    String result = stp.parse(template, new MacroResolver() {
      public String resolve(String macroName)
      {
        return map.get(macroName);
      }
    });

    return result;
  }

  public static int getDayTimestamp()
  {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return (int) (cal.getTimeInMillis() / 1000);
  }

  public static String randomPassword(int count)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++)
    {
      sb.append(CHAR_STR[new Random().nextInt(CHAR_STR.length)]);
    }
    return sb.toString();
  }

}
