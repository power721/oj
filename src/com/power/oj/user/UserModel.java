package com.power.oj.user;

import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.core.OjConfig;

public class UserModel extends Model<UserModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 7553341600472286034L;
  
  public static final UserModel dao = new UserModel();

  public int getUid()
  {
    return getInt("uid");
  }

  public int getUidByName(String name)
  {
    int uid = 0;
    UserModel userModel = findFirst("SELECT uid FROM user WHERE name=? LIMIT 1", name);
    if (userModel != null)
      uid = userModel.getUid();
    return uid;
  }

  public UserModel getUserByName(String name)
  {
    return findFirst("SELECT * FROM user WHERE name=? LIMIT 1", name);
  }

  public UserModel getUserByNameAndPassword(String name, String password)
  {
    UserModel userModel = getUserByName(name);
    if (userModel != null)
    {
      String stored_hash = userModel.getStr("pass");
      if (BCrypt.checkpw(password, stored_hash))
        return userModel;
    }

    return null;
  }

  public UserModel getUserByNameAndEmail(String name, String email)
  {
    return findFirst("SELECT * FROM user WHERE name=? AND email=? LIMIT 1", name, email);
  }

  public UserModel getUserInfoByName(String name)
  {
    return findFirst("SELECT uid,name,nick,avatar,school,blog,online,level,credit,share,gender,submit,accept,login,ctime FROM user WHERE name=? LIMIT 1", name);
  }

  public UserModel getUserInfoByUid(int uid)
  {
    return findFirst("SELECT uid,name,nick,avatar,school,blog,online,level,credit,share,gender,submit,accept,login,ctime FROM user WHERE uid=? LIMIT 1", uid);
  }

  public int getUserRank(int uid)
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

  public boolean saveUser()
  {
    String password = getStr("pass");
    password = BCrypt.hashpw(password, BCrypt.gensalt());
    set("pass", password);
    set("name", HtmlEncoder.text(getStr("name")));

    long ctime = OjConfig.timeStamp;
    set("atime", ctime).set("ctime", ctime).set("mtime", ctime).set("login", ctime);

    return save();
  }

  public boolean updateUser()
  {
    String password = getStr("pass");
    if (StringUtil.isNotBlank(password))
    {
      password = BCrypt.hashpw(password, BCrypt.gensalt());
      set("pass", password);
    } else
    {
      remove("pass");
    }
    
    set("nick", HtmlEncoder.text(getStr("nick")));
    set("school", HtmlEncoder.text(getStr("school")));
    set("realname", HtmlEncoder.text(getStr("realname")));
    set("blog", HtmlEncoder.text(getStr("blog")));
    set("email", HtmlEncoder.text(getStr("email")));
    set("phone", HtmlEncoder.text(getStr("phone")));
    set("mtime", OjConfig.timeStamp);
    
    return update();
  }

  public boolean updateLogin()
  {
    set("token", null);
    set("login", OjConfig.timeStamp);
    
    return update();
  }

  public boolean checkPassword(int uid, String password)
  {
    String stored_hash = findById(uid, "pass").getStr("pass");
    return BCrypt.checkpw(password, stored_hash);
  }

  public boolean containEmail(String email)
  {
    return findFirst("SELECT email FROM user WHERE email=? LIMIT 1", email) != null;
  }

  public boolean containUsername(String username)
  {
    return findFirst("SELECT name FROM user WHERE name=? LIMIT 1", username) != null;
  }

  public boolean containEmailExceptThis(int userID, String email)
  {
    return findFirst("SELECT email FROM user WHERE email=? AND uid!=? LIMIT 1", email, userID) != null;
  }

  public boolean containUsernameExceptThis(int userID, String username)
  {
    return findFirst("SELECT name FROM user WHERE name=? AND uid!=? LIMIT 1", username, userID) != null;
  }
}
