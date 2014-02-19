package com.power.oj.api;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.problem.ProblemService;
import com.power.oj.user.UserService;

public class AdminApiController extends OjController
{
  private static final UserService userService = UserService.me();
  private static final ProblemService problemService = ProblemService.me();
  
  public void problemList()
  {
    int iDisplayStart = getParaToInt("iDisplayStart", 0);
    int pageSize = getParaToInt("iDisplayLength", OjConfig.problemPageSize);
    int pageNumber = iDisplayStart / pageSize + 1;
    int iSortCol = getParaToInt("iSortCol_0", 0);
    String sSortDir = getPara("sSortDir_0");
    String sSortName = getPara("mDataProp_" + iSortCol);
    String sSearch = getPara("sSearch");
    
    renderJson(problemService.getProblemPageDataTables(pageNumber, pageSize, sSortName, sSortDir, sSearch));
  }
  
  public void userList()
  {
    int iDisplayStart = getParaToInt("iDisplayStart", 0);
    int pageSize = getParaToInt("iDisplayLength", OjConfig.problemPageSize);
    int pageNumber = iDisplayStart / pageSize + 1;
    int iSortCol = getParaToInt("iSortCol_0", 0);
    String sSortDir = getPara("sSortDir_0");
    String sSortName = getPara("mDataProp_" + iSortCol);
    String sSearch = getPara("sSearch");
    
    renderJson(userService.getUserRankListDataTables(pageNumber, pageSize, sSortName, sSortDir, sSearch));
  }
  
}
