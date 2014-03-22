package com.power.oj.api;

import com.jfinal.aop.Before;
import com.power.oj.admin.AdminService;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;

@Before(AdminInterceptor.class)
public class AdminApiController extends OjController
{
  private static final AdminService adminService = AdminService.me();
  
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

  public void contestList()
  {
    int iDisplayStart = getParaToInt("iDisplayStart", 0);
    int pageSize = getParaToInt("iDisplayLength", OjConfig.problemPageSize);
    int pageNumber = iDisplayStart / pageSize + 1;
    int iSortCol = getParaToInt("iSortCol_0", 0);
    String sSortDir = getPara("sSortDir_0");
    String sSortName = getPara("mDataProp_" + iSortCol);
    String sSearch = getPara("sSearch");
    
    renderJson(contestService.getContestListDataTables(pageNumber, pageSize, sSortName, sSortDir, sSearch));
  }
  
  public void updateProblem()
  {
    Integer pid = getParaToInt("pk");
    String name = getPara("name");
    String value = getPara("value");
    
    if (problemService.updateProblemByField(pid, name, value))
    {
      renderNull();
    }
    else
    {
      renderError(400);
    }
  }
  
  public void updateConfig()
  {
    String name = getPara("name");
    String value = getPara("value");
    String type = getPara("type", "string");
    int result = adminService.updateConfig(name, value, type);
    
    if (result == 0)
    {
      renderJson("success:true");
    }
    else
    {
      renderJson("success:false, result:" + result);
    }
  }
  
}
