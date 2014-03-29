package com.power.oj.api;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;

public class DiscussionApiController extends OjController
{
  public void comment()
  {
    
  }
  
  public void commentList()
  {
    Integer threadId = getParaToInt("threadId");
    int pageNumber = getParaToInt("pageNumber", 1);
    int pageSize = getParaToInt("pageSize", OjConfig.noticePageSize);
    
    renderJson(discussionService.getCommentList(pageNumber, pageSize, threadId));
  }
}
