package com.power.oj.discussion;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.user.UserService;

public class DiscussionService
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
    sb.append("FROM `topic` t LEFT JOIN `user` u ON u.uid=t.uid");
    if (pid != null && pid > 0)
    {
      sb.append(" WHERE pid=?");
      paras.add(pid);
    }
    sb.append(" ORDER BY id DESC");
    
    Page<TopicModel> topicPage = dao.paginate(pageNumber, pageSize, "SELECT t.*,u.name", sb.toString(), paras.toArray());
    
    for (TopicModel topic : topicPage.getList())
    {
      CommentModel comment = CommentModel.dao.findFirst("SELECT COUNT(*) AS reply,MAX(ctime) AS last FROM `comment` WHERE threadId=? LIMIT 1", topic.getId());
      topic.put("reply", comment.get("reply"));
      topic.put("last", comment.get("last"));
    }
    
    return topicPage;
  }
  
  public List<CommentModel> getCommentList(Integer thread)
  {
    List<CommentModel> commentList = CommentModel.dao.find("SELECT c.*,u.name,u.avatar FROM `comment` c JOIN `user` u ON u.uid=c.uid WHERE threadId=?", thread);
    
    return commentList;
  }
  
  public TopicModel findTopic(Integer id)
  {
    return dao.findFirst("SELECT t.*,u.name,u.avatar FROM `topic` t LEFT JOIN `user` u ON u.uid=t.uid WHERE id=?", id);
  }

  public TopicModel findTopic4Show(Integer id)
  {
    TopicModel topic = dao.findFirst("SELECT t.*,u.name,u.avatar FROM `topic` t LEFT JOIN `user` u ON u.uid=t.uid WHERE id=?", id);
    topic.setView(topic.getView() + 1).update();
    return topic;
  }
  
  public boolean addDiscussion(TopicModel topicModel)
  {
    TopicModel newTopic = new TopicModel();
    
    newTopic.setUid(userService.getCurrentUid());
    newTopic.setPid(topicModel.getPid());
    newTopic.setTitle(topicModel.getTitle());
    newTopic.setContent(topicModel.getContent());
    newTopic.setCtime(OjConfig.timeStamp);
    
    return newTopic.save();
  }
  
  public boolean updateDiscussion(TopicModel topicModel)
  {
    TopicModel newTopic = dao.findById(topicModel.getId());
    
    //newTopic.setUid(topicModel.getUid());
    newTopic.setPid(topicModel.getPid());
    newTopic.setTitle(topicModel.getTitle());
    newTopic.setContent(topicModel.getContent());
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
