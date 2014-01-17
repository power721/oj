package com.power.oj.user;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class UserModel extends Model<UserModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 7553341600472286034L;
  
  public static final UserModel dao = new UserModel();

  public Integer getUid()
  {
    return getInt("uid");
  }

  public Integer getUidByName(String name)
  {
    Integer uid = 0;
    UserModel userModel = findFirst("SELECT uid FROM user WHERE name=? LIMIT 1", name);
    if (userModel != null)
      uid = userModel.getUid();
    return uid;
  }

  public UserModel getUserByName(String name)
  {
    return findFirst("SELECT * FROM user WHERE name=? LIMIT 1", name);
  }

  public UserModel getUserByEmail(String email)
  {
    return findFirst("SELECT * FROM user WHERE email=? LIMIT 1", email);
  }

  public UserModel getUserByNameAndEmail(String name, String email)
  {
    return findFirst("SELECT * FROM user WHERE name=? AND email=? LIMIT 1", name, email);
  }

  public UserModel getUserInfoByName(String name)
  {
    return findFirst("SELECT uid,name,nick,avatar,school,blog,online,level,credit,share,gender,submit,accept,login,ctime FROM user WHERE name=? LIMIT 1", name);
  }

  public UserModel getUserInfoByUid(Integer uid)
  {
    return findFirst("SELECT uid,name,nick,avatar,school,blog,online,level,credit,share,gender,submit,accept,login,ctime FROM user WHERE uid=? LIMIT 1", uid);
  }

  public int getUserRank(Integer uid)
  {
    int userRank = 0;
    Object object = findFirst(
        "SELECT rank FROM (SELECT @rank:=@rank+1 AS rank,uid FROM user,(SELECT @rank:=0)r ORDER BY solved desc,submit)t_rank WHERE uid=? LIMIT 1", uid).get(
        "rank");
    
    if (object instanceof Double)
    {
      double d = (Double) object;
      userRank = (int) d;
    } else if (object instanceof Long)
    {
      long l = (Long) object;
      userRank = (int) l;
    } else
    {
      userRank = (Integer) object;
    }
    return userRank;
  }

  public Page<UserModel> getUserRankList(int pageNumber, int pageSize)
  {
    Page<UserModel> userList = paginate(pageNumber, pageSize, "SELECT @rank:=@rank+1 AS rank,uid,name,nick,realname,solved,submit",
        "FROM user,(SELECT @rank:=?)r WHERE status=1 ORDER BY solved DESC,submit,uid", (pageNumber - 1) * pageSize);
    
    return userList;
  }

  public boolean containsEmailExceptThis(Integer uid, String email)
  {
    return findFirst("SELECT email FROM user WHERE email=? AND uid!=? LIMIT 1", email, uid) != null;
  }

  public boolean containsUsernameExceptThis(Integer uid, String username)
  {
    return findFirst("SELECT name FROM user WHERE name=? AND uid!=? LIMIT 1", username, uid) != null;
  }
}
