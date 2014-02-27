package com.power.oj.notice;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;
import com.power.oj.user.UserService;

public class NoticeService
{
  private static final NoticeModel dao = NoticeModel.dao;
  private static final NoticeService me = new NoticeService();
  private static final UserService userService = UserService.me();
  
  private NoticeService() {}
  public static NoticeService me()
  {
    return me;
  }
  
  public NoticeModel getNotice(Integer id)
  {
    return dao.findFirst("SELECT n.*,u.name,FROM_UNIXTIME(start_time, '%Y-%m-%d %H:%i:%s') AS start_time_t,FROM_UNIXTIME(end_time, '%Y-%m-%d %H:%i:%s') AS end_time_t FROM notice n LEFT JOIN user u ON u.uid=n.uid WHERE id=? AND n.status=1", id);
  }
  
  public List<NoticeModel> getNoticeList()
  {
    return dao.find("SELECT title FROM notice WHERE start_time<=UNIX_TIMESTAMP() AND end_time>=UNIX_TIMESTAMP() AND status=1 ORDER BY id DESC");
  }
  
  public Page<NoticeModel> getNoticePage(int pageNumber, int pageSize)
  {
    String sql = "SELECT n.*,u.name";
    String from = "FROM notice n LEFT JOIN user u ON u.uid=n.uid WHERE start_time<=UNIX_TIMESTAMP() AND n.status=1 ORDER BY id DESC";
    
    return dao.paginate(pageNumber, pageSize, sql, from);
  }
  
  public boolean saveNotice(NoticeModel noticeModel)
  {
    NoticeModel newNotice = new NoticeModel();
    
    newNotice.set("uid", userService.getCurrentUid());
    newNotice.set("cid", noticeModel.get("cid"));
    newNotice.set("title", noticeModel.get("title"));
    newNotice.set("start_time", noticeModel.get("start_time"));
    newNotice.set("end_time", noticeModel.get("end_time"));
    newNotice.set("content", noticeModel.get("content"));
    newNotice.set("ctime", OjConfig.timeStamp);
    
    return newNotice.save();
  }
  
  public boolean updateNotice(NoticeModel noticeModel)
  {
    Integer id = noticeModel.getInt("id");
    NoticeModel newNotice = NoticeModel.dao.findById(id);

    newNotice.set("id", id);
    newNotice.set("editor", userService.getCurrentUid());
    newNotice.set("cid", noticeModel.get("cid"));
    newNotice.set("title", noticeModel.get("title"));
    newNotice.set("start_time", noticeModel.get("start_time"));
    newNotice.set("end_time", noticeModel.get("end_time"));
    newNotice.set("content", noticeModel.get("content"));
    newNotice.set("status", noticeModel.get("status"));
    newNotice.set("mtime", OjConfig.timeStamp);
    
    return newNotice.update();
  }
  
}
