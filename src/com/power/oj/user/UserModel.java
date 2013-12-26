package com.power.oj.user;

import java.util.ArrayList;
import java.util.List;

import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
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
    UserModel userModel = findFirst("SELECT * FROM user WHERE name=? LIMIT 1", name);
    return userModel;
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
    UserModel userModel = findFirst("SELECT * FROM user WHERE name=? AND email=? LIMIT 1", name, email);
    return userModel;
  }

  public UserModel getUserInfoByName(String name)
  {
    UserModel userModel = findFirst("SELECT uid,name,nick,avatar,school,blog,online,level,credit,share,gender,submit,accept,login,ctime FROM user WHERE name=? LIMIT 1", name);
    return userModel;
  }

  public UserModel getUserInfoByUid(int uid)
  {
    UserModel userModel = findFirst("SELECT uid,name,nick,avatar,school,blog,online,level,credit,share,gender,submit,accept,login,ctime FROM user WHERE uid=? LIMIT 1", uid);
    return userModel;
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
    Page<UserModel> userList = UserModel.dao.paginate(pageNumber, pageSize, "SELECT @rank:=@rank+1 AS rank,uid,name,nick,realname,solved,submit",
        "FROM user,(SELECT @rank:=?)r WHERE status=1 ORDER BY solved DESC,submit,uid", (pageNumber - 1) * pageSize);
    
    return userList;
  }

  public List<UserModel> searchUser(String scope, String word)
  {
    List<UserModel> userList = null;
    List<Object> paras = new ArrayList<Object>();

    if (StringUtil.isNotBlank(word))
    {
      word = new StringBuilder(3).append("%").append(word).append("%").toString();
      StringBuilder sb = new StringBuilder("SELECT uid,name,nick,school,solved,submit FROM user WHERE (");
      
      if (StringUtil.isNotBlank(scope))
      {
        String scopes[] =
        { "name", "nick", "school", "email" };
        if (StringUtil.equalsOneIgnoreCase(scope, scopes) == -1)
          return null;
        sb.append(scope).append(" LIKE ? ");
        paras.add(word);
      } else
      {
        sb.append("name LIKE ? OR nick LIKE ? OR school LIKE ? OR email LIKE ?");
        for (int i = 0; i < 4; ++i)
          paras.add(word);
      }
      sb.append(") AND status=1 ORDER BY solved desc,submit,uid");
      
      userList = find(sb.toString(), paras.toArray());
    }
    
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

  public boolean updateLogin(String token)
  {
    set("login", OjConfig.timeStamp);
    if (StringUtil.isNotBlank(token))
      set("token", token);
    return update();
  }

  public boolean updateLogin()
  {
    set("login", OjConfig.timeStamp);
    
    return update();
  }

  public boolean build()
  {
    int uid = getUid();
    long accept = 0;
    long submit = 0;
    long solved = 0;

    Record record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE uid=? LIMIT 1", uid);

    if (record != null)
    {
      submit = record.getLong("count");
      set("submit", submit);
    }

    record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE uid=? AND result=0 LIMIT 1", uid);
    if (record != null)
    {
      accept = record.getLong("count");
      set("accept", accept);
    }

    record = Db.findFirst("SELECT COUNT(pid) AS count FROM solution WHERE uid=? AND result=0 LIMIT 1", uid);
    if (record != null)
    {
      solved = record.getLong("count");
      set("solved", solved);
    }

    return update();
  }

  public boolean checkPass(int uid, String password)
  {
    String stored_hash = findById(uid, "pass").getStr("pass");
    return BCrypt.checkpw(password, stored_hash);
  }

  public boolean containEmail(String email)
  {
    return findFirst("select email from user where email=? limit 1", email) != null;
  }

  public boolean containUsername(String username)
  {
    return findFirst("select name from user where name=? limit 1", username) != null;
  }

  public boolean containEmailExceptThis(int userID, String email)
  {
    return findFirst("select email from user where email=? and uid!=? limit 1", email, userID) != null;
  }

  public boolean containUsernameExceptThis(int userID, String username)
  {
    return findFirst("select name from user where name=? and uid!=? limit 1", username, userID) != null;
  }
}
