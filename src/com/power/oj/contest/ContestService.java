package com.power.oj.contest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.HtmlDecoder;
import jodd.util.StringUtil;
import org.apache.http.conn.HttpHostConnectException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import com.power.oj.user.UserService;
import com.power.oj.util.HttpUtil;

public class ContestService
{
  private static final Logger log = Logger.getLogger(ContestService.class);
  private static final ContestModel dao = ContestModel.dao;
  private static final ContestService me = new ContestService();
  private static final UserService userService = UserService.me();
  
  private ContestService() {}
  public static ContestService me()
  {
    return me;
  }

  public Page<ContestModel> getContestList(int pageNumber, int pageSize, Integer type, Integer status)
  {
    List<Object> paras = new ArrayList<Object>();
    String sql = "SELECT *,FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS start_time_t,FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS end_time_t";
    StringBuilder sb = new StringBuilder("FROM contest WHERE 1=1");
    if (type > -1)
    {
      sb.append(" AND type=?");
      paras.add(type);
    }
    // TODO only admin and attendee can see test contest

    if (status == ContestModel.PENDING)
    {
      sb.append(" AND startTime>UNIX_TIMESTAMP()");
    } else if (status == ContestModel.RUNNING)
    {
      sb.append(" AND startTime<UNIX_TIMESTAMP() AND endTime>UNIX_TIMESTAMP()");
    } else if (status == ContestModel.FINISHED)
    {
      sb.append(" AND endTime<UNIX_TIMESTAMP()");
    }

    sb.append(" ORDER BY cid DESC");

    Page<ContestModel> ContestList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    for (ContestModel contest : ContestList.getList())
    {
      long ctime = OjConfig.timeStamp;
      int startTime = contest.getStartTime();
      int endTime = contest.getEndTime();
      String cstatus = "Running";

      if (startTime > ctime)
        cstatus = "Pending";
      else if (endTime < ctime)
        cstatus = "Finished";

      contest.put("cstatus", cstatus);

      String ctype = "Public";
      if (contest.hasPassword())
      {
        ctype = "Password";
      } else if (contest.isPrivate())
      {
        ctype = "Private";
      } else if (contest.isStrictPrivate())
      {
        ctype = "Strict Private";
      } else if (contest.isTest())
      {
        ctype = "Test";
      }
      contest.put("ctype", ctype);
    }

    return ContestList;
  }

  public Page<ContestModel> getContestListDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir, String sSearch)
  {
    List<Object> paras = new ArrayList<Object>();
    String sql = "SELECT *";
    StringBuilder sb = new StringBuilder("FROM contest WHERE 1=1");
    /*if (type > -1)
    {
      sb.append(" AND type=?");
      paras.add(type);
    }*/

    if ("running".equals(sSearch.toLowerCase()))
    {
      sb.append(" AND startTime>UNIX_TIMESTAMP()");
    } else if ("pending".equals(sSearch.toLowerCase()))
    {
      sb.append(" AND startTime<UNIX_TIMESTAMP() AND endTime>UNIX_TIMESTAMP()");
    } else if ("finished".equals(sSearch.toLowerCase()))
    {
      sb.append(" AND endTime<UNIX_TIMESTAMP()");
    }

    sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", cid DESC");

    Page<ContestModel> ContestList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

    for (ContestModel contest : ContestList.getList())
    {
      long ctime = OjConfig.timeStamp;
      int startTime = contest.getInt("startTime");
      int endTime = contest.getInt("endTime");
      String cstatus = "Running";

      if (startTime > ctime)
        cstatus = "Pending";
      else if (endTime < ctime)
        cstatus = "Finished";

      contest.put("cstatus", cstatus);

      String ctype = "Public";
      if (contest.hasPassword())
      {
        ctype = "Password";
      } else if (contest.isPrivate())
      {
        ctype = "Private";
      } else if (contest.isStrictPrivate())
      {
        ctype = "Strict Private";
      } else if (contest.isTest())
      {
        ctype = "Test";
      }
      contest.put("ctype", ctype);
    }

    return ContestList;
  }

  public Page<Record> getContestRank(int pageNumber, int pageSize, Integer cid)
  {
    String sql = "FROM board b LEFT JOIN user u ON u.uid=b.uid WHERE b.cid=? ORDER BY solved DESC,penalty";
    Page<Record> userRank = Db.paginate(pageNumber, pageSize, "SELECT b.*,u.name,u.nick,u.realname", sql, cid);
    
    return userRank;
  }
  
  public ContestModel getContest(Integer cid)
  {
    ContestModel contestModle = dao.findFirst(
        "SELECT *,FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS start_time_t,FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS end_time_t FROM contest WHERE cid=? LIMIT 1",
        cid);
    return contestModle;
  }

  public String getContestTitle(Integer cid)
  {
    return Db.queryStr("SELECT title FROM contest WHERE cid=? LIMIT 1", cid);
  }

  public List<Record> getContestProblems(Integer cid, Integer uid)
  {
    String sql = "SELECT * FROM contest_problem WHERE cid=? ORDER BY num";
    List<Record> contestProblems;
    if (uid != null && uid > 0)
    {
      sql = "SELECT cp.pid,cp.num,cp.accept,cp.submit,title,status FROM contest_problem cp LEFT OUTER JOIN (SELECT pid,MIN(result) AS status FROM contest_solution WHERE uid=? AND cid=? GROUP BY pid)AS temp ON cp.pid=temp.pid WHERE cp.cid=? ORDER BY num";
      contestProblems = Db.find(sql, uid, cid, cid);
    } else
    {
      contestProblems = Db.find(sql, cid);
    }

    for (Record problem : contestProblems)
    {
      problem.set("id", (char) (problem.getInt("num") + 'A'));
    }

    return contestProblems;
  }

  public List<Record> getContestProblems(Integer cid)
  {
    List<Record> contestProblems = Db.find("SELECT p.*,cp.title,cp.num FROM contest_problem cp LEFT JOIN problem p ON p.pid=cp.pid WHERE cid=? ORDER BY num", cid);
    for (Record problem : contestProblems)
    {
      problem.set("id", (char) (problem.getInt("num") + 'A'));
    }

    return contestProblems;
  }
  
  public ProblemModel getProblem(Integer cid, Integer num)
  {
    Record record = Db.findFirst("SELECT pid,title,accept,submit FROM contest_problem WHERE cid=? AND num=? LIMIT 1", cid, num);
    if (record == null)
      return null;

    Integer pid = record.getInt("pid");
    ProblemModel problem = ProblemService.me().findProblemForContest(pid);
    if (problem == null)
      return null;
    
    String title = record.getStr("title");
    if (StringUtil.isNotBlank(title))
      problem.set("title", title);

    problem.set("accept", record.get("accept"));
    problem.set("submit", record.get("submit"));
    problem.set("submit_user", Db.queryLong("SELECT COUNT(uid) FROM contest_solution WHERE cid=? AND num=?", cid, num));
    problem.set("solved", Db.queryLong("SELECT COUNT(uid) FROM contest_solution WHERE result=0 AND cid=? AND num=?", cid, num));
    problem.put("id", (char) (num + 'A'));
    problem.put("num", num);

    return problem;
  }

  public Integer getPid(Integer cid, Integer num)
  {
    return Db.queryInt("SELECT pid FROM contest_problem WHERE cid=? AND num=? LIMIT 1", cid, num);
  }

  public String getProblemTitle(int cid, int num)
  {
    return Db.queryStr("SELECT title FROM contest_problem WHERE cid=? AND num=? LIMIT 1", cid, num);
  }

  public List<Record> getContestUsers(Integer cid)
  {
    return Db.find("SELECT c.*,u.name,u.realname,u.nick FROM contest_user c LEFT JOIN user u ON u.uid=c.uid WHERE cid=?", cid);
  }

  public boolean isUserInContest(Integer uid, Integer cid)
  {
    return Db.queryInt("SELECT uid FROM contest_user WHERE uid=? AND cid=? LIMIT 1", uid, cid) != null;
  }

  public Integer getUserResult(Integer cid, Integer num)
  {
    Integer uid = userService.getCurrentUid();
    if (uid == null)
      return null;
    
    return Db.queryInt("SELECT MIN(result) AS result FROM contest_solution WHERE cid=? AND uid=? AND num=? LIMIT 1", cid, uid, num);
  }
  
  public List<Record> getClarifyList(Integer cid)
  {
    return Db.find("SELECT c.*,u.name FROM contest_clarify c LEFT JOIN user u ON u.uid=c.uid WHERE cid=? ORDER BY id DESC", cid);
  }
  
  
  public List<Record> getPrivateClarifyList(Integer cid, Integer uid)
  {
    return Db.find("SELECT c.*,u.name FROM contest_clarify c LEFT JOIN user u ON u.uid=c.uid WHERE cid=? AND c.uid=? AND public=0 ORDER BY id DESC", cid, uid);
  }
  
  public List<Record> getPublicClarifyList(Integer cid)
  {
    return Db.find("SELECT c.*,u.name FROM contest_clarify c LEFT JOIN user u ON u.uid=c.uid WHERE cid=? AND public=1 ORDER BY id DESC", cid);
  }
  
  public boolean addClarify(Integer cid, String question)
  {
    Record clarify = new Record();
    
    clarify.set("cid", cid);
    clarify.set("uid", userService.getCurrentUid());
    clarify.set("question", question);
    clarify.set("ctime", OjConfig.timeStamp);
   
    return Db.save("contest_clarify", clarify);
  }
  
  public boolean updateClarify(Integer id, String reply, boolean isPublic)
  {
    Record clarify = Db.findById("contest_clarify", id);
    
    if (StringUtil.isNotBlank(clarify.getStr("reply")))
    {
      clarify.set("mtime", OjConfig.timeStamp);
    }
    else
    {
      clarify.set("admin", userService.getCurrentUid());
    }
    clarify.set("reply", reply);
    clarify.set("public", isPublic);
    clarify.set("atime", OjConfig.timeStamp);
    
    return Db.update("contest_clarify", clarify);
  }
  
  public String getRecentContest()
  {
    String json = null;
      List<ContestkendoSchedulerTask> contests = new ArrayList<ContestkendoSchedulerTask>();
      String html = null;
      try
      {
        html = HttpUtil.doGet("http://contests.acmicpc.info/contests.json");
      } catch(HttpHostConnectException e)
      {
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.info(e.getLocalizedMessage());
      }
      
      if (html == null)
      {
        try
        {
          html = HttpUtil.doGet("http://acm.nankai.edu.cn/contests.json");
        } catch(HttpHostConnectException e)
        {
          if (OjConfig.getDevMode())
            e.printStackTrace();
          log.info(e.getLocalizedMessage());
        }
      }
      if (html == null)
      {
        return null;
      }

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      long timeStamp = 0;

      JSONArray jsonArray;
      try
      {
        jsonArray = JSON.parseArray(html);
      } catch (JSONException e)
      {
        try
        {
          html = HttpUtil.doGet("http://contests.acmicpc.info/contests.json");
        } catch(HttpHostConnectException e1)
        {
          if (OjConfig.getDevMode())
            e1.printStackTrace();
          log.info(e1.getLocalizedMessage());
        }
        jsonArray = JSON.parseArray(html);
      }

      for (int i = 0; i < jsonArray.size(); ++i)
      {
        JSONObject data = jsonArray.getJSONObject(i);
        ContestkendoSchedulerTask contest = new ContestkendoSchedulerTask();
        try
        {
          timeStamp = sdf.parse(data.getString("startTime")).getTime();
        } catch (ParseException e)
        {
          timeStamp = 0;
          log.warn(e.getLocalizedMessage());
        }
        String start = "/Date(" + timeStamp + ")/";
        String end = "/Date(" + (timeStamp + 18000000) + ")/";
        String link = HtmlDecoder.decode(data.getString("link"));
        String title = data.getString("oj") + " -- " + data.getString("name");

        contest.setTaskId(data.getString("id"));
        contest.setOj(data.getString("oj"));
        contest.setTitle(title);
        contest.setUrl(link);
        contest.setDescription(link);
        contest.setStart(start);
        contest.setEnd(end);

        contests.add(contest);
      }
      json = JsonKit.listToJson(contests, 2);
    
    return json;
  }
  
  public int getContestStatus(Integer cid)
  {
    ContestModel contestModle = dao.findFirst("SELECT startTime,endTime FROM contest WHERE cid=? LIMIT 1", cid);
    if (contestModle == null)
      return -1;
    long ctime = OjConfig.timeStamp;
    int startTime = contestModle.getInt("startTime");
    int endTime = contestModle.getInt("endTime");

    if (startTime > ctime)
      return ContestModel.PENDING;
    else if (endTime < ctime)
      return  ContestModel.FINISHED;

    return  ContestModel.RUNNING;
  }

  public List<Record> getContestStatistics(Integer cid)
  {
    StringBuilder sb = new StringBuilder("SELECT ");
    for (LanguageModel language : OjConfig.program_languages)
    {
      sb.append("COUNT(IF(language=").append(language.getInt("id")).append(",1,NULL)) AS ").append(language.getStr("ext")).append(",");
    }
    for (ResultType resultType : OjConfig.judge_result)
    {
      if (resultType.getId() > 9)
        break;
      sb.append("COUNT(IF(result=").append(resultType.getId()).append(",1,NULL)) AS ").append(resultType.getName()).append(",");
    }
    sb.append("pid,num,COUNT(IF(result>9,1,NULL)) AS Others,COUNT(*) AS total FROM contest_solution WHERE cid=? GROUP BY pid ORDER BY num");
    List<Record> statistics = Db.find(sb.toString(), cid);
    for (Record record : statistics)
    {
      record.set("id", (char) (record.getInt("num") + 'A'));
    }

    return statistics;
  }
  
  public boolean updateContest(ContestModel contestModel)
  {
    ContestModel newContest = dao.findById(contestModel.getCid());
    
    if (contestModel.hasPassword())
    {
      if (StringUtil.isNotBlank(contestModel.getPassword()))
      {
        newContest.setPassword(contestModel.getPassword());
      }
      else if (newContest.getInt("type") != ContestModel.TYPE_PASSWORD)
      {
        return false;
      }
    }
    newContest.setTitle(contestModel.getTitle());
    newContest.setDescription(contestModel.getDescription());
    newContest.setReport(contestModel.getReport());
    newContest.setStartTime(contestModel.getStartTime());
    newContest.setEndTime(contestModel.getEndTime());
    newContest.setType(contestModel.getType());
    newContest.setFreeze(contestModel.getFreeze());
    newContest.setMtime(OjConfig.timeStamp);
    
    return contestModel.update();
  }
  
  public int addProblem(Integer cid, Integer pid, String title)
  {
    if (isContestFinished(cid))
    {
      return 4;
    }
    
    if(Db.queryInt("SELECT pid FROM problem WHERE pid=? AND status=1", pid) == null)
    {
      return 3;
    }
    
    if (Db.queryInt("SELECT id FROM contest_problem WHERE cid=? AND pid=?", cid, pid) != null)
    {
      return 2;
    }
    
    Long num = Db.queryLong("SELECT MAX(num)+1 FROM contest_problem WHERE cid=?", cid);
    if (num == null)
    {
      num = 0L;
    }
    if (num >= OjConstants.MAX_PROBLEMS_IN_CONTEST)
    {
      return 1;
    }
    
    Record record = new Record();
    record.set("cid", cid);
    record.set("pid", pid);
    record.set("title", title);
    record.set("num", num);
    
    if (Db.save("contest_problem", record))
    {
      return 0;
    }
    return -1;
  }

  public int removeProblem(Integer cid, Integer pid)
  {
    if (!isContestPending(cid))
    {
      return -1;
    }
    
    int num = Db.queryInt("SELECT num FROM contest_problem WHERE cid=? AND pid=?", cid, pid);
    int result = Db.update("DELETE FROM contest_problem WHERE cid=? AND pid=?", cid, pid);
    List<Record> problems = Db.find("SELECT * FROM contest_problem WHERE cid=? AND num>? ORDER BY num", cid, num);
    
    for (Record problem : problems)
    {
      problem.set("num", num++);
      Db.update("contest_problem", problem);
    }
    
    return result;
  }
  
  public int reorderProblem(Integer cid, String str)
  {
    if (!isContestPending(cid))
    {
      return -1;
    }
    
    int result = 0;
    int i = 0;
    String[] pids = str.split(",");
    for (String s_pid : pids)
    {
      Integer pid = Integer.parseInt(s_pid);
      result += Db.update("UPDATE contest_problem SET num=? WHERE cid=? AND pid=?", i++, cid, pid);
    }
    return result;
  }
  
  public int addUser(Integer cid, Integer uid)
  {
    if (isContestFinished(cid))
    {
      return 4;
    }
    
    if(Db.queryInt("SELECT uid FROM user WHERE uid=? AND status=1", uid) == null)
    {
      return 3;
    }
    
    if (Db.queryInt("SELECT id FROM contest_user WHERE cid=? AND uid=?", cid, uid) != null)
    {
      return 2;
    }

    Record record = new Record();
    record.set("cid", cid);
    record.set("uid", uid);
    record.set("ctime", OjConfig.timeStamp);
    
    if (Db.save("contest_user", record))
    {
      return 0;
    }
    return 1;
  }

  public int removeUser(Integer cid, Integer uid)
  {
    return Db.update("DELETE FROM contest_user WHERE cid=? AND uid=?", cid, uid);
  }
  
  public boolean buildRank(Integer cid)
  {
    Db.update("DELETE FROM board WHERE cid=?", cid);
    ContestModel contestModle = getContest(cid);
    int contestStartTime = contestModle.getInt("startTime");
    List<Record> solutions = Db.find("SELECT uid,pid,num,result,ctime FROM contest_solution WHERE cid=? ORDER BY sid", cid);
    HashMap<Object, UserInfo> userRank = new HashMap<Object, UserInfo>();
    UserInfo userInfo = null;
    int uid = 0;
    int num = 0;
    int result = 0;
    int ctime = 0;
    int penalty = 0;
    int firstBoold[] = new int[26];
    int firstBooldTime[] = new int[26];
    for (int i = 0; i < 26; ++i)
      firstBooldTime[i] = -1;
    int accept[] = new int[26];
    int submit[] = new int[26];
    for (Record solution : solutions)
    {
      ++submit[num];
      uid = solution.getInt("uid");
      num = solution.getInt("num");
      result = solution.getInt("result");
      ctime = solution.getInt("ctime");
      userInfo = (UserInfo) userRank.get(uid);
      penalty = (ctime - contestStartTime) / 60;
      if (result == ResultType.AC && firstBoold[num] == 0)
      {
        firstBoold[num] = uid;
        firstBooldTime[num] = penalty;
      }
      if (userInfo == null)
      {
        userInfo = new UserInfo(uid);
        if (result == ResultType.AC)
        {
          ++userInfo.solved;
          ++accept[num];
          userInfo.acTime[num] = penalty;
          userInfo.penalty += penalty;
        } else
        {
          ++userInfo.waTime[num];
        }
        userRank.put(uid, userInfo);
      } else if (userInfo.acTime[num] == 0)
      {
        if (result == ResultType.AC)
        {
          ++userInfo.solved;
          ++accept[num];
          userInfo.acTime[num] = penalty;
          penalty += userInfo.waTime[num] * 20;
          userInfo.penalty += penalty;
        } else
        {
          ++userInfo.waTime[num];
        }
      }
    }

    for (Map.Entry<Object, UserInfo> entry : userRank.entrySet())
    {
      userInfo = entry.getValue();
      List<Object> paras = new ArrayList<Object>();
      paras.add(cid);
      paras.add(userInfo.uid);
      paras.add(userInfo.solved);
      paras.add(userInfo.penalty);
      StringBuilder fields = new StringBuilder();
      StringBuilder values = new StringBuilder();
      for (int i = 0; i < 26; ++i)
      {
        char c = (char) (i + 'A');
        if (userInfo.acTime[i] > 0)
        {
          fields.append(",").append(c).append("_time");
          values.append(",?");
          paras.add(userInfo.acTime[i]);
        }

        if (userInfo.waTime[i] > 0)
        {
          fields.append(",").append(c).append("_WrongSubmits");
          paras.add(userInfo.waTime[i]);
          values.append(",?");
        }
      }
      StringBuilder sb = new StringBuilder(5).append("INSERT INTO board (cid,uid,solved,penalty").append(fields.toString()).append(") VALUES (?,?,?,?")
          .append(values.toString()).append(")");
      Db.update(sb.toString(), paras.toArray());
    }
    for (int i = 0; i < OjConstants.MAX_PROBLEMS_IN_CONTEST; ++i)
    {
      Db.update("UPDATE contest_problem SET first_blood=?,first_blood_time=?,accept=?,submit=? WHERE cid=? AND num=?", firstBoold[i], firstBooldTime[i],
          accept[i], submit[i], cid, i);
    }

    return true;
  }

  public boolean isContestPending(Integer cid)
  {
    return dao.findFirst("SELECT 1 FROM contest WHERE cid=? AND startTime>UNIX_TIMESTAMP() LIMIT 1", cid) != null;
  }

  public boolean isContestFinished(Integer cid)
  {
    return dao.findFirst("SELECT 1 FROM contest WHERE cid=? AND endTime<UNIX_TIMESTAMP() LIMIT 1", cid) != null;
  }

  public boolean isContestHasPassword(Integer cid)
  {
    return dao.findFirst("SELECT 1 FROM contest WHERE cid=? AND type=1 LIMIT 1", cid) != null;
  }

  public boolean checkContestPassword(Integer cid, String password)
  {
    return dao.findFirst("SELECT 1 FROM contest WHERE cid=? AND password=? AND type=1 LIMIT 1", cid, password) != null;
  }

  public class UserInfo
  {
    public int cid;
    public int uid;
    public int solved = 0;
    public int penalty = 0;
    public int acTime[] = new int[26];
    public int waTime[] = new int[26];

    UserInfo()
    {
    }

    UserInfo(int uid)
    {
      this.uid = uid;
    }
  }
  
}
