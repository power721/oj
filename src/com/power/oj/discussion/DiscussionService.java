package com.power.oj.discussion;

import java.util.ArrayList;
import java.util.List;

import jodd.util.HtmlEncoder;

import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.user.UserService;

public final class DiscussionService
{
  private static final TopicModel dao = TopicModel.dao;
  private static final DiscussionService me = new DiscussionService();
  private static final UserService userService = UserService.me();
  
  private DiscussionService() {}
  
  public static DiscussionService me()
  {
    return me;
  }
  
  public Page<TopicModel> getTopicPage(int pageNumber, int pageSize, Integer pid)
  {
    List<Object> paras = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("FROM `topic`");
    if (pid != null && pid > 0)
    {
      sb.append(" WHERE pid=?");
      paras.add(pid);
    }
    sb.append(" GROUP BY threadId ORDER BY MAX(id) DESC");
    
    Page<TopicModel> topicPage = dao.paginate(pageNumber, pageSize, "SELECT *", sb.toString(), paras.toArray());
    
    return topicPage;
  }
  
  public List<TopicModel> getTopicList(Page<TopicModel> topicPage)
  {
    List<TopicModel> topicList = new ArrayList<TopicModel>();
    
    for (TopicModel topic : topicPage.getList())
    {
      topicList.addAll(dao.find("SELECT t.*,u.name FROM `topic` t LEFT JOIN `user` u ON u.uid=t.uid WHERE t.threadId=? ORDER BY t.id", topic.getThreadId()));
    }
    return topicList;
  }
  /*
  public Page<CommentModel> getCommentList(int pageNumber, int pageSize, Integer threadId)
  {
    Page<CommentModel> commentList = CommentModel.dao.paginate(pageNumber, pageSize, 
        "SELECT c.*,u.name,u.avatar,FROM_UNIXTIME(c.ctime, '%Y-%m-%d %H:%i:%s') AS postDate", 
        "FROM `comment` c JOIN `user` u ON u.uid=c.uid WHERE threadId=? ORDER BY c.ctime DESC", threadId);
    
    return commentList;
  }
  */
  public TopicModel findTopic(Integer id)
  {
    return dao.findFirst("SELECT * FROM `topic` WHERE id=?", id);
  }

  public TopicModel findTopic4Show(Integer id)
  {
    TopicModel topic = dao.findFirst("SELECT t.*,p.title AS problem,u.name FROM `topic` t "
                            + "LEFT JOIN problem p ON p.pid=t.pid "
                            + "LEFT JOIN user u ON u.uid=t.uid WHERE t.id=?", id);
    //topic.setView(topic.getView() + 1).update();
    return topic;
  }
  
  public boolean addDiscussion(TopicModel topicModel)
  {
    topicModel.setUid(userService.getCurrentUid());
    topicModel.setCtime(OjConfig.timeStamp);
    topicModel.setTitle(HtmlEncoder.text(topicModel.getTitle()));
    topicModel.setContent(HtmlEncoder.text(topicModel.getContent()));
    
    if (topicModel.save())
    {
      Integer threadId = topicModel.getThreadId();
      if (threadId == null || threadId == 0)
      {
        topicModel.setThreadId(topicModel.getId());
        return topicModel.update();
      }
      return true;
    }
    return false;
  }
  
  public boolean updateDiscussion(TopicModel topicModel)
  {
    TopicModel newTopic = dao.findById(topicModel.getId());
    
    newTopic.setTitle(topicModel.getTitle());
    newTopic.setContent(HtmlEncoder.text(topicModel.getContent()));
    newTopic.setMtime(OjConfig.timeStamp);
    
    return newTopic.update();
  }
  
  public boolean removeDiscussion(TopicModel topicModel)
  {
    topicModel.setStatus(false);
    return topicModel.update();
  }

  public boolean deleteDiscussion(TopicModel topicModel)
  {
    return topicModel.delete();
  }
}
