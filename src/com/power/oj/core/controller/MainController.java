package com.power.oj.core.controller;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.ext.render.CaptchaRender;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;

/**
 * The controller for some common pages.
 * 
 * @author power
 * 
 */
public class MainController extends OjController
{
  /**
   * The index page.
   */
  public void index()
  {
    setAttr("problemsNumber", problemService.getProblemsNumber());
    setTitle(getText("page.index.title"));
  }

  /**
   * The about page.
   */
  public void about()
  {
    setTitle(getText("page.about.title"));
  }

  /**
   * contact me page.
   */
  public void contact()
  {
    setTitle(getText("page.contact.title"));
  }

  /**
   * OJ changelog page.
   */
  public void changelog()
  {
    setTitle(getText("page.changelog.title"));
  }

  /**
   * Frequently Asked Questions page.
   */
  public void faq()
  {
    setTitle(getText("page.faq.title"));
  }

  /**
   * Show hot tags of the problems.
   */
  public void tag()
  {
    setAttr("tagList", ojService.tagList());
    setTitle(getText("page.tag.title"));
  }

  /**
   * Generate captcha image.
   */
  @ClearInterceptor(ClearLayer.ALL)
  public void captcha()
  {
    CaptchaRender img = new CaptchaRender(OjConstants.RANDOM_CODE_KEY);
    render(img);
  }

}
