package com.power.oj.solution;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;

public class SolutionService
{
  private static final SolutionModel dao = SolutionModel.dao;
  private static final SolutionService me = new SolutionService();
  
  private SolutionService() {}
  public static SolutionService me()
  {
    return me;
  }

  public Page<SolutionModel> getPage(int pageNumber, int pageSize, int result, int language, int pid, String userName)
  {
    String sql = "SELECT sid,s.uid,pid,cid,num,result,time,memory,s.language,code_len,FROM_UNIXTIME(s.ctime, '%Y-%m-%d %H:%i:%s') AS ctime,u.name";
    StringBuilder sb = new StringBuilder("FROM solution s LEFT JOIN user u ON u.uid=s.uid WHERE cid=0 ");

    List<Object> paras = new ArrayList<Object>();
    if (result > -1)
    {
      sb.append(" AND result=?");
      paras.add(result);
    }
    if (language > -1)
    {
      sb.append(" AND s.language=?");
      paras.add(language);
    }
    if (pid > 0)
    {
      sb.append(" AND pid=? AND cid=0");
      paras.add(pid);
    }
    if (StringUtil.isNotBlank(userName))
    {
      sb.append(" AND name=?");
      paras.add(userName);
    }

    sb.append(" ORDER BY sid DESC");
    Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    for (SolutionModel solution : solutionList.getList())
    {
      solution.set("language", ((LanguageModel) OjConfig.language_type.get(solution.getInt("language"))).get("name"));

      ResultType resultType = (ResultType) OjConfig.result_type.get(solution.getInt("result"));
      solution.put("resultName", resultType.getName());
      solution.put("resultLongName", resultType.getLongName());

      if (solution.get("num") != null && solution.getInt("num") > -1)
      {
        solution.put("alpha", (char) (solution.getInt("num") + 'A'));
      }
    }

    return solutionList;
  }

  public SolutionModel findSolution(Integer sid)
  {
    return dao.findFirst("SELECT * FROM solution WHERE sid=? LIMIT 1", sid);
  }
  

  public Page<SolutionModel> getPageForContest(int pageNumber, int pageSize, int result, int language, int cid, int num, String userName)
  {
    String sql = "SELECT sid,s.uid,pid,cid,num,result,time,memory,s.language,code_len,FROM_UNIXTIME(s.ctime, '%Y-%m-%d %H:%i:%s') AS ctime,u.name,u.nick";
    StringBuilder sb = new StringBuilder("FROM contest_solution s LEFT JOIN user u ON u.uid=s.uid WHERE cid=?");
    List<Object> paras = new ArrayList<Object>();
    paras.add(cid);
    
    if (result > -1)
    {
      sb.append(" AND result=?");
      paras.add(result);
    }
    if (language > -1)
    {
      sb.append(" AND s.language=?");
      paras.add(language);
    }
    if (num > -1)
    {
      sb.append(" AND num=?");
      paras.add(num);
    }
    if (StringUtil.isNotBlank(userName))
    {
      sb.append(" AND name=?");
      paras.add(userName);
    }

    sb.append(" ORDER BY sid DESC");
    Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    for (SolutionModel solution : solutionList.getList())
    {
      solution.set("language", ((LanguageModel) OjConfig.language_type.get(solution.getInt("language"))).get("name"));

      ResultType resultType = (ResultType) OjConfig.result_type.get(solution.getInt("result"));
      solution.put("resultName", resultType.getName());
      solution.put("resultLongName", resultType.getLongName());
      solution.put("alpha", (char) (solution.getInt("num") + 'A'));
    }

    return solutionList;
  }

  public Page<SolutionModel> getPage(int pageNumber, int pageSize, Integer result, Integer language, Integer uid)
  {
    String sql = "SELECT sid,s.uid,s.pid,cid,num,result,time,memory,s.language,code_len,s.ctime,p.title";
    StringBuilder sb = new StringBuilder("FROM solution s LEFT JOIN problem p ON p.pid=s.pid WHERE 1=1");

    List<Object> paras = new ArrayList<Object>();
    if (result > -1)
    {
      sb.append(" AND s.result=?");
      paras.add(result);
    }
    if (language > -1)
    {
      sb.append(" AND s.language=?");
      paras.add(language);
    }
    
    sb.append(" AND s.uid=?");
    paras.add(uid);

    sb.append(" ORDER BY sid DESC");
    Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    for (SolutionModel solution : solutionList.getList())
    {
      solution.set("language", ((LanguageModel) OjConfig.language_type.get(solution.getInt("language"))).get("name"));

      ResultType resultType = (ResultType) OjConfig.result_type.get(solution.getInt("result"));
      solution.put("resultName", resultType.getName());
      solution.put("resultLongName", resultType.getLongName());
    }

    return solutionList;
  }

  public Page<SolutionModel> getProblemStatusPage(int pageNumber, int pageSize, Integer language, Integer pid)
  {
    // TODO check user permission for view source code
    String sql = "SELECT sid,s.uid,u.name,pid,result,time,memory,s.language,code_len,s.ctime,l.name AS language";
    StringBuilder sb = new StringBuilder("FROM solution s LEFT JOIN user u ON u.uid=s.uid LEFT JOIN program_language l ON l.id=s.language WHERE result=0");

    List<Object> paras = new ArrayList<Object>();

    if (language != null)
    {
      sb.append(" AND s.language=?");
      paras.add(language);
    }

    sb.append(" AND pid=?");
    paras.add(pid);

    sb.append(" ORDER BY time,memory,code_len,sid");
    Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    return solutionList;
  }

  public Page<SolutionModel> getProblemStatusPageForContest(int pageNumber, int pageSize, int language, int cid, int num)
  {
    String sql = "SELECT sid,s.uid,u.name,pid,result,time,memory,s.language,code_len,FROM_UNIXTIME(s.ctime, '%Y-%m-%d %H:%i:%s') AS ctime,l.name AS language";
    StringBuilder sb = new StringBuilder("FROM contest_solution s LEFT JOIN user u ON u.uid=s.uid LEFT JOIN program_language l ON l.id=s.language WHERE result=0");

    List<Object> paras = new ArrayList<Object>();

    if (language > -1)
    {
      sb.append(" AND s.language=?");
      paras.add(language);
    }

    sb.append(" AND cid=?");
    paras.add(cid);

    sb.append(" AND num=?");
    paras.add(num);

    sb.append(" ORDER BY time,memory,code_len,sid");
    Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    return solutionList;
  }

  public List<SolutionModel> getProblemStatusForContest(Integer cid, Integer num)
  {
    List<SolutionModel> resultList = dao.find("SELECT result,COUNT(*) AS count FROM contest_solution WHERE cid=? AND num=? GROUP BY result", cid, num);
    
    for (SolutionModel record : resultList)
    {
      ResultType resultType = (ResultType) OjConfig.result_type.get(record.getInt("result"));
      record.put("longName", resultType.getLongName());
      record.put("name", resultType.getName());
    }
    
    return resultList;
  }
}
