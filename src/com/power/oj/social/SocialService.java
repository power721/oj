package com.power.oj.social;

import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;

public class SocialService
{
  private static final SocialService me = new SocialService();
  public static final FriendGroupModel dao = FriendGroupModel.dao;

  private SocialService()
  {
  }

  public static SocialService me()
  {
    return me;
  }

  public List<FriendGroupModel> getGroupList(Integer uid)
  {
    List<FriendGroupModel> groupList = dao
        .find(
            "(SELECT g.id,g.uid,g.name,COUNT(f.userId) AS count,g.ctime FROM friend_group g LEFT JOIN friend f ON f.userId=? AND f.gid=g.id WHERE g.uid=0) UNION (SELECT * FROM friend_group WHERE uid=? ORDER BY id)",
            uid, uid);
    if (groupList.size() > 0 && StringUtil.isEmpty(groupList.get(0).getStr("name")))
    {
      groupList.get(0).set("name", "未分组");
    }
    return groupList;
  }

  public Page<FriendGroupModel> getFollowingList(int pageNumber, int pageSize, Integer uid, Integer gid)
  {
    String sql = "SELECT g.name AS groupName,f.*,u.name,u.solved,u.submission,u.comeFrom,u.gender,u.signature,u.avatar,(SELECT 1 FROM friend ff WHERE ff.userId=f.friendUid AND ff.friendUid=f.userId) AS isFriend";
    String from = null;

    if (gid == null || gid <= 0)
    {
      gid = 0;
      from = "FROM friend f LEFT JOIN friend_group g ON f.gid=g.id LEFT JOIN user u ON u.uid=f.friendUid WHERE f.userId=? AND f.gid>?";
    } else
    {
      from = "FROM friend f LEFT JOIN friend_group g ON f.gid=g.id LEFT JOIN user u ON u.uid=f.friendUid WHERE f.userId=? AND f.gid=?";
    }
    return dao.paginate(pageNumber, pageSize, sql, from, uid, gid);
  }

  public Page<FriendGroupModel> getFollowedList(int pageNumber, int pageSize, Integer uid)
  {
    String sql = "SELECT g.name AS groupName,f.*,u.name,u.solved,u.submission,u.comeFrom,u.gender,u.signature,u.avatar,(SELECT 1 FROM friend ff WHERE ff.userId=f.friendUid AND ff.friendUid=f.userId) AS isFriend";
    String from = "FROM friend f LEFT JOIN friend_group g ON f.gid=g.id LEFT JOIN user u ON u.uid=f.userId WHERE f.friendUid=?";

    return dao.paginate(pageNumber, pageSize, sql, from, uid);
  }

  public boolean addGroup(Integer uid, String groupName)
  {
    FriendGroupModel group = dao.findFirst("SELECT * FROM friend_group WHERE uid=? AND name=?", uid, groupName);

    if (group == null)
    {
      group = new FriendGroupModel();
      group.set("uid", uid);
      group.set("name", groupName);
      group.set("ctime", OjConfig.timeStamp);
      return group.save();
    }
    return true;
  }

  public int updateGroup(Integer uid, Integer gid, String groupName)
  {
    if (dao.findFirst("SELECT * FROM friend_group WHERE uid=? AND name=?", uid, groupName) != null)
    {
      return -1;
    }
    return Db.update("UPDATE friend_group SET name=? WHERE uid=? AND id=? AND id>1", groupName, uid, gid);
  }
  
  public int deleteGroup(Integer uid, Integer gid)
  {
    return Db.update("DELETE FROM friend_group WHERE uid=? AND id=? AND id>1 AND count=0", uid, gid);
  }

  public boolean changeFollowingByGroup(Integer uid, Integer fid, Integer gid, Integer tgid)
  {
    if (gid.equals(tgid))
    {
      return true;
    }
    if (uid.equals(fid))
    {
      return false;
    }

    boolean result = Db.update("UPDATE friend SET gid=? WHERE userId=? AND friendUid=? AND gid=?", tgid, uid, fid, gid) > 0;
    if (result)
    {
      if (gid > 1)
      {
        Db.update("UPDATE friend_group SET count=count-1 WHERE id=?", gid);
      }
      if (tgid > 1)
      {
        Db.update("UPDATE friend_group SET count=count+1 WHERE id=?", tgid);
      }
      return true;
    }
    return false;
  }

  public boolean changeFollowingByGroup(Integer uid, String[] friendUid, String[] groupId, Integer tgid)
  {
    boolean result = true;
    for (int i = 0; i < friendUid.length; ++i)
    {
      Integer fid = Integer.parseInt(friendUid[i]);
      Integer gid = Integer.parseInt(groupId[i]);

      result = result && changeFollowingByGroup(uid, fid, gid, tgid);
    }

    return result;
  }
  
  public boolean addFriend(Integer uid, Integer fid, Integer gid)
  {
    FriendModel friend = FriendModel.dao.findFirst("SELECT * FROM friend WHERE userId=? AND friendUid=?", uid, fid);
    
    if (friend == null)
    {
      friend = new FriendModel();
      friend.setUserId(uid);
      friend.setFriendUid(fid);
      friend.setGid(gid);
      friend.setCtime(OjConfig.timeStamp);
      if(friend.save())
      {
        if (gid > 1)
        {
          Db.update("UPDATE friend_group SET count=count+1 WHERE id=?", gid);
        }
        return true;
      }
    }
    return false;
  }

  public boolean deleteFriend(Integer uid, Integer fid)
  {
    FriendModel friend = FriendModel.dao.findFirst("SELECT * FROM friend WHERE userId=? AND friendUid=?", uid, fid);
    
    if (friend != null)
    {
      Integer gid = friend.getInt("gid");
      if (friend.delete() && gid > 1)
      {
        Db.update("UPDATE friend_group SET count=count-1 WHERE id=?", gid);
      }
      return true;
    }
    return false;
  }

}
