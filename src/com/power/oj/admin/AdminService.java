package com.power.oj.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.AppConfig;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.FpsProblem;
import com.power.oj.problem.ProblemModel;

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

  public List<String> getDataFiles(Integer pid)
  {
    List<String> dataFiles = new ArrayList<String>();
    File dataDir = new File(new StringBuilder(3).append(OjConfig.get("dataPath")).append(File.separator).append(pid).toString());
    if (!dataDir.isDirectory())
    {
      return dataFiles;
    }
    
    File[] arrayOfFile = dataDir.listFiles();
    if (arrayOfFile.length > 1)
    {
      Arrays.sort(arrayOfFile);
    }
    
    for (int i = 0; i < arrayOfFile.length; i++)
    {
      try
      {
        FileInputStream fis = new FileInputStream(arrayOfFile[i]);
        long size = fis.available();
        fis.close();
        String str = String.format("%d", size) + " ";
        if (size >= 1048576)
          str = String.format("%.2f", size / 1048576.) + " M";
        else if (size >= 1024)
          str = String.format("%.2f", size / 1024.) + " K";
        dataFiles.add(arrayOfFile[i].getName() + ";" + str + "B");
      } catch (FileNotFoundException e)
      {
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.error(e.getLocalizedMessage());
      } catch (IOException e)
      {
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.error(e.getLocalizedMessage());
      }
    }
    
    return dataFiles;
  }
  
  public String uploadData(Integer pid, String filename, File srcFile) throws IOException
  {
    String destFileName = new StringBuilder(5).append(OjConfig.get("dataPath")).
        append(File.separator).append(pid).append(File.separator).append(filename).toString();
    File destFile = new File(destFileName);
    
    if (destFile.exists())
    {
      FileUtil.delete(destFile);
    }
    FileUtil.moveFile(srcFile, destFile);
    log.info(destFile.getAbsolutePath());
    
    return destFile.getName();
  }
  
  public File downloadData(Integer pid, String filename)
  {
    String destFileName = new StringBuilder(5).append(OjConfig.get("dataPath")).
        append(File.separator).append(pid).append(File.separator).append(filename).toString();
    File file = new File(destFileName);
    
    if (file.exists())
    {
      return file;
    }
    
    return null;
  }

  public String editData(Integer pid, String filename)
  {
    String destFileName = new StringBuilder(5).append(OjConfig.get("dataPath")).
        append(File.separator).append(pid).append(File.separator).append(filename).toString();
    File file = new File(destFileName);
    
    if (file.exists())
    {
      try
      {
        String content = FileUtil.readString(file);
        if (content.length() >= 2 * 1024 * 1024)
        {
          content = null;
        }
        return content;
      } catch (IOException e)
      {
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.error(e.getLocalizedMessage());
        return null;
      }
    }
    
    return null;
  }
  
  public boolean updateData(Integer pid, String originalName, String filename, String content)
  {
    String srcFileName = new StringBuilder(5).append(OjConfig.get("dataPath")).
        append(File.separator).append(pid).append(File.separator).append(originalName).toString();
    File srcFile = new File(srcFileName);

    if (srcFile.exists())
    {
      try
      {
        FileUtil.writeString(srcFile, content);
        if (!originalName.equals(filename))
        {
          String destFileName = new StringBuilder(5).append(OjConfig.get("dataPath")).
              append(File.separator).append(pid).append(File.separator).append(filename).toString();
          File destFile = new File(destFileName);
          FileUtil.touch(destFile);
          FileUtil.move(srcFile, destFile);
        }
      } catch (IOException e)
      {
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.error(e.getLocalizedMessage());
        return false;
      }
    }
    else
    {
      log.warn(srcFileName);
      return false;
    }
    return true;
  }

  public boolean deleteData(Integer pid, String filename)
  {
    String fileName = new StringBuilder(5).append(OjConfig.get("dataPath")).
        append(File.separator).append(pid).append(File.separator).append(filename).toString();
    File srcFile = new File(fileName);
    File destFile = new File(OjConfig.uploadPath + File.separator + filename);
    
    try
    {
      FileUtil.moveFile(srcFile, destFile);
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
      return false;
    }
    
    return true;
  }
  
  public List<FpsProblem> importProblems(File file, Integer outputLimit, Boolean status)
  {
    SAXReader saxReader = new SAXReader();
    Document document = null;
    List<FpsProblem> problemList = new ArrayList<FpsProblem>();
    
    try
    {
      document = saxReader.read(file);
    } catch (DocumentException e1)
    {
      if (OjConfig.getDevMode())
        e1.printStackTrace();
      log.error(e1.getLocalizedMessage());
      return problemList;
    }
    
    try
    {
      FileUtil.mkdirs(OjConfig.problemImagePath);
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }

    Element root = document.getRootElement();
    for (Iterator<?> i = root.elementIterator("item"); i.hasNext();)
    {
      Element ele = (Element) i.next();
      FpsProblem problem = new FpsProblem(outputLimit * 1024, status);
      problem.itemToProblem(ele);
      problemList.add(problem);
    }

    return problemList;
  }
  
  public File exportProblems(String problems, Boolean status, Boolean replace)
  {
    String[] pidStr = problems.split(",");
    Document document = DocumentHelper.createDocument();
    Element rootElement = createXmlRootElement(document);
    
    for (String pidPar : pidStr)
    {
      String pids[] = pidPar.split("-");
      if (pids.length == 1)
      {
        try
        {
          Integer pid = Integer.parseInt(pids[0]);
          ProblemModel problemModel = null;
          if (status)
          {
            problemModel = ProblemModel.dao.
              findFirst("SELECT * FROM problem WHERE pid=? AND status=1", pid);
          }
          else
          {
            problemModel = ProblemModel.dao.findFirst("SELECT * FROM problem WHERE pid=?", pid);
          }
          Element item = rootElement.addElement("item");
          
          item.addAttribute("pid", String.valueOf(problemModel.getPid()));
          FpsProblem problem = new FpsProblem(problemModel);
          problem.problemToItem(item, replace);
        } catch (NumberFormatException e)
        {
          if (OjConfig.getDevMode())
            e.printStackTrace();
          log.error(e.getLocalizedMessage());
          return null;
        }
      }
      else if (pids.length == 2)
      {
        Integer start = 1;
        Integer end = 0;
        try
        {
          start = Integer.parseInt(pids[0]);
          end = Integer.parseInt(pids[1]);
        } catch (NumberFormatException e)
        {
          if (OjConfig.getDevMode())
            e.printStackTrace();
          log.error(e.getLocalizedMessage());
          return null;
        }
        
        if (start <= end)
        {
          List<ProblemModel> problemList = null;
          if (status)
          {
            problemList = ProblemModel.dao.
              find("SELECT * FROM problem WHERE pid>=? AND pid<=? AND status=1", start, end);
          }
          else
          {
            problemList = ProblemModel.dao.
                find("SELECT * FROM problem WHERE pid>=? AND pid<=?", start, end);
          }
          for (ProblemModel problemModel : problemList)
          {
            Element item = rootElement.addElement("item");
            item.addAttribute("pid", String.valueOf(problemModel.getPid()));
            FpsProblem problem = new FpsProblem(problemModel);
            problem.problemToItem(item, replace);
          }
        }
      }
    }

    String fileName = new StringBuilder(4).append("PowerOJ").append("-")
        .append(problems.replaceAll(",", "_")).append(".xml").toString();
    return exportXmlFile(document, fileName);
  }
  
  public File exportProblems(Integer start, Integer end, Boolean status, Boolean replace)
  {
    if (start > end)
    {
      return null;
    }
    Document document = DocumentHelper.createDocument();
    Element rootElement = createXmlRootElement(document);
    
    List<ProblemModel> problemList = ProblemModel.dao.
        find("SELECT * FROM problem WHERE pid>=? AND pid<=? AND status=?", start, end, status ? 1 : 0);
    for (ProblemModel problemModel : problemList)
    {
      Element item = rootElement.addElement("item");
      item.addAttribute("pid", String.valueOf(problemModel.getPid()));
      FpsProblem problem = new FpsProblem(problemModel);
      problem.problemToItem(item, replace);
    }
    
    String fileName = new StringBuilder(6).append("PowerOJ").append("-").
        append(start).append("-").append(end).append(".xml").toString();
    return exportXmlFile(document, fileName);
  }
  
  private Element createXmlRootElement(Document document)
  {
    document.addDocType("fps", "-//FreeProblemSet//EN", "http://freeproblemset.googlecode.com/svn/trunk/fps.current.dtd");
    Element rootElement = document.addElement("fps");
    rootElement.addAttribute("version", "1.1");
    rootElement.addAttribute("url", "http://code.google.com/p/freeproblemset/");
    
    Element generator = rootElement.addElement("generator");
    generator.addAttribute("name", OjConfig.get("siteTitle", "PowerOJ"));
    generator.addAttribute("url", OjConfig.get("domaiNname", "http://git.oschina.net/power/oj"));
    
    return rootElement;
  }
  
  private File exportXmlFile(Document document, String fileName)
  {
    String filePath = new StringBuilder(8).append(OjConfig.downloadPath).
        append(File.separator).append(fileName).toString();
    try
    {
      OutputFormat format = OutputFormat.createPrettyPrint();
      format.setEncoding("UTF-8");
      OutputStream out = new FileOutputStream(filePath);
      Writer fileWriter = new OutputStreamWriter(out, "UTF-8");
      XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
      
      xmlWriter.write(document);
      xmlWriter.flush();
      xmlWriter.close();
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
    return new File(filePath);
  }
  
}
