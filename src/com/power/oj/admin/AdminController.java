package com.power.oj.admin;

import java.util.Properties;

import jodd.util.SystemUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.plugin.activerecord.Db;
import com.power.oj.core.OjController;

@RequiresPermissions("admin")
public class AdminController extends OjController
{

  public void index()
  {
    Properties sysProperty = System.getProperties(); // 系统属性

    setAttr("JREName", sysProperty.getProperty("java.runtime.name"));
    setAttr("JREVersion", sysProperty.getProperty("java.runtime.version"));
    setAttr("javaHome", sysProperty.getProperty("java.home"));
    setAttr("javaVendor", sysProperty.getProperty("java.vendor"));
    setAttr("javaVersion", SystemUtil.getJavaVersion());
    setAttr("OSName", SystemUtil.getOsName());
    setAttr("OSVersion", SystemUtil.getOsVersion());
    setAttr("OSArch", sysProperty.getProperty("os.arch"));
    setAttr("timezone", sysProperty.getProperty("user.timezone"));
    setAttr("fileSeparator", sysProperty.getProperty("file.separator"));
    setAttr("tempDir", SystemUtil.getTempDir());
    setAttr("workDir", SystemUtil.getUserDir());
    setAttr("userHome", SystemUtil.getUserHome());
   
    setAttr("mysql", Db.queryStr("select version() as v"));

    setTitle(getText("admin.index.title"));
  }

}
