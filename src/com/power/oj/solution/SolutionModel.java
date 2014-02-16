package com.power.oj.solution;

import com.jfinal.plugin.activerecord.Model;
import com.power.oj.contest.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
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
  
  public boolean addSolution()
  {
    long ctime = OjConfig.timeStamp;
    if (get("cid") != null)
    {
      int pid = ContestModel.dao.getPid(getInt("cid"), getInt("num"));
      set("pid", pid);
    }
    this.set("ctime", ctime).set("result", ResultType.WAIT).set("code_len", this.getStr("source").length());
    if (this.getInt("code_len") < 10 || this.getInt("code_len") > 30000)
    {
      // TODO throw exception
      return false;
    }

    return this.save();
  }
}
