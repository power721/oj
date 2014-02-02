package com.power.oj.problem;

import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConstants;
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
  
  public Page<ProblemModel> getProblems(int pageNumber, int pageSize)
  {
    String sql = "SELECT pid,title,source,accept,submit,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime,status";
    StringBuilder sb = new StringBuilder("FROM problem");
    if (!userService.isAdmin())
      sb.append(" WHERE status=1");
    sb.append(" ORDER BY pid");

    Page<ProblemModel> problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString());
    
    return problemList;
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
  
  public Integer getUserResult(Integer pid)
  {
    Integer uid = userService.getCurrentUid();
    if (uid == null)
      return null;
    
    return Db.queryInt("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? LIMIT 1", uid, pid);
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
  
}
