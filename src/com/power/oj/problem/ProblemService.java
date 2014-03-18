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
import com.power.oj.service.VisitCountService;
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
  
  public ProblemModel findProblem(Integer pid)
  {
    return dao.findByPid(pid, userService.isAdmin());
  }

  public ProblemModel findProblemForShow(Integer pid)
  {
    ProblemModel problemModel = dao.findByPid(pid, userService.isAdmin());
    
    if (problemModel == null)
      return null;
    
    int sample_input_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getSampleInput()))
      sample_input_rows = StringUtil.count(problemModel.getSampleInput(), '\n') + 1;
    problemModel.put("sample_input_rows", sample_input_rows);
    
    int sample_output_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getSampleOutput()))
      sample_output_rows = StringUtil.count(problemModel.getSampleOutput(), '\n') + 1;
    problemModel.put("sample_output_rows", sample_output_rows);
    problemModel.setView(VisitCountService.get(VisitCountService.problemViewCount, pid));

    return problemModel;
  }

  public ProblemModel findProblemForContest(Integer pid)
  {
    ProblemModel problemModel = dao.findById(pid);
    
    if (problemModel == null)
      return null;
    
    int sample_input_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getSampleInput()))
      sample_input_rows = StringUtil.count(problemModel.getSampleInput(), '\n') + 1;
    problemModel.put("sample_input_rows", sample_input_rows);
    
    int sample_output_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getSampleOutput()))
      sample_output_rows = StringUtil.count(problemModel.getSampleOutput(), '\n') + 1;
    problemModel.put("sample_output_rows", sample_output_rows);
    problemModel.setView(VisitCountService.get(VisitCountService.problemViewCount, pid));

    return problemModel;
  }
  
  public Long getProblemsNumber()
  {
    return Db.queryLong("SELECT COUNT(*) FROM problem WHERE status=1");
  }
  
  public int getNextPid(Integer pid)
  {
    return dao.getNextPid(pid, userService.isAdmin());
  }
  
  public int getPrevPid(Integer pid)
  {
    return dao.getPrevPid(pid, userService.isAdmin());
  }

  public Integer getRandomPid()
  {
    return dao.getRandomPid();
  }

  public Integer getViewCount(Integer pid)
  {
    return dao.getViewCount(pid);
  }

  public void setViewCount(Integer pid, Integer view)
  {
    dao.setViewCount(pid, view);
  }

  public List<Record> getTags(Integer pid)
  {
    return dao.getTags(pid);
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
    
    return Db.queryInt("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? AND cid=0 LIMIT 1", uid, pid);
  }
  
  public Record getUserResult(Integer pid, Integer uid)
  {
    return dao.getUserResult(pid, uid);
  }
  
  public <T> T getProblemField(Integer pid, String name)
  {
    String[] fields = {"title", "timeLimit", "memoryLimit", "description", "input", "output", "sampleInput", "sampleOutput", "hint", "source", "status"};
    if (StringUtil.equalsOne(name, fields) == -1)
    {
      return null;
    }
    
    ProblemModel problemModel = dao.findByPid(pid, false);
    if (problemModel == null)
    {
      return null;
    }
        
    return problemModel.get(name);
  }
  
  public Page<ProblemModel> getProblemPage(int pageNumber, int pageSize)
  {
    String sql = "SELECT pid,title,source,accepted,submission,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime_t,status";
    StringBuilder sb = new StringBuilder().append("FROM problem");
    if (!userService.isAdmin())
      sb.append(" WHERE status=1");
    sb.append(" ORDER BY pid");

    Page<ProblemModel> problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString());
    
    return problemList;
  }

  public Page<ProblemModel> getProblemPageDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir, String sSearch)
  {
    List<Object> param = new ArrayList<Object>();
    String sql = "SELECT pid,title,source,accepted,submission,ctime,status";
    StringBuilder sb = new StringBuilder().append("FROM problem WHERE 1=1");
    if (StringUtil.isNotEmpty(sSearch))
    {
      sb.append(" AND (pid LIKE ? OR title LIKE ?)");
      param.add(new StringBuilder(3).append(sSearch).append("%").toString());
      param.add(new StringBuilder(3).append("%").append(sSearch).append("%").toString());
    }
    sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", pid");

    Page<ProblemModel> problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), param.toArray());
    
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

  public SolutionModel getSolution(Integer pid, Integer sid)
  {
    Integer uid = userService.getCurrentUid();
    StringBuilder sb = new StringBuilder("SELECT pid,uid,language,source FROM solution WHERE sid=? AND pid=?");
    
    if (!userService.isAdmin())
      sb.append(" AND uid=").append(uid);
    sb.append(" LIMIT 1");
    
    return SolutionModel.dao.findFirst(sb.toString(), sid, pid);
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
  
  public Page<ProblemModel> searchProblem(int pageNumber, int pageSize, String scope, String word)
  {
    Page<ProblemModel> problemList = null;
    List<Object> paras = new ArrayList<Object>();
    String sql = "SELECT pid,title,accepted,submission,source,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime_t";

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
      sb.append(" ) AND status=1 ORDER BY accepted desc,submission desc,pid");
      problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());
    }
    
    return problemList;
  }
  
  public boolean addTag(Integer pid, String tag)
  {
    Integer uid = userService.getCurrentUid();
    return dao.addTag(pid, uid, tag);
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
  
  public int updateProblemByField(Integer pid, String name, String value)
  {
    // TODO store tags in problem table
    String[] fields = {"title", "timeLimit", "memoryLimit", "description", "input", "output", "sampleInput", "sampleOutput", "hint", "source", "status"};
    if (StringUtil.equalsOne(name, fields) == -1)
    {
      return -1;
    }
    
    long mtime = OjConfig.timeStamp;
    if (StringUtil.equalsOne(name, new String[]{"timeLimit", "memoryLimit", "status"}) != -1)
    {
      Integer intValue = Integer.parseInt(value);
      return Db.update("UPDATE problem SET `" + name + "`=?,`mtime`=? WHERE `pid`=?", intValue, mtime, pid);
    }
    
    return Db.update("UPDATE problem SET `" + name + "`=?,`mtime`=? WHERE `pid`=?", value, mtime, pid);
  }
  
  public boolean build(Integer pid)
  {
    ProblemModel problemModel = findProblem(pid);
    if (problemModel == null)
      throw new ProblemException("Problem is not exist!");
    
    long accepted = 0;
    long submission = 0;
    long ratio = 0;
    long submit_user = 0;
    long solved = 0;
    long difficulty = 0;

    Record record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE pid=? LIMIT 1", pid);

    if (record != null)
    {
      submission = record.getLong("count");
      problemModel.set("submission", submission);
    }

    record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE pid=? AND result=0 LIMIT 1", pid);
    if (record != null)
    {
      accepted = record.getLong("count");
      problemModel.set("accepted", accepted);
    }

    if (submission > 0)
      ratio = accepted / submission;
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
      problemModel.setSolved((int) solved);
    }

    if (submit_user > 0)
      difficulty = solved / submit_user;
    problemModel.setDifficulty((int) difficulty);

    return problemModel.update();
  }
  
}
