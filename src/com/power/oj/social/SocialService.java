package com.power.oj.social;

import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Page;

public class SocialService
{
  private static final SocialService me = new SocialService();
  public static final FriendGroupModel dao = FriendGroupModel.dao;
  
  private SocialService() {}
  public static SocialService me()
  {
    return me;
  }
  
  public List<FriendGroupModel> getGroupList(Integer uid)
  {
    List<FriendGroupModel> groupList = dao.find("SELECT g.*,COUNT(f.user) AS count FROM friend_group g LEFT JOIN friend f ON f.gid=g.id WHERE f.user=? ORDER BY g.id", uid);
    if (groupList.size() > 0 && StringUtil.isEmpty(groupList.get(0).getStr("name")))
    {
      groupList.get(0).set("id", 0).set("name", "未分组");
    }
    return groupList;
  }
  
  public Page<FriendGroupModel> getFollowingList(int pageNumber, int pageSize, Integer uid, Integer gid)
  {
    String sql = "SELECT g.name AS groupName,f.*,u.name,u.solved,u.submit,u.comefrom,u.gender,u.sign,u.avatar,(SELECT 1 FROM friend ff WHERE ff.user=f.friend AND ff.friend=f.user) AS isFriend";
    String from = null;
  
    if (gid == null || gid < 0)
    {
      gid = -1;
      from = "FROM friend f LEFT JOIN friend_group g ON f.gid=g.id LEFT JOIN user u ON u.uid=f.friend WHERE f.user=? AND f.gid>?";
    }
    else
    {
      from = "FROM friend f LEFT JOIN friend_group g ON f.gid=g.id LEFT JOIN user u ON u.uid=f.friend WHERE f.user=? AND f.gid=?";
    }
    return dao.paginate(pageNumber, pageSize, sql, from, uid, gid);
  }
  
}
