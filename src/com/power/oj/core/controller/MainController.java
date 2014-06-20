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

  public void index()
  {
    setAttr("problemsNumber", problemService.getProblemsNumber());
    setTitle(getText("page.index.title"));
  }

  public void about()
  {
    setTitle(getText("page.about.title"));
  }

  public void contact()
  {
    setTitle(getText("page.contact.title"));
  }

  public void changelog()
  {
    setTitle(getText("page.changelog.title"));
  }

  public void faq()
  {
    setTitle(getText("page.faq.title"));
  }

  public void tag()
  {
    setAttr("tagList", ojService.tagList());
    setTitle(getText("page.tag.title"));
  }

  @ClearInterceptor(ClearLayer.ALL)
  public void captcha()
  {
    CaptchaRender img = new CaptchaRender(OjConstants.RANDOM_CODE_KEY);
    render(img);
  }

}
