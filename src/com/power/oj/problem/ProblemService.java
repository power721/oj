package com.power.oj.problem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jodd.io.FileUtil;
import jodd.util.StringUtil;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;

public class ProblemService
{
  private static final Logger log = Logger.getLogger(ProblemService.class);
  private static final ProblemService me = new ProblemService();
  private static final UserService userService = UserService.me();
  private static final ProblemModel dao = ProblemModel.dao;
  
  private ProblemService() {}
  
  public static ProblemService me()
  {
    return me;
  }
  
  public Page<ProblemModel> getProblemPage(int pageNumber, int pageSize)
  {
    String sql = "SELECT pid,title,source,accept,submit,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime,status";
    StringBuilder sb = new StringBuilder().append("FROM problem");
    if (!userService.isAdmin())
      sb.append(" WHERE status=1");
    sb.append(" ORDER BY pid");

    Page<ProblemModel> problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString());
    
    return problemList;
  }

  public int getPageNumber(Integer pid, int pageSize)
  {
    long pageNumber = 0;
    StringBuilder sb = new StringBuilder().append("SELECT COUNT(*) AS idx FROM problem WHERE pid<?");
    if (!userService.isAdmin())
      sb.append(" AND status=1");
    sb.append(" ORDER BY pid LIMIT 1");
    
    pageNumber = dao.findFirst(sb.toString(), pid).getLong("idx");
    pageNumber = (pageNumber + pageSize) / pageSize;
    return (int) pageNumber;
  }

  public ProblemModel findProblem(Integer pid)
  {
    return dao.findByPid(pid, userService.isAdmin());
  }
  
  public int getNextPid(Integer pid)
  {
    return dao.getNextPid(pid, userService.isAdmin());
  }
  
  public int getPrevPid(Integer pid)
  {
    return dao.getPrevPid(pid, userService.isAdmin());
  }

  public List<Record> getTags(Integer pid)
  {
    return dao.getTags(pid);
  }
  
  public SolutionModel getSolution(Integer pid, Integer sid)
  {
    Integer uid = userService.getCurrentUid();
    StringBuilder sb = new StringBuilder("SELECT pid,uid,language,source FROM solution WHERE sid=? AND pid=?");
    
    if (!userService.isAdmin())
      sb.append(" AND uid=").append(uid);
    sb.append(" LIMIT 1");
    
    return SolutionModel.dao.findFirst(sb.toString(), sid, pid);
  }
  
  public boolean addProblem(ProblemModel problemModel) throws IOException
  {
    problemModel.set("uid", userService.getCurrentUid());
    problemModel.saveProblem();

    File dataDir = new File(new StringBuilder(3).append(OjConfig.get("data_path")).append("\\").append(problemModel.getInt("pid")).toString());
    if (dataDir.isDirectory())
    {
      log.warn("Data directory already exists: " + dataDir.getPath());
      return false;
    }
    
    FileUtil.mkdirs(dataDir);
    
    return true;
  }
  
  public List<SolutionModel> getProblemStatus(Integer pid)
  {
    List<SolutionModel> resultList = SolutionModel.dao.find("SELECT result,COUNT(*) AS count FROM solution WHERE pid=? GROUP BY result", pid);
    
    for (SolutionModel record : resultList)
    {
      try
      {
        ResultType resultType = (ResultType) OjConfig.result_type.get(record.getInt("result"));
        record.put("longName", resultType.getLongName());
        record.put("name", resultType.getName());
      } catch (NullPointerException e)
      {
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.warn(e.getLocalizedMessage());
      }
    }
    
    return resultList;
  }
  
  public Page<SolutionModel> getProblemStatusPage(int pageNumber, int pageSize, Integer language, Integer pid)
  {
    return SolutionModel.dao.getProblemStatusPage(pageNumber, pageSize, language, pid);
  }
  
  public Integer getRandomPid()
  {
    return dao.getRandomPid();
  }
  
  public Page<ProblemModel> searchProblem(int pageNumber, int pageSize, String scope, String word)
  {
    Page<ProblemModel> problemList = null;
    List<Object> paras = new ArrayList<Object>();
    String sql = "SELECT pid,title,accept,submit,source,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";

    if (StringUtil.isNotBlank(word))
    {
      word = new StringBuilder(3).append("%").append(word).append("%").toString();
      StringBuilder sb = new StringBuilder("FROM problem WHERE (");
      if (StringUtil.isNotBlank(scope))
      {
        String scopes[] = { "title", "source", "content", "tag" };
        if (StringUtil.equalsOneIgnoreCase(scope, scopes) == -1)
          return null;
        if ("tag".equalsIgnoreCase(scope))
        {
          sb.append("pid IN (SELECT pid FROM tag WHERE tag LIKE ? AND status=1)");
        } else if ("content".equalsIgnoreCase(scope))
        {
          sb.append("description LIKE ? ");
        } else
        {
          sb.append(scope).append(" LIKE ? ");
        }
        paras.add(word);
      } else
      {
        sb.append("title LIKE ? OR source LIKE ? OR description LIKE ?");
        paras.add(word);
        paras.add(word);
        paras.add(word);
      }
      sb.append(" ) AND status=1 ORDER BY accept desc,submit desc,pid");
      problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());
    }
    
    return problemList;
  }
  
  public List<Record> getUserInfo(Integer pid, Integer uid)
  {
    return dao.getUserInfo(pid, uid);
  }
  
  public Integer getUserResult(Integer pid)
  {
    Integer uid = userService.getCurrentUid();
    if (uid == null)
      return null;
    
    return Db.queryInt("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? LIMIT 1", uid, pid);
  }
  
  public Record getUserResult(Integer pid, Integer uid)
  {
    return dao.getUserResult(pid, uid);
  }
  
  public boolean addTag(Integer pid, String tag)
  {
    Integer uid = userService.getCurrentUid();
    return dao.addTag(pid, uid, tag);
  }

  public boolean build(Integer pid)
  {
    ProblemModel problemModel = findProblem(pid);
    if (problemModel == null)
      throw new ProblemException("Problem is not exist!");
    
    long accept = 0;
    long submit = 0;
    long ratio = 0;
    long submit_user = 0;
    long solved = 0;
    long difficulty = 0;

    Record record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE pid=? LIMIT 1", pid);

    if (record != null)
    {
      submit = record.getLong("count");
      problemModel.set("submit", submit);
    }

    record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE pid=? AND result=0 LIMIT 1", pid);
    if (record != null)
    {
      accept = record.getLong("count");
      problemModel.set("accept", accept);
    }

    if (submit > 0)
      ratio = accept / submit;
    problemModel.set("ratio", ratio);

    record = Db.findFirst("SELECT COUNT(uid) AS count FROM solution WHERE pid=? LIMIT 1", pid);
    if (record != null)
    {
      submit_user = record.getLong("count");
      problemModel.set("submit_user", submit_user);
    }

    record = Db.findFirst("SELECT COUNT(uid) AS count FROM solution WHERE pid=? AND result=0 LIMIT 1", pid);
    if (record != null)
    {
      solved = record.getLong("count");
      problemModel.set("solved", solved);
    }

    if (submit_user > 0)
      difficulty = solved / submit_user;
    problemModel.set("difficulty", difficulty);

    return problemModel.update();
  }
  
}
