package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jodd.util.collection.IntHashMap;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.util.FileKit;

public class OjService
{
  private static final OjService me = new OjService();
  
  private OjService()
  {
    
  }
  
  public static OjService me()
  {
    return me;
  }
  
  public void loadVariable()
  {
    OjConfig.variable = new HashMap<String, VariableModel>();
    for (VariableModel variableModel : VariableModel.dao.find("SELECT * FROM variable"))
    {
      OjConfig.variable.put(variableModel.getStr("name"), variableModel);
    }
    
    OjConfig.siteTitle = OjConfig.get("siteTitle", "Power OJ");
    
    String userAvatarPath = OjConfig.get("userAvatarPath", "assets/images/user/");
    OjConfig.userAvatarPath = FileKit.parsePath(userAvatarPath);
    
    String problemImagePath = OjConfig.get("problemImagePath", "assets/images/problem/");
    OjConfig.problemImagePath = FileKit.parsePath(problemImagePath);
    
    OjConfig.contestPageSize = OjConfig.getInt("contestPageSize", 20);
    OjConfig.contestRankPageSize = OjConfig.getInt("contestRankPageSize", 50);
    OjConfig.problemPageSize = OjConfig.getInt("problemPageSize", 50);
    OjConfig.userPageSize = OjConfig.getInt("userPageSize", 20);
    OjConfig.statusPageSize = OjConfig.getInt("statusPageSize", 20);
  }
  
  public void loadLanguage()
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
  
  public void initJudgeResult()
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

  public List<Record> getUserRoles(int uid)
  {
    String sql = "SELECT r.name AS role, r.id AS rid FROM roles r LEFT JOIN user_role ur ON ur.rid = r.id WHERE ur.uid = ?";
    List<Record> roleList = Db.find(sql, uid);
    
    return roleList;
  }
  
  public List<Record> getRolePermission(int rid)
  {
    String sql = "SELECT p.name AS permission FROM permission p LEFT JOIN role_permission rp ON rp.pid = p.id WHERE rp.rid = ?";
    List<Record> permissionList = Db.find(sql,rid);
    
    return permissionList;
  }
}
