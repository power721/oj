package com.power.oj.core.bean;

import java.io.File;
import java.io.IOException;

import org.dom4j.Element;

import jodd.io.FileUtil;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;

import sun.misc.BASE64Decoder;

public class FpsImage
{
  private final Logger log = Logger.getLogger(FpsImage.class);

  private String originalSrc;
  private String src;
  private String base64;
  private File imageFile;

  public FpsImage(Element e, FpsProblem p, int num)
  {
    Integer pid = p.getProblemModel().getPid();
    
    originalSrc = e.elementText("src");
    base64 = e.elementText("base64");
    StringBuilder sb = new StringBuilder(6).append(OjConfig.problemImagePath).append(File.separator);
    sb.append(pid).append("_").append(num).append(".png");
    imageFile = new File(sb.toString());
    src = imageFile.getAbsolutePath().replaceAll(PathKit.getWebRootPath(), "").substring(1);
  }

  public void saveImage()
  {
    try
    {
      byte[] decodeBuffer = (new BASE64Decoder()).decodeBuffer(base64);
      FileUtil.touch(imageFile);
      FileUtil.writeBytes(imageFile, decodeBuffer);
    } catch (IOException e1)
    {
      if (OjConfig.getDevMode())
        e1.printStackTrace();
      log.error(e1.getLocalizedMessage());
    }
  }
  
  public String getOldURL()
  {
    return originalSrc;
  }

  public void setOldURL(String oldURL)
  {
    this.originalSrc = oldURL;
  }

  public String getURL()
  {
    return src;
  }

  public void setURL(String uRL)
  {
    src = uRL;
  }
  
}
