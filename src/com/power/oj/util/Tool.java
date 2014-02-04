package com.power.oj.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

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

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

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
   * Get the html content by url.
   * 
   * @param url
   *          the string of url.
   * @return the string of html content.
   */
  public static String getHtmlByUrl(String url)
  {
    String html = null;

    HttpClient httpclient = new DefaultHttpClient();
    try
    {
      HttpGet httpget = new HttpGet(url);
      log.info("executing request " + httpget.getURI());

      // Create a response handler
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
      html = httpclient.execute(httpget, responseHandler);

    } catch (ClientProtocolException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
    } finally
    {
      // When HttpClient instance is no longer needed,
      // shut down the connection manager to ensure
      // immediate deallocation of all system resources
      httpclient.getConnectionManager().shutdown();
    }

    return html;
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
  public static void sendEmail(String from, String to, String subject, EmailMessage content) throws Exception
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
    } catch (MailException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
      throw new Exception("Configuration for SMTP mail is incorrect.");
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

}
