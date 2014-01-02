package com.power.oj.core;

import java.util.HashMap;
import java.util.List;

import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.VariableModel;

import jodd.util.collection.IntHashMap;

/**
 * Configure the system.
 * 
 * @author power
 * 
 */
public class OjConfig
{

  public static String baseUrl = null;
  public static String siteTitle = null;
  public static String userAvatarPath = null;
  public static String problemImagePath = null;
  public static String uploadPath = null;
  public static String downloadPath = null;

  public static List<LanguageModel> program_languages;
  public static IntHashMap language_type = new IntHashMap();
  public static IntHashMap language_name = new IntHashMap();
  public static List<ResultType> judge_result;
  public static IntHashMap result_type = new IntHashMap();

  public static HashMap<String, VariableModel> variable = new HashMap<String, VariableModel>();

  public static int contestPageSize = 20;
  public static int contestRankPageSize = 50;
  public static int problemPageSize = 50;
  public static int userPageSize = 20;
  public static int statusPageSize = 20;

  public static long timeStamp;
  public static long startGlobalInterceptorTime;
  public static long startGlobalHandlerTime;

  /*
   * get OJ variable from DB cache
   */
  public static String get(String name)
  {
    return get(name, null);
  }
  
  public static String get(String name, String defaultValue)
  {
    VariableModel model = variable.get(name);
    if (model != null)
    {
      return model.getStr("value");
    }
    
    return defaultValue;
  }
  
  public static Integer getInt(String name)
  {
    return getInt(name, null);
  }

  public static Integer getInt(String name, Integer defaultValue)
  {
    VariableModel model = variable.get(name);
    if (model != null)
    {
      return model.getInt("int_value");
    }
    
    return defaultValue;
  }

  public static Boolean getBoolean(String name)
  {
    return getBoolean(name ,null);
  }

  public static Boolean getBoolean(String name, Boolean defaultValue)
  {
    VariableModel model = variable.get(name);
    if (model != null)
    {
      return model.getBoolean("boolean_value");
    }
    
    return defaultValue;
  }

  public static String getText(String name)
  {
    return variable.get(name).getStr("text_value");
  }

  public static String getType(String name)
  {
    return variable.get(name).getStr("type");
  }

}
