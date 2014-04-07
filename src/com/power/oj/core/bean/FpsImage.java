package com.power.oj.core.bean;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;

import sun.misc.BASE64Decoder;

public class FpsImage
{
  private final Logger log = Logger.getLogger(FpsImage.class);

  private String originalSrc;
  private String src;

  public FpsImage(Node e, FpsProblem p, int num)
  {
    Integer pid = p.getProblemModel().getPid();
    NodeList cn = e.getChildNodes();
    originalSrc = cn.item(0).getTextContent();
    String base64 = cn.item(1).getTextContent();
    StringBuilder sb = new StringBuilder(6).append(OjConfig.problemImagePath).append(File.separator);
    sb.append(pid).append("_").append(num).append(".png");
    File imageFile = new File(sb.toString());
    src = imageFile.getAbsolutePath().replaceAll(PathKit.getWebRootPath(), "").substring(1);

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
