package com.power.oj.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.AppConfig;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.FpsProblem;
import com.power.oj.util.Tool;

import jodd.io.FileUtil;
import jodd.util.SystemUtil;

public class AdminService
{
  private final Logger log = Logger.getLogger(AdminService.class);
  private static final AdminService me = new AdminService();
  
  private AdminService() {}
  public static AdminService me()
  {
    return me;
  }
  
  public Map<String, Object> getSystemInfo()
  {
    Map<String, Object> systemInfo = new HashMap<String, Object>();
    Properties sysProperty = System.getProperties();

    systemInfo.put("JREName", sysProperty.getProperty("java.runtime.name"));
    systemInfo.put("JREVersion", sysProperty.getProperty("java.runtime.version"));
    systemInfo.put("javaHome", sysProperty.getProperty("java.home"));
    systemInfo.put("javaVendor", sysProperty.getProperty("java.vendor"));
    systemInfo.put("javaVersion", SystemUtil.getJavaVersion());
    systemInfo.put("OSName", SystemUtil.getOsName());
    systemInfo.put("OSVersion", SystemUtil.getOsVersion());
    systemInfo.put("OSArch", sysProperty.getProperty("os.arch"));
    systemInfo.put("timezone", sysProperty.getProperty("user.timezone"));
    systemInfo.put("fileSeparator", sysProperty.getProperty("file.separator"));
    systemInfo.put("tempDir", SystemUtil.getTempDir());
    systemInfo.put("workDir", SystemUtil.getUserDir());
    systemInfo.put("userHome", SystemUtil.getUserHome());
    
    systemInfo.put("mysql", Db.queryStr("select version() as v"));
    
    return systemInfo;
  }
  
  public Map<String, Object> getOjInfo()
  {
    Map<String, Object> ojInfo = new HashMap<String, Object>();
    String rootPath = PathKit.getWebRootPath();
    
    ojInfo.put("webRootPath", rootPath);
    ojInfo.put("baseViewPath", AppConfig.getBaseViewPath());
    ojInfo.put("devMode", OjConfig.getDevMode());
    ojInfo.put("siteTitle", OjConfig.siteTitle);
    ojInfo.put("userAvatarPath", OjConfig.userAvatarPath.replace(rootPath, ""));
    ojInfo.put("problemImagePath", OjConfig.problemImagePath.replace(rootPath, ""));
    ojInfo.put("uploadPath", OjConfig.uploadPath.replace(rootPath, ""));
    ojInfo.put("downloadPath", OjConfig.downloadPath.replace(rootPath, ""));
    
    ojInfo.put("workPath", OjConfig.get("workPath"));
    ojInfo.put("dataPath", OjConfig.get("dataPath"));
    ojInfo.put("runShell", OjConfig.get("runShell"));
    ojInfo.put("compilerShell", OjConfig.get("compileShell"));
    ojInfo.put("debugFile", OjConfig.get("debugFile"));
    ojInfo.put("errorFile", OjConfig.get("errorFile"));
    
    return ojInfo;
  }
  
  public void reloadConfig(String type)
  {
    switch(type)
    {
      case "var":OjConfig.loadVariable();break;
      case "lang":OjConfig.loadLanguage();break;
      case "level":OjConfig.loadLevel();break;
      default:OjConfig.loadConfig();
    }
  }
  
  public int updateConfig(String name, String value, String type)
  {
    Record record = Db.findFirst("SELECT * FROM variable WHERE name=?", name);
    
    if (record == null)
    {
      return -1;
    }
    
    switch (type)
    {
      case "string":record.set("value", value);break;
      case "boolean":record.set("boolean_value", value);break;
      case "int":record.set("int_value", value);break;
      case "text":record.set("text_value", value);break;
      default:return -2;
    }
    record.set("type", type);
    
    return Db.update("variable", record) ? 0 : 1;
  }
  
  public List<FpsProblem> importProblems(File file, Integer outputLimit, Boolean status)
  {
    Document doc = Tool.parseXML(file);
    NodeList itemList = doc.getElementsByTagName("item");
    List<FpsProblem> problemList = new ArrayList<FpsProblem>();
    try
    {
      FileUtil.mkdirs(OjConfig.problemImagePath);
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
    
    for (int i=0; i<itemList.getLength(); ++i)
    {
      FpsProblem problem = new FpsProblem(outputLimit * 1024, status);
      problem.itemToProblem(itemList.item(i));
      problemList.add(problem);
    }
    return problemList;
  }
  
}
