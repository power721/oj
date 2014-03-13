package com.power.oj.discussion;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.user.UserService;

public class DiscussionService
{
  private static final Logger log = Logger.getLogger(DiscussionService.class);
  private static final DiscussionModel dao = DiscussionModel.dao;
  private static final DiscussionService me = new DiscussionService();
  private static final UserService userService = UserService.me();
  
  private DiscussionService() {}
  
  public static DiscussionService me()
  {
    return me;
  }
  
  public Page<DiscussionModel> getDiscussionPage(int pageNumber, int pageSize, Integer pid)
  {
    List<Object> paras = new ArrayList<Object>();
    StringBuilder sb = new StringBuilder();
    sb.append("FROM discussion d LEFT JOIN user u ON u.uid=d.uid WHERE threadId=0");
    if (pid != null && pid > 0)
    {
      sb.append(" AND pid=?");
      paras.add(pid);
    }
    
    Page<DiscussionModel> discussionPage = dao.paginate(pageNumber, pageSize, "SELECT d.*,u.name", sb.toString(), paras.toArray());
    
    return discussionPage;
  }
  
  public List<DiscussionModel> getDiscussionList(Integer thread)
  {
    List<DiscussionModel> discussionList = dao.find("SELECT * FROM discussion WHERE thread=?", thread);
    
    return discussionList;
  }
  
  public DiscussionModel getDiscussion(Integer id)
  {
    return dao.findFirst("SELECT d.*,u.name,u.avatar FROM discussion d LEFT JOIN user u ON u.uid=d.uid WHERE id=?", id);
  }
  
  public boolean addDiscussion(DiscussionModel discussionModel)
  {
    DiscussionModel newDiscussion = new DiscussionModel();
    
    newDiscussion.set("uid", userService.getCurrentUid());
    newDiscussion.set("pid", discussionModel.get("pid"));
    newDiscussion.set("quoteId", discussionModel.get("quoteId"));
    newDiscussion.set("threadId", discussionModel.get("threadId"));
    newDiscussion.set("title", discussionModel.get("title"));
    newDiscussion.set("content", discussionModel.get("content"));
    newDiscussion.set("ctime", OjConfig.timeStamp);
    
    return newDiscussion.save();
  }
  
  public boolean updateDiscussion(DiscussionModel discussionModel)
  {
    DiscussionModel newDiscussion = dao.findById(discussionModel.get("id"));
    
    newDiscussion.set("uid", discussionModel.get("uid"));
    newDiscussion.set("pid", discussionModel.get("pid"));
    newDiscussion.set("quoteId", discussionModel.get("quoteId"));
    newDiscussion.set("threadId", discussionModel.get("threadId"));
    newDiscussion.set("title", discussionModel.get("title"));
    newDiscussion.set("content", discussionModel.get("content"));
    newDiscussion.set("mtime", OjConfig.timeStamp);
    
    return newDiscussion.update();
  }
  
  public boolean removeDiscussion(DiscussionModel discussionModel)
  {
    discussionModel.set("status", false);
    return discussionModel.update();
  }

  public boolean deleteDiscussion(DiscussionModel discussionModel)
  {
    return discussionModel.delete();
  }
}
