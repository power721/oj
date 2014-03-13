package com.power.oj.discussion;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;

public class DiscussionController extends OjController
{
  private static final DiscussionService discussionService = DiscussionService.me();
  
  public void index()
  {
    int pageNumber = getParaToInt(0, 1);
    int pageSize = getParaToInt(1, OjConfig.userPageSize);
    Integer pid = getParaToInt("pid");
    
    setAttr("pageSize", OjConfig.userPageSize);
    setAttr("discussionList", discussionService.getDiscussionPage(pageNumber, pageSize, pid));
    setTitle(getText("discuss.index.title"));
  }
}
