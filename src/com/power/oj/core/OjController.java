package com.power.oj.core;

import javax.servlet.http.Cookie;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.interceptor.FlashMessageInterceptor;
import com.power.oj.user.UserInterceptor;
import com.power.oj.user.UserModel;

/**
 * Base Controller provides some common methods.
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
   * @see FlashMessageInterceptor
   * @param url
   *          string of destination url.
   * @param msg
   *          message with content, type and title.
   */
  protected void redirect(String url, FlashMessage msg)
  {
    setFlashMessage(msg);
    redirect(url);
  }

  /**
   * Redirect to url with flash message。
   * 
   * @see FlashMessageInterceptor
   * @param url
   *          string of destination url.
   * @param withQueryString
   *          whether contains query string.
   * @param msg
   *          message with content, type and title.
   */
  protected void redirect(String url, boolean withQueryString, FlashMessage msg)
  {
    setFlashMessage(msg);
    redirect(url, withQueryString);
  }

  /**
   * Set flash message in session.
   * 
   * @param message
   *          message with content, type and title.
   */
  protected void setFlashMessage(FlashMessage message)
  {
    setSessionAttr(OjConstants.MSG, message.getContent());
    setSessionAttr(OjConstants.MSG_TYPE, message.getType());
    setSessionAttr(OjConstants.MSG_TITLE, message.getTitle());
  }

  /**
   * Set message in attr.
   * 
   * @param message
   *          message with content, type and title.
   */
  protected void setAttrMessage(FlashMessage message)
  {
    setAttr(OjConstants.MSG, message.getContent());
    setAttr(OjConstants.MSG_TYPE, message.getType());
    setAttr(OjConstants.MSG_TITLE, message.getTitle());
  }

  /**
   * The the page title of the view.
   * 
   * @param title
   *          string of the page title.
   */
  protected void setTitle(String title)
  {
    setAttr(OjConstants.PAGE_TITLE, title);
  }

  /**
   * Get current user from attribute.
   * 
   * @see UserInterceptor
   * @return UserModel.
   */
  protected UserModel getCurrentUser()
  {
    return getAttr(OjConstants.USER);
  }

  /**
   * Set the cookie in safe way.
   * 
   * @param name
   * @param value
   * @param maxAgeInSeconds
   * @return this OjController.
   */
  protected OjController setCookieHttpOnly(String name, String value, int maxAgeInSeconds)
  {
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(maxAgeInSeconds);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    setCookie(cookie);
    return this;
  }

}
