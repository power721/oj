package com.power.oj.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.contest.ContestModel;
import com.power.oj.contest.ContestService;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.user.UserService;

@RequiresPermissions("admin")
public class ContestAdminController extends OjController
{
  private static final UserService userService = UserService.me();
  private static final ContestService contestService = ContestService.me();
  
  public void index()
  {
    int pageNumber = getParaToInt(0, 1);
    int pageSize = getParaToInt(1, OjConfig.contestPageSize);
    Integer type = getParaToInt("type", -1);
    Integer status = getParaToInt("status", -1);

    setAttr("contestList", contestService.getPage(pageNumber, pageSize, type, status));
    setAttr("pageSize", OjConfig.contestPageSize);

    setTitle(getText("contest.index.title"));
  }
  
  @RequiresPermissions("contest:add")
  public void add()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    long ctime = OjConfig.startInterceptorTime + 3600000;
    setAttr("start_time", sdf.format(new Date(ctime)));
    setAttr("end_time", sdf.format(new Date(ctime + 18000000)));
    
    setTitle(getText("contest.add.title"));
  }

  @Before(POST.class)
  @RequiresPermissions("contest:add")
  public void save()
  {
    String start_time = getPara("start_time");
    String end_time = getPara("end_time");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    ContestModel contestModel = getModel(ContestModel.class, "contest");
    contestModel.set("uid", userService.getCurrentUid());
    try
    {
      contestModel.set("start_time", sdf.parse(start_time).getTime() / 1000);
      contestModel.set("end_time", sdf.parse(end_time).getTime() / 1000);
      log.info(contestModel.get("end_time").toString());
    } catch (ParseException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
    contestModel.saveContest();

    redirect(new StringBuilder(2).append("/contest/show/").append(contestModel.getInt("cid")).toString());
  }

  @RequiresPermissions("contest:edit")
  public void edit()
  {
    Integer cid = getParaToInt(0);
    ContestModel contestModle = contestService.getContest(cid);
    
    setAttr("cid", cid);
    setAttr("contest", contestModle);
    
    setTitle(getText("contest.edit.title"));
  }

  @Before(POST.class)
  @RequiresPermissions("contest:edit")
  public void update()
  {
    String start_time = getPara("start_time");
    String end_time = getPara("end_time");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ContestModel contestModel = getModel(ContestModel.class, "contest");
    
    try
    {
      contestModel.set("start_time", sdf.parse(start_time).getTime() / 1000);
      contestModel.set("end_time", sdf.parse(end_time).getTime() / 1000);
    } catch (ParseException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
    }
    
    contestService.updateContest(contestModel);
    redirect(new StringBuilder(2).append("/contest/show/").append(contestModel.getInt("cid")).toString());
  }

  @RequiresPermissions("contest:build")
  public void buildRank()
  {
    Integer cid = getParaToInt(0);
    contestService.buildRank(cid);

    redirect(new StringBuilder(2).append("/contest/rank/").append(cid).toString(), new FlashMessage(getText("contest.buildRank.success")));
  }

}
