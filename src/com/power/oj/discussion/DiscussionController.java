package com.power.oj.discussion;

import org.apache.shiro.authz.annotation.RequiresUser;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;

public class DiscussionController extends OjController
{
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
    Integer id = getParaToInt(0);
    
    setAttr("topic", discussionService.findTopic4Show(id));
  }
  
  @RequiresUser
  public void add()
  {
    
  }
  
  @Before(POST.class)
  @RequiresUser
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
  
  @RequiresUser
  public void edit()
  {
    
  }
  
  @Before(POST.class)
  @RequiresUser
  public void update()
  {
    
  }

  @RequiresUser
  public void remove()
  {
    
  }
  
  @RequiresUser
  public void delete()
  {
    
  }
  
}
