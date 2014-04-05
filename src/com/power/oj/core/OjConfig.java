package com.power.oj.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.util.FileKit;

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

  public static int contestPageSize = 20;
  public static int contestRankPageSize = 50;
  public static int problemPageSize = 50;
  public static int userPageSize = 20;
  public static int friendPageSize = 10;
  public static int statusPageSize = 20;
  public static int mailGroupPageSize = 10;
  public static int mailPageSize = 20;
  public static int noticePageSize = 20;

  public static int timeStamp;
  public static long startInterceptorTime;
  public static long startGlobalHandlerTime;

  public static boolean variableChanged = false;
  public static List<ProgramLanguageModel> program_languages;
  public static HashMap<Integer, ProgramLanguageModel> language_type;
  public static HashMap<Integer, String> language_name;
  public static HashMap<Integer, ResultType> result_type;
  public static List<ResultType> judge_result;
  public static List<Integer> level;

  private static HashMap<String, VariableModel> variable = new HashMap<String, VariableModel>();
  // TODO use enChahe

  public static boolean getDevMode()
  {
    return JFinal.me().getConstants().getDevMode();
  }
  
  public static void loadConfig()
  {
    loadVariable();
    loadLanguage();
    loadLevel();
  }
  
  public static void loadVariable()
  {
    variable = new HashMap<String, VariableModel>();
    for (VariableModel variableModel : VariableModel.dao.find("SELECT * FROM variable"))
    {
      variable.put(variableModel.getName(), variableModel);
    }
    
    siteTitle = get("siteTitle", "Power OJ");

    uploadPath = FileKit.parsePath(get("uploadPath", "upload/"));
    downloadPath = FileKit.parsePath(get("downloadPath", "download/"));
    userAvatarPath = FileKit.parsePath(get("userAvatarPath", "upload/image/user/"));
    problemImagePath = FileKit.parsePath(get("problemImagePath", "upload/image/problem/"));
    
    contestPageSize = getInt("contestPageSize", 20);
    contestRankPageSize = getInt("contestRankPageSize", 50);
    problemPageSize = getInt("problemPageSize", 50);
    userPageSize = getInt("userPageSize", 20);
    statusPageSize = getInt("statusPageSize", 20);
    variableChanged = false;
  }
  
  public static void loadLanguage()
  {
    language_type = new HashMap<Integer, ProgramLanguageModel>();
    language_name = new HashMap<Integer, String>();
    program_languages = ProgramLanguageModel.dao.find("SELECT * FROM program_language WHERE status=1");
    for (ProgramLanguageModel Language : program_languages)
    {
      language_type.put(Language.getId(), Language);
      language_name.put(Language.getId(), Language.getName());
    }
  }

  public static void loadLevel()
  {
    level = new ArrayList<Integer>();
    List<Record> levels = Db.find("SELECT * FROM level ORDER BY level");
    for (Record record : levels)
    {
      level.add(record.getInt("exp"));
    }
  }
  
  public static void initJudgeResult()
  {
    judge_result = new ArrayList<ResultType>();
    judge_result.add(new ResultType(ResultType.AC, "AC", "Accepted"));
    judge_result.add(new ResultType(ResultType.PE, "PE", "Presentation Error"));
    judge_result.add(new ResultType(ResultType.WA, "WA", "Wrong Answer"));
    judge_result.add(new ResultType(ResultType.TLE, "TLE", "Time Limit Exceed"));
    judge_result.add(new ResultType(ResultType.MLE, "MLE", "Memory Limit Exceed"));
    judge_result.add(new ResultType(ResultType.OLE, "OLE", "Output Limit Exceed"));
    judge_result.add(new ResultType(ResultType.CE, "CE", "Compile Error"));
    judge_result.add(new ResultType(ResultType.RF, "RF", "Restricted Function"));
    judge_result.add(new ResultType(ResultType.RE, "RE", "Runtime Error"));
    judge_result.add(new ResultType(ResultType.SE, "SE", "System Error"));
    judge_result.add(new ResultType(ResultType.VE, "VE", "Validate Error"));
    judge_result.add(new ResultType(ResultType.RUN, "RUN", "Runing"));
    judge_result.add(new ResultType(ResultType.WAIT, "WAIT", "Waiting"));

    result_type = new HashMap<Integer, ResultType>();
    for (Iterator<ResultType> it = judge_result.iterator(); it.hasNext();)
    {
      ResultType resultType = it.next();
      result_type.put(resultType.getId(), resultType);
    }
  }

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
      return model.getStringValue();
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
      return model.getIntValue();
    }
    
    return defaultValue;
  }

  public static Boolean getBoolean(String name)
  {
    return getBoolean(name, null);
  }

  public static Boolean getBoolean(String name, Boolean defaultValue)
  {
    VariableModel model = variable.get(name);
    if (model != null)
    {
      return model.getBooleanValue();
    }
    
    return defaultValue;
  }

  public static String getText(String name)
  {
    return variable.get(name).getTextValue();
  }

  public static String getType(String name)
  {
    return variable.get(name).getType();
  }

}
