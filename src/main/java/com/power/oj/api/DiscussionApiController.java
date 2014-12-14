package com.power.oj.api;

import com.power.oj.core.OjController;
import com.power.oj.discussion.TopicModel;

public class DiscussionApiController extends OjController
{
  
  public void index()
  {
    Integer id = getParaToInt("id");
    TopicModel topic = discussionService.findTopic4Show(id);
    
    topic.put("success", true);
    
    renderJson("topic", topic);
  }
  
  public void comment()
  {
    renderNull();
  }
  
}
