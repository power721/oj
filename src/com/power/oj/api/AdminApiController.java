package com.power.oj.api;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.problem.ProblemService;

public class AdminApiController extends OjController
{
  
  private static final ProblemService problemService = ProblemService.me();
  
  public void problemList()
  {
    int pageNumber = getParaToInt("pageNumber", 1);
    int pageSize = getParaToInt("pageSize", OjConfig.problemPageSize);
    
    renderJson(problemService.getProblemPageDataTables(pageNumber, pageSize));
  }
  
}
