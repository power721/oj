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

  public FpsImage(Element e, FpsProblem p, int num)
  {
    Integer pid = p.getProblemModel().getPid();
    
    originalSrc = e.elementText("src");
    String base64 = e.elementText("base64");
    StringBuilder sb = new StringBuilder(6).append(OjConfig.problemImagePath).append(File.separator);
    sb.append(pid).append("_").append(num).append(".png");
    File imageFile = new File(sb.toString());
    src = imageFile.getAbsolutePath().replace(PathKit.getWebRootPath(), "").substring(1);
    
    try
    {
      byte[] decodeBuffer = new BASE64Decoder().decodeBuffer(base64);
      FileUtil.touch(imageFile);
      FileUtil.writeBytes(imageFile, decodeBuffer);
    } catch (IOException e1)
    {
      if (OjConfig.isDevMode())
        e1.printStackTrace();
      log.error(e1.getLocalizedMessage());
    }
  }

  public String getOriginalSrc()
  {
    return originalSrc;
  }

  public void setOriginalSrc(String originalSrc)
  {
    this.originalSrc = originalSrc;
  }

  public String getSrc()
  {
    return src;
  }

  public void setSrc(String src)
  {
    this.src = src;
  }
  
}
