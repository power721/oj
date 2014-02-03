package com.power.oj.problem;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;

public class ProblemModel extends Model<ProblemModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 1943890587086216047L;
  
  public static final ProblemModel dao = new ProblemModel();

  public int getNextPid(Integer pid, boolean isAdmin)
  {
    int nextPid = 0;
    StringBuilder sb = new StringBuilder("SELECT pid FROM problem WHERE pid>?");
    if (!isAdmin)
      sb.append(" AND status=1");
    sb.append(" ORDER BY pid LIMIT 1");

    try
    {
      nextPid = dao.findFirst(sb.toString(), pid).getInt("pid");
    } catch (Exception e)
    {
      nextPid = pid;
    }
    return nextPid;
  }

  public int getPrevPid(Integer pid, boolean isAdmin)
  {
    int prevPid = 0;
    StringBuilder sb = new StringBuilder("SELECT pid FROM problem WHERE pid<?");
    if (!isAdmin)
      sb.append(" AND status=1");
    sb.append(" ORDER BY pid DESC LIMIT 1");

    try
    {
      prevPid = dao.findFirst(sb.toString(), pid).getInt("pid");
    } catch (Exception e)
    {
      prevPid = pid;
    }
    return prevPid;
  }

  public Integer getRandomPid()
  {
    Integer pid = 0;
    pid = dao
        .findFirst(
            "SELECT t1.pid FROM `problem` AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(pid) FROM `problem`)-(SELECT MIN(pid) FROM `problem`))+(SELECT MIN(pid) FROM `problem`)) AS pid) AS t2 WHERE t1.pid >= t2.pid AND status=1 ORDER BY t1.pid LIMIT 1")
        .getInt("pid");
    return pid;
  }

  public String getProblemTitle(Integer pid)
  {
    ProblemModel problemModel = findFirst("SELECT title FROM problem WHERE pid=? AND status=1 LIMIT 1", pid);
    String title = null;
    if (problemModel != null)
      title = problemModel.getStr("title");

    return title;
  }

  public List<Record> getUserInfo(Integer pid, Integer uid)
  {
    List<Record> userInfo = Db.find("SELECT uid,sid,pid,cid,result,ctime,num,time,memory,code_len,language FROM solution WHERE uid=? AND pid=? GROUP BY result", uid, pid);
    return userInfo;
  }

  public Record getUserResult(Integer pid, Integer uid)
  {
    Record record = Db.findFirst("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? LIMIT 1", uid, pid);
    return record;
  }

  public List<Record> getTags(Integer pid)
  {
    List<Record> tagList = Db.find("SELECT tag.tag,user.name FROM tag LEFT JOIN user on user.uid=tag.uid WHERE tag.pid=? AND tag.status=1", pid);
    if (tagList.isEmpty())
      return null;
    return tagList;
  }

  public ProblemModel findByPid(Integer pid, boolean isAdmin)
  {
    ProblemModel problemModel = null;
    if (isAdmin)
      problemModel = dao.findById(pid);
    else
      problemModel = dao.findFirst("SELECT * FROM problem WHERE status=1 AND pid=? LIMIT 1", pid);

    return problemModel;
  }

  public boolean updateProblem()
  {
    long mtime = OjConfig.timeStamp;
    this.set("mtime", mtime);
    if (this.get("status") == null)
      this.set("status", false);

    return this.update();
  }

  public boolean saveProblem()
  {
    long ctime = OjConfig.timeStamp;
    this.set("ctime", ctime).set("mtime", ctime);
    if (this.get("status") == null)
      this.set("status", false);

    return this.save();
  }

  public boolean incViewCount()
  {
    this.set("view", this.getInt("view") + 1);
    return this.update();
  }
  
  public Integer getViewCount(Integer pid)
  {
    return Db.queryInt("SELECT view FROM problem WHERE pid=? LIMIT 1", pid);
  }

  public void setViewCount(Integer pid, int view)
  {
    Db.update("UPDATE problem SET view=? WHERE pid=?", view, pid);
  }

  public boolean addTag(Integer pid, Integer uid, String tag)
  {
    Record Tag = new Record().set("pid", pid).set("uid", uid).set("tag", tag).set("ctime", OjConfig.timeStamp);
    return Db.save("tag", Tag);
  }

}
