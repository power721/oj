package com.power.oj.core.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.service.OjService;
import com.power.oj.image.ImageScaleImpl;

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
    setAttr("tagList", OjService.me().tagList());
    setTitle(getText("page.tag.title"));
  }

  /**
   * Generate captcha image.
   */
  @ClearInterceptor(ClearLayer.ALL)
  public void captcha()
  {
    CaptchaRender img = new CaptchaRender(OjConstants.randomCodeKey);
    render(img);
  }

  @Before(POST.class)
  @RequiresPermissions("user:upload:avatar")
  public void uploadAvatar()
  {
    UploadFile uploadFile = getFile("Filedata", "", 10 * 1024 * 1024, "UTF-8");
    File file = uploadFile.getFile();
    String rootPath = PathKit.getWebRootPath() + File.separator;
    String fileName = file.getAbsolutePath().replace(rootPath, "");
    ImageScaleImpl imageScale = new ImageScaleImpl();
    int width = 400;
    int height = 400;

    log.info(uploadFile.getFile().getAbsolutePath());
    setAttr("error", "true");
    try
    {
      imageScale.resizeFix(file, file, width, height);
      BufferedImage srcImgBuff = ImageIO.read(uploadFile.getFile());
      width = srcImgBuff.getWidth();
      height = srcImgBuff.getHeight();
      setAttr("error", "false");
      setAttr("src", fileName);
    } catch (IOException e)
    {
      log.error(e.getLocalizedMessage());
    } catch (Exception e1)
    {
      log.error(e1.getLocalizedMessage());
    }

    setAttr("width", width);
    setAttr("height", height);
    renderJson(new String[]
    { "error", "src", "width", "height" });
  }

}
