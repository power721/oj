package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Level extends Model<Level>
{
  private static final long serialVersionUID = 1L;
  
  public static final Level dao = new Level();
  
  public static final String TABLE_NAME = "level";
  public static final String LEVEL = "level";
  public static final String EXPERIENCE = "experience";

  public Integer getLevel()
  {
    return getInt(LEVEL);
  }
  
  public Level setLevel(Integer value)
  {
    return set(LEVEL, value);
  }
  
  public Integer getExperience()
  {
    return getInt(EXPERIENCE);
  }
  
  public Level setExperience(Integer value)
  {
    return set(EXPERIENCE, value);
  }
  
}
