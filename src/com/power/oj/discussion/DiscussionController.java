package com.power.oj.discussion;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;

public class DiscussionController extends OjController
{
  private static final DiscussionService discussionService = DiscussionService.me();
  
  public void index()
  {
    int pageNumber = getParaToInt(0, 1);
    int pageSize = getParaToInt(1, OjConfig.noticePageSize);
    Integer pid = getParaToInt("pid");
    
    setAttr("pageSize", OjConfig.noticePageSize);
    setAttr("topicList", discussionService.getTopicPage(pageNumber, pageSize, pid));
    setTitle(getText("discuss.index.title"));
  }
  
  public void show()
  {
    
  }
  
  public void add()
  {
    
  }
  
  @Before(POST.class)
  public void save()
  {
    TopicModel discussionModel = getModel(TopicModel.class, "topic");
    
    if (discussionService.addDiscussion(discussionModel))
    {
      redirect("/discuss");
    }
    else
    {
      redirect("/discuss");
    }
  }
  
  public void edit()
  {
    
  }
  
  @Before(POST.class)
  public void update()
  {
    
  }

  public void remove()
  {
    
  }
  
  public void delete()
  {
    
  }
  
}
