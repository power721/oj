package com.power.oj.core.bean;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.power.oj.core.OjConfig;

public class DataFile
{
  private String name;
  private String path;
  private String user;
  private String group;
  private String perm;
  private int pid;
  private long size;
  private long createTime;
  private long modifyTime;
  private File file;
  
  public DataFile()
  {
    
  }
  
  public DataFile(Integer pid, String name)
  {
    this.pid = pid;
    this.name = name;
    
    path = new StringBuilder(5).append(OjConfig.get("dataPath")).
        append(File.separator).append(pid).append(File.separator).append(name).toString();
    file = new File(path);
    
    if (exists())
    {
      size = file.length();
      modifyTime = file.lastModified();
    }
  }

  public DataFile(Integer pid, File file)
  {
    this.pid = pid;
    this.file = file;
    
    if (exists())
    {
      name = file.getName();
      size = file.length();
      modifyTime = file.lastModified();
    }
  }
  
  public boolean exists()
  {
    return file.exists();
  }
  
  public String readString() throws IOException
  {
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

  public void setPath(String path)
  {
    this.path = path;
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
    return size;
  }

  public void setSize(long size)
  {
    this.size = size;
  }

  public long getCreateTime()
  {
    return createTime;
  }

  public void setCreateTime(long createTime)
  {
    this.createTime = createTime;
  }

  public long getModifyTime()
  {
    return modifyTime;
  }

  public void setModifyTime(long modifyTime)
  {
    this.modifyTime = modifyTime;
  }

  public File getFile()
  {
    return file;
  }

  public void setFile(File file)
  {
    this.file = file;
  }
  
}
