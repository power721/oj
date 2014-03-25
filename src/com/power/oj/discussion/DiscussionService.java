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
  
  public Page<CommentModel> getCommentList(int pageNumber, int pageSize, Integer threadId)
  {
    Page<CommentModel> commentList = CommentModel.dao.paginate(pageNumber, pageSize, 
        "SELECT c.*,u.name,u.avatar,FROM_UNIXTIME(c.ctime, '%Y-%m-%d %H:%i:%s') AS postDate", 
        "FROM `comment` c JOIN `user` u ON u.uid=c.uid WHERE threadId=? ORDER BY c.ctime DESC", threadId);
    
    return commentList;
  }
  
  public TopicModel findTopic(Integer id)
  {
    return dao.findFirst("SELECT * FROM `topic` WHERE id=?", id);
  }

  public TopicModel findTopic4Show(Integer id)
  {
    TopicModel topic = dao.findFirst("SELECT t.*,u.name,u.avatar,p.title AS problem FROM `topic` t "
        + "LEFT JOIN `user` u ON u.uid=t.uid LEFT JOIN problem p ON p.pid=t.pid WHERE id=?", id);
    topic.setView(topic.getView() + 1).update();
    return topic;
  }
  
  public boolean addDiscussion(TopicModel topicModel)
  {
    topicModel.setUid(userService.getCurrentUid());
    topicModel.setCtime(OjConfig.timeStamp);
    
    return topicModel.save();
  }
  
  public boolean updateDiscussion(TopicModel topicModel)
  {
    TopicModel newTopic = dao.findById(topicModel.getId());
    
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
