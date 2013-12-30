package com.power.oj.util;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;

import java.io.IOException;

import jodd.mail.Email;
import jodd.mail.EmailMessage;
import jodd.mail.SendMailSession;
import jodd.mail.SimpleAuthenticator;
import jodd.mail.SmtpServer;
import jodd.util.MimeTypes;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

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
      log.warn(e.getLocalizedMessage());
    } catch (IOException e)
    {
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
   * Send Email.
   * 
   * @param from
   *          the email address of sender.
   * @param to
   *          the email address of receiver.
   * @param subject
   *          the title of email.
   * @param content
   *          string of email body.
   */
  public static void sendEmail(String from, String to, String subject, String content)
  {
    EmailMessage textMessage = new EmailMessage(content, MimeTypes.MIME_TEXT_PLAIN);
    sendEmail(from, to, subject, textMessage);
  }

  /**
   * Send Email.
   * 
   * @param from
   *          the email address of sender.
   * @param to
   *          the email address of receiver.
   * @param subject
   *          the title of email.
   * @param content
   *          EmailMessage of email body.
   */
  public static void sendEmail(String from, String to, String subject, EmailMessage content)
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
    finally
    {
      session.close();
    }
  }

}
