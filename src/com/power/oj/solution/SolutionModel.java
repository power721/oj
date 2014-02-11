package com.power.oj.solution;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.contest.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.user.UserModel;

public class SolutionModel extends Model<SolutionModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = -287562753616099282L;
  
  public static final SolutionModel dao = new SolutionModel();

  public int getUid()
  {
    return getInt("uid");
  }
  
  public Page<SolutionModel> getPage(int pageNumber, int pageSize, int result, int language, int pid, String userName)
  {
    String sql = "SELECT sid,s.uid,pid,cid,num,result,time,memory,s.language,code_len,FROM_UNIXTIME(s.ctime, '%Y-%m-%d %H:%i:%s') AS ctime,u.name";
    StringBuilder sb = new StringBuilder("FROM solution s LEFT JOIN user u ON u.uid=s.uid WHERE 1=1");

    List<Object> paras = new ArrayList<Object>();
    if (result > -1)
    {
      sb.append(" AND result=?");
      paras.add(result);
    }
    if (language > -1)
    {
      sb.append(" AND language=?");
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
      sb.append(" AND language=?");
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
    Page<SolutionModel> solutionList = SolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

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
    Integer uid = 0;
    String name = "";
    String sql = "SELECT sid,uid,pid,result,time,memory,language,code_len,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";
    StringBuilder sb = new StringBuilder("FROM solution WHERE result=0");

    List<Object> paras = new ArrayList<Object>();

    if (language != null)
    {
      sb.append(" AND language=?");
      paras.add(language);
    }

    sb.append(" AND pid=?");
    paras.add(pid);

    sb.append(" ORDER BY time,memory,code_len,sid");
    Page<SolutionModel> solutionList = SolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    for (SolutionModel solution : solutionList.getList())
    {

      uid = solution.getUid();
      try
      {
        name = UserModel.dao.findById(uid, "name").get("name");
      } catch (NullPointerException e)
      {
        name = "";
      }
      solution.put("name", name);

      solution.set("language", OjConfig.language_name.get(solution.getInt("language")));
    }

    return solutionList;
  }

  public Page<SolutionModel> getProblemStatusPageForContest(int pageNumber, int pageSize, int language, int cid, int num)
  {
    int uid = 0;
    String name = "";
    String sql = "SELECT sid,uid,pid,result,time,memory,language,code_len,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";
    StringBuilder sb = new StringBuilder("FROM solution WHERE result=0");

    List<Object> paras = new ArrayList<Object>();

    if (language > -1)
    {
      sb.append(" AND language=?");
      paras.add(language);
    }

    sb.append(" AND cid=?");
    paras.add(cid);

    sb.append(" AND num=?");
    paras.add(num);

    sb.append(" ORDER BY time,memory,code_len,sid");
    Page<SolutionModel> solutionList = SolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    for (SolutionModel solution : solutionList.getList())
    {

      uid = solution.getUid();
      name = UserModel.dao.findById(uid, "name").get("name");
      solution.put("name", name);

      solution.set("language", ((LanguageModel) OjConfig.language_type.get(solution.getInt("language"))).get("name"));
    }

    return solutionList;
  }

  public boolean addSolution()
  {
    long ctime = OjConfig.timeStamp;
    if (get("cid") != null)
    {
      int pid = ContestModel.dao.getPid(getInt("cid"), getInt("num"));
      set("pid", pid);
    }
    this.set("ctime", ctime).set("result", ResultType.WAIT).set("code_len", this.getStr("source").length());
    System.out.println(this.getInt("code_len"));
    if (this.getInt("code_len") < 10 || this.getInt("code_len") > 30000)
      return false;

    return this.save();
  }
}
