package com.power.oj.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import magick.MagickException;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.service.OjService;
import com.power.oj.image.ImageInfo;
import com.power.oj.image.ImageScaleImpl;
import com.power.oj.image.MagickImageScale;
import com.power.oj.util.FileKit;

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
    String rootPath = PathKit.getWebRootPath() + File.separator;
    String fileName = uploadFile.getFile().getAbsolutePath().replace(rootPath, "");
    int width = 0;
    int height = 0;

    setAttr("error", "true");
    try
    {
      ImageInfo info = new ImageInfo(uploadFile.getFile());
      
      setAttr("error", "false");
      setAttr("src", fileName);
      if (info.check())
      {
        width = info.getWidth();
        height = info.getHeight();
      }
      info.close();
    } catch (FileNotFoundException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    setAttr("width", width);
    setAttr("height", height);
    renderJson(new String[]{"error", "src", "width", "height"});
  }
  
  public void cutAvatar()
  {
    String rootPath = PathKit.getWebRootPath() + File.separator;
    String fileName = rootPath + getPara("imageSource");
    File srcFile = new File(fileName);
    File destFile = new File(new StringBuilder(3).append(OjConfig.uploadPath).append(File.separator).append(FileKit.getNewName(srcFile.getName())).toString());
    int imageX = getParaToInt("imageX");
    int imageY = getParaToInt("imageY");
    int boxWidth = getParaToInt("imageW");
    int boxHeight = getParaToInt("imageH");
    int cutTop = getParaToInt("selectorY");
    int cutLeft = getParaToInt("selectorX");
    int cutWidth = getParaToInt("selectorW");
    int catHeight = getParaToInt("selectorH");
    ImageScaleImpl imageScale = new ImageScaleImpl();
    
    try
    {
      log.info(String.valueOf(imageX+cutTop+catHeight));
      imageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight, imageX+cutTop, imageY+cutLeft, cutWidth, catHeight);
      fileName = destFile.getAbsolutePath().replace(rootPath, "");
    } catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    renderJson(fileName);
  }
  
}
