package com.power.oj.core;

import javax.servlet.http.Cookie;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.bean.Message;

/**
 * Base Controller
 * 
 * @author power
 * 
 */
public class OjController extends Controller
{
  protected final Logger log = Logger.getLogger(getClass());

  /**
   * Redirect to url with flash message。
   * 
   * @param url
   *          string of destination url
   * @param msg
   *          message with content, type and title
   */
  public void redirect(String url, Message msg)
  {
    setMessage(msg);
    super.redirect(url);
  }

  /**
   * Redirect to url with succes message。
   * 
   * @param url
   *          string of destination url
   * @param withQueryString
   *          whether contains query string
   * @param msg
   *          message with content, type and title
   */
  public void redirect(String url, boolean withQueryString, Message msg)
  {
    setMessage(msg);
    super.redirect(url, withQueryString);
  }

  public void setMessage(Message message)
  {
    setSessionAttr(OjConstants.MSG, message.getContent());
    setSessionAttr(OjConstants.MSG_TYPE, message.getType());
    setSessionAttr(OjConstants.MSG_TITLE, message.getTitle());
  }

  /**
   * The the page title of the view.
   * 
   * @param title
   *          string of the page title.
   */
  public void setTitle(String title)
  {
    setAttr(OjConstants.PAGE_TITLE, title);
  }

  /**
   * Set the cookie in safe way.
   * 
   * @param name
   * @param value
   * @param maxAgeInSeconds
   * @return this OjController.
   */
  public OjController setCookieHttpOnly(String name, String value, int maxAgeInSeconds)
  {
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(maxAgeInSeconds);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    setCookie(cookie);
    return this;
  }

  /**
   * Return a Object from session.
   * 
   * @param key
   *          a String specifying the key of the Object stored in session
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getSessionAttr(String key)
  {
    Session session = SecurityUtils.getSubject().getSession(false);
    return session != null ? (T) session.getAttribute(key) : null;
  }

  /**
   * Store Object to session.
   * 
   * @param key
   *          a String specifying the key of the Object stored in session
   * @param value
   *          a Object specifying the value stored in session
   */
  @Override
  public Controller setSessionAttr(String key, Object value)
  {
    SecurityUtils.getSubject().getSession().setAttribute(key, value);
    return this;
  }

  /**
   * Remove Object in session.
   * 
   * @param key
   *          a String specifying the key of the Object stored in session
   */
  @Override
  public Controller removeSessionAttr(String key)
  {
    Session session = SecurityUtils.getSubject().getSession(false);
    if (session != null)
      session.removeAttribute(key);
    return this;
  }

  /**
   * Get the Logger object.
   * 
   * @return log
   */
  public Logger getLog()
  {
    return log;
  }
}
