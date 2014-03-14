package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Level extends Model<Level>
{
  private static final long serialVersionUID = 1L;
  
  public static final Level dao = new Level();
  
  public static final String LEVEL = "level";
  public static final String EXPERIENCE = "experience";

  public <T> T getLevel()
  {
    return get(LEVEL);
  }
  
  public Level setLevel(Object value)
  {
    return set(LEVEL, value);
  }
  
  public <T> T getExperience()
  {
    return get(EXPERIENCE);
  }
  
  public Level setExperience(Object value)
  {
    return set(EXPERIENCE, value);
  }
  
}
