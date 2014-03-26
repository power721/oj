package com.power.oj.discussion;

import org.apache.shiro.authz.annotation.RequiresUser;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;

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
    TopicModel topic = discussionService.findTopic4Show(id);
    
    setAttr("topic", topic);
    setAttr("author", userService.getUser(topic.getUid()));
  }
  
  @RequiresUser
  public void add()
  {
    setTitle(getText("discuss.add.title"));
  }
  
  @Before(POST.class)
  @RequiresUser
  public void save()
  {
    TopicModel topicModel = getModel(TopicModel.class, "topic");
    
    if (discussionService.addDiscussion(topicModel))
    {
      // TODO
    }
    else
    {
      
    }
    redirect("/discuss/show/" + topicModel.getId());
  }
  
  @RequiresUser
  public void edit()
  {
    Integer id = getParaToInt(0);
    Integer uid = userService.getCurrentUid();
    TopicModel topicModel = discussionService.findTopic(id);
    
    if (topicModel != null && (userService.isAdmin() || uid.equals(topicModel.getUid())))
    {
      setAttr("topic", topicModel);
    }
    else
    {
      redirect("/discuss/show/" + topicModel.getId(), new FlashMessage("Access Denied.", MessageType.ERROR, getText("message.error.title")));
    }
  }
  
  @Before(POST.class)
  @RequiresUser
  public void update()
  {
    TopicModel topicModel = getModel(TopicModel.class, "topic");
    
    if (discussionService.updateDiscussion(topicModel))
    {
      // TODO
    }
    else
    {
      
    }
    redirect("/discuss/show/" + topicModel.getId());
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
