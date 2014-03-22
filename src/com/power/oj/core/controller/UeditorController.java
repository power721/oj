package com.power.oj.core.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jodd.io.FileUtil;
import jodd.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import sun.misc.BASE64Decoder;

import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.util.FileKit;

public class UeditorController extends OjController
{
  @RequiresPermissions("admin:upload")
  public void upload()
  {
    // UploadFile file = getFile("Filedata", "", 10*1024*1024, "UTF-8");

    renderText("TODO");
  }

  /*
   * These bellow actions are used for UEditor.
   */
  @RequiresPermissions("image:upload")
  public void uploadImage()
  {
    UploadFile file = getFile("upfile", "", 10 * 1024 * 1024, "UTF-8");

    String rootPath = PathKit.getWebRootPath() + File.separator;
    String originalName = file.getOriginalFileName();
    String title = getPara("pictitle");
    String state = "SUCCESS";
    String url = file.getFile().getAbsolutePath().replace(rootPath, "").replace("\\", "/");
    System.out.println("originalName: " + originalName);
    System.out.println("file: " + file.getFileName());
    System.out.println(file.getFile().getAbsolutePath());

    renderJson("{'original':'" + originalName + "','url':'" + url + "','title':'" + title + "','state':'" + state + "'}");
  }
  
  @RequiresPermissions("image:upload")
  public void uploadProblemImage()
  {
    UploadFile file = getFile("upfile", "", 10 * 1024 * 1024, "UTF-8");

    String rootPath = PathKit.getWebRootPath() + File.separator;
    String originalName = file.getOriginalFileName();
    String title = getPara("pictitle");
    String state = "SUCCESS";
    String url = file.getFile().getAbsolutePath().replace(rootPath, "").replace("\\", "/");
    System.out.println("originalName: " + originalName);
    System.out.println("file: " + file.getFileName());
    System.out.println(file.getFile().getAbsolutePath());

    String fileName = new StringBuilder(3).append(OjConfig.problemImagePath).append(File.separator).append(originalName).toString();
    File imageFile = new File(fileName);
    log.info(fileName);
    try
    {
      FileUtil.moveFile(file.getFile(), imageFile);
      url = imageFile.getAbsolutePath().replace(rootPath, "").replace("\\", "/");
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
      state = "IO Exception";
    }

    renderJson("{'original':'" + originalName + "','url':'" + url + "','title':'" + title + "','state':'" + state + "'}");
  }

  @RequiresPermissions("image:upload")
  public void uploadScrawl()
  {
    String action = getPara("action");

    if (action != null && action.equals("tmpImg"))
    {
      UploadFile file = getFile("upfile", "", 10 * 1024 * 1024, "UTF-8");
      renderHtml("<script>parent.ue_callback('" + file.getFileName() + "','SUCCESS')</script>");
      return;
    }

    String state = "SUCCESS";
    String base64Data = getPara("content");
    BASE64Decoder decoder = new BASE64Decoder();
    File outFile = new File(new StringBuilder(3).append(OjConfig.uploadPath).append(File.separator).append(FileKit.getNewName("test.png")).toString());
    OutputStream ro;
    try
    {
      ro = new FileOutputStream(outFile);
      byte[] b = decoder.decodeBuffer(base64Data);
      for (int i = 0; i < b.length; ++i)
      {
        if (b[i] < 0)
        {
          b[i] += 256;
        }
      }
      ro.write(b);
      ro.flush();
      ro.close();
    } catch (Exception e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.warn(e.getLocalizedMessage());
      state = "IO Exception";
    }
    renderJson("{'url':'" + outFile.getName() + "',state:'" + state + "'}");
  }

  @RequiresPermissions("image:upload")
  public void remoteImage()
  {
    String url = getPara("upfile");
    String state = "SUCCESS";
    String[] arr = url.split("ue_separate_ue");
    String[] outSrc = new String[arr.length];

    for (int i = 0; i < arr.length; i++)
    {

      // 保存文件路径
      String savePath = OjConfig.uploadPath;
      // 格式验证
      String type = FileKit.getImageType(arr[i]);
      if (type.equals(""))
      {
        state = "图片类型不正确！";
        continue;
      }
      String saveName = Long.toString(new Date().getTime()) + type;
      // 大小验证
      HttpURLConnection.setFollowRedirects(false);
      try
      {
        HttpURLConnection conn = (HttpURLConnection) new URL(arr[i]).openConnection();
        if (conn.getContentType().indexOf("image") == -1)
        {
          state = "请求地址头不正确";
          continue;
        }
        if (conn.getResponseCode() != 200)
        {
          state = "请求地址不存在！";
          continue;
        }
        File dir = new File(savePath);
        if (!dir.exists())
        {
          dir.mkdirs();
        }
        File savetoFile = new File(savePath + File.separator + saveName);
        outSrc[i] = saveName;

        InputStream is = conn.getInputStream();
        OutputStream os = new FileOutputStream(savetoFile);
        int b;
        while ((b = is.read()) != -1)
        {
          os.write(b);
        }
        os.close();
        is.close();
        // 这里处理 inputStream
      } catch (Exception e)
      {
        e.printStackTrace();
        System.err.println("页面无法访问");
      }
    }

    String outstr = "";
    for (int i = 0; i < outSrc.length; i++)
    {
      outstr += outSrc[i] + "ue_separate_ue";
    }
    outstr = outstr.substring(0, outstr.lastIndexOf("ue_separate_ue"));
    renderJson("{'url':'" + outstr + "','tip':'" + state + "','srcUrl':'" + url + "'}");
  }

  @RequiresPermissions("image:admin")
  public void imageManager()
  {
    String imgStr = "";
    String rootPath = PathKit.getWebRootPath() + File.separator;
    String imagesDir = OjConfig.problemImagePath;
    List<File> files = FileKit.getImageFiles(imagesDir, new ArrayList<File>());
    
    for (File file : files)
    {
      imgStr += file.getPath().replace(rootPath, "") + "ue_separate_ue";
    }
    
    imagesDir = OjConfig.uploadPath;
    files = FileKit.getImageFiles(imagesDir, new ArrayList<File>());
    
    for (File file : files)
    {
      imgStr += file.getPath().replace(rootPath, "") + "ue_separate_ue";
    }
    
    if (StringUtil.isNotBlank(imgStr))
    {
      imgStr = imgStr.substring(0, imgStr.lastIndexOf("ue_separate_ue")).replace(File.separator, "/").trim();
    }
    
    renderText(imgStr);
  }

}
