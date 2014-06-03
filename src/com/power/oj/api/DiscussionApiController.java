package com.power.oj.api;

import com.power.oj.core.OjController;
import com.power.oj.discussion.TopicModel;

public class DiscussionApiController extends OjController
{
  
  public void index()
  {
    Integer id = getParaToInt("id");
    TopicModel topic = discussionService.findTopic4Show(id);
    
    setAttr("success", true);
    setAttr("topic", topic);
    setAttr("author", userService.getUser(topic.getUid()));
    
    renderJson(new String[]{"success", "topic", "author"});
  }
  
  public void comment()
  {
    renderNull();
  }
  /*
  public void commentList()
  {
    Integer threadId = getParaToInt("threadId");
    int pageNumber = getParaToInt("pageNumber", 1);
    int pageSize = getParaToInt("pageSize", OjConfig.noticePageSize);
    
    renderJson(discussionService.getCommentList(pageNumber, pageSize, threadId));
  }
  */
}
