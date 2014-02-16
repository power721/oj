package com.power.oj.contest;

import com.jfinal.plugin.activerecord.Model;
import com.power.oj.core.OjConfig;

public class ContestModel extends Model<ContestModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 2557500234914909223L;
  
  public static final ContestModel dao = new ContestModel();
  public static final int Public = 0;
  public static final int Private = 1;
  public static final int StrictPrivate = 2;
  public static final int Password = 3;
  public static final int Test = 4;

  public static final int Pending = 0;
  public static final int Running = 1;
  public static final int Finished = 2;

  public boolean saveContest()
  {
    long ctime = OjConfig.timeStamp;
    this.set("ctime", ctime);

    return this.save();
  }
  
}
