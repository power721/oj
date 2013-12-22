package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jodd.util.collection.IntHashMap;

import com.power.oj.core.OjConfig;
import com.power.oj.core.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.VariableModel;

public class OjService
{

  public static void loadVariable()
  {
    OjConfig.variable = new HashMap<String, VariableModel>();
    for (VariableModel variableModel : VariableModel.dao.find("SELECT * FROM variable"))
    {
      OjConfig.variable.put(variableModel.getStr("name"), variableModel);
    }
  }
  
  public static void loadLanguage()
  {
    OjConfig.language_type = new IntHashMap();
    OjConfig.language_name = new IntHashMap();
    OjConfig.program_languages = LanguageModel.dao.find("SELECT * FROM program_language WHERE status=1");
    for (LanguageModel Language : OjConfig.program_languages)
    {
      OjConfig.language_type.put(Language.getInt("id"), Language);
      OjConfig.language_name.put(Language.getInt("id"), Language.getStr("name"));
    }
  }
  
  public static void initJudgeResult()
  {
    OjConfig.judge_result = new ArrayList<ResultType>();
    OjConfig.judge_result.add(new ResultType(ResultType.AC, "AC", "Accepted"));
    OjConfig.judge_result.add(new ResultType(ResultType.PE, "PE", "Presentation Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.TLE, "TLE", "Time Limit Exceed"));
    OjConfig.judge_result.add(new ResultType(ResultType.MLE, "MLE", "Memory Limit Exceed"));
    OjConfig.judge_result.add(new ResultType(ResultType.WA, "WA", "Wrong Answer"));
    OjConfig.judge_result.add(new ResultType(ResultType.RE, "RE", "Runtime Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.OLE, "OLE", "Output Limit Exceed"));
    OjConfig.judge_result.add(new ResultType(ResultType.CE, "CE", "Compile Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.SE, "SE", "System Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.VE, "VE", "Validate Error"));
    OjConfig.judge_result.add(new ResultType(ResultType.Wait, "Wait", "Waiting"));

    OjConfig.result_type = new IntHashMap();
    for (Iterator<ResultType> it = OjConfig.judge_result.iterator(); it.hasNext();)
    {
      ResultType resultType = it.next();
      OjConfig.result_type.put(resultType.getId(), resultType);
    }
  }

}
