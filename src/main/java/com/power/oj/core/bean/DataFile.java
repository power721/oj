package com.power.oj.core.bean;

import java.io.File;
import java.io.IOException;

import jodd.io.FileNameUtil;
import jodd.util.StringUtil;

import org.apache.commons.io.FileUtils;

import com.power.oj.core.OjConfig;

/**
 * file info for problem data files.
 * current implementation is for all files.
 * @author power
 *
 */
public class DataFile
{
  private static String[] exts = {"in", "out", "txt", "c", "cc", "cpp", "pas", "java", "py"};
  private String name;
  private String path;
  private String user;
  private String group;
  private String perm;
  private int pid;
  private long createTime;
  private File file;
  
  public DataFile()
  {
    
  }
  
  public DataFile(Integer pid, String name)
  {
    this.pid = pid;
    this.name = name;
    
    path = new StringBuilder(5).append(OjConfig.getString("dataPath")).
        append(File.separator).append(pid).append(File.separator).append(name).toString();
    file = new File(path);
  }

  public DataFile(Integer pid, File file)
  {
    this.pid = pid;
    this.file = file;
    
    if (file.exists())
    {
      name = file.getName();
    }
  }
  
  public boolean exists()
  {
    return file.exists();
  }
  
  public String readString() throws IOException
  {
    String ext = FileNameUtil.getExtension(name);
    if (StringUtil.equalsOne(ext, exts) == -1)
    {
      return null;
    }
    return FileUtils.readFileToString(file);
  }
  
  public void writeString(String data) throws IOException
  {
    FileUtils.writeStringToFile(file, data);
  }
  
  public void touch() throws IOException
  {
    FileUtils.touch(file);
  }
  
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPath()
  {
    return path;
  }

  public String getUser()
  {
    return user;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  public String getGroup()
  {
    return group;
  }

  public void setGroup(String group)
  {
    this.group = group;
  }

  public String getPerm()
  {
    return perm;
  }

  public void setPerm(String perm)
  {
    this.perm = perm;
  }

  public int getPid()
  {
    return pid;
  }

  public void setPid(int pid)
  {
    this.pid = pid;
  }

  public long getSize()
  {
    return file.length();
  }

  public long getCreateTime()
  {
    return createTime;
  }

  public long getModifyTime()
  {
    return file.lastModified();
  }

  public File getFile()
  {
    return file;
  }

}
