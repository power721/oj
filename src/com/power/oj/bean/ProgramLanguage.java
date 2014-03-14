package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class ProgramLanguage extends Model<ProgramLanguage>
{
  private static final long serialVersionUID = 1L;
  
  public static final ProgramLanguage dao = new ProgramLanguage();
  
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

  public <T> T getId()
  {
    return get(ID);
  }
  
  public ProgramLanguage setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public ProgramLanguage setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getDescription()
  {
    return get(DESCRIPTION);
  }
  
  public ProgramLanguage setDescription(Object value)
  {
    return set(DESCRIPTION, value);
  }
  
  public <T> T getExtTime()
  {
    return get(EXT_TIME);
  }
  
  public ProgramLanguage setExtTime(Object value)
  {
    return set(EXT_TIME, value);
  }
  
  public <T> T getExtMemory()
  {
    return get(EXT_MEMORY);
  }
  
  public ProgramLanguage setExtMemory(Object value)
  {
    return set(EXT_MEMORY, value);
  }
  
  public <T> T getTimeFactor()
  {
    return get(TIME_FACTOR);
  }
  
  public ProgramLanguage setTimeFactor(Object value)
  {
    return set(TIME_FACTOR, value);
  }
  
  public <T> T getMemoryFactor()
  {
    return get(MEMORY_FACTOR);
  }
  
  public ProgramLanguage setMemoryFactor(Object value)
  {
    return set(MEMORY_FACTOR, value);
  }
  
  public <T> T getExt()
  {
    return get(EXT);
  }
  
  public ProgramLanguage setExt(Object value)
  {
    return set(EXT, value);
  }
  
  public <T> T getExe()
  {
    return get(EXE);
  }
  
  public ProgramLanguage setExe(Object value)
  {
    return set(EXE, value);
  }
  
  public <T> T getComplieOrder()
  {
    return get(COMPLIE_ORDER);
  }
  
  public ProgramLanguage setComplieOrder(Object value)
  {
    return set(COMPLIE_ORDER, value);
  }
  
  public <T> T getCompileCmd()
  {
    return get(COMPILE_CMD);
  }
  
  public ProgramLanguage setCompileCmd(Object value)
  {
    return set(COMPILE_CMD, value);
  }
  
  public <T> T getBrush()
  {
    return get(BRUSH);
  }
  
  public ProgramLanguage setBrush(Object value)
  {
    return set(BRUSH, value);
  }
  
  public <T> T getScript()
  {
    return get(SCRIPT);
  }
  
  public ProgramLanguage setScript(Object value)
  {
    return set(SCRIPT, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public ProgramLanguage setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
