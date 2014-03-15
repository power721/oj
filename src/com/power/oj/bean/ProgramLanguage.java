package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class ProgramLanguage extends Model<ProgramLanguage>
{
  private static final long serialVersionUID = 1L;
  
  public static final ProgramLanguage dao = new ProgramLanguage();
  
  public static final String TABLE_NAME = "program_language";
  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String DESCRIPTION = "description";
  public static final String EXT_TIME = "extTime";
  public static final String EXT_MEMORY = "extMemory";
  public static final String TIME_FACTOR = "timeFactor";
  public static final String MEMORY_FACTOR = "memoryFactor";
  public static final String EXT = "ext";
  public static final String EXE = "exe";
  public static final String COMPLIE_ORDER = "complieOrder";
  public static final String COMPILE_CMD = "compileCmd";
  public static final String BRUSH = "brush";
  public static final String SCRIPT = "script";
  public static final String STATUS = "status";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public ProgramLanguage setId(Integer value)
  {
    return set(ID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public ProgramLanguage setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getDescription()
  {
    return getStr(DESCRIPTION);
  }
  
  public ProgramLanguage setDescription(String value)
  {
    return set(DESCRIPTION, value);
  }
  
  public Integer getExtTime()
  {
    return getInt(EXT_TIME);
  }
  
  public ProgramLanguage setExtTime(Integer value)
  {
    return set(EXT_TIME, value);
  }
  
  public Integer getExtMemory()
  {
    return getInt(EXT_MEMORY);
  }
  
  public ProgramLanguage setExtMemory(Integer value)
  {
    return set(EXT_MEMORY, value);
  }
  
  public Integer getTimeFactor()
  {
    return getInt(TIME_FACTOR);
  }
  
  public ProgramLanguage setTimeFactor(Integer value)
  {
    return set(TIME_FACTOR, value);
  }
  
  public Integer getMemoryFactor()
  {
    return getInt(MEMORY_FACTOR);
  }
  
  public ProgramLanguage setMemoryFactor(Integer value)
  {
    return set(MEMORY_FACTOR, value);
  }
  
  public String getExt()
  {
    return getStr(EXT);
  }
  
  public ProgramLanguage setExt(String value)
  {
    return set(EXT, value);
  }
  
  public String getExe()
  {
    return getStr(EXE);
  }
  
  public ProgramLanguage setExe(String value)
  {
    return set(EXE, value);
  }
  
  public Integer getComplieOrder()
  {
    return getInt(COMPLIE_ORDER);
  }
  
  public ProgramLanguage setComplieOrder(Integer value)
  {
    return set(COMPLIE_ORDER, value);
  }
  
  public String getCompileCmd()
  {
    return getStr(COMPILE_CMD);
  }
  
  public ProgramLanguage setCompileCmd(String value)
  {
    return set(COMPILE_CMD, value);
  }
  
  public String getBrush()
  {
    return getStr(BRUSH);
  }
  
  public ProgramLanguage setBrush(String value)
  {
    return set(BRUSH, value);
  }
  
  public Boolean getScript()
  {
    return getBoolean(SCRIPT);
  }
  
  public ProgramLanguage setScript(Boolean value)
  {
    return set(SCRIPT, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public ProgramLanguage setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
