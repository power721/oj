package com.power.oj.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.power.oj.core.AppConfig;
import com.power.oj.core.OjConfig;

import jodd.util.SystemUtil;

public class AdminService
{
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
    
    ojInfo.put("workPath", OjConfig.get("work_path"));
    ojInfo.put("dataPath", OjConfig.get("data_path"));
    ojInfo.put("runShell", OjConfig.get("run_shell"));
    ojInfo.put("compilerShell", OjConfig.get("compile_shell"));
    ojInfo.put("debugFile", OjConfig.get("debug_file"));
    ojInfo.put("errorFile", OjConfig.get("error_file"));
    
    return ojInfo;
  }
  
  public void reloadConfig(String type)
  {
    switch(type)
    {
      case "all":OjConfig.loadConfig();break;
      case "var":OjConfig.loadVariable();break;
      case "lang":OjConfig.loadLanguage();break;
      case "level":OjConfig.loadLevel();break;
    }
  }
  
}
