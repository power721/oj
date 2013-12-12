package com.power.oj.user;

import java.util.ArrayList;
import java.util.List;

import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;
import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
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
    UserModel userModel = dao.findFirst("SELECT * FROM user WHERE name=? AND email=? LIMIT 1", name, email);
    return userModel;
  }

  public int getUidByName(String name)
  {
    int uid = 0;
    UserModel userModel = findFirst("SELECT uid FROM user WHERE name=? LIMIT 1", name);
    if (userModel != null)
      uid = userModel.getUid();
    return uid;
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

  public UserModel getUserByName(String name)
  {
    UserModel userModel = dao.findFirst("SELECT * FROM user WHERE name=? LIMIT 1", name);
    return userModel;
  }

  public UserModel autoLogin(String name, String token) throws AutoLoginException
  {
    UserModel userModel = dao.findFirst("SELECT * FROM user WHERE name=? AND token=? LIMIT 1", name, token);
    if (userModel == null)
      throw new AutoLoginException("Auto login for user " + name + " failed!");
    
    return userModel;
  }

  public boolean isRoot(int uid)
  {
    return Db.findFirst("SELECT 1 FROM role WHERE uid=? AND role='root' AND status=1 LIMIT 1", uid) != null;
  }

  public boolean isAdmin(int uid)
  {
    return Db.findFirst("SELECT 1 FROM role WHERE uid=? AND (role='root' OR role='administrator') AND status=1 LIMIT 1", uid) != null;
  }

  public boolean isMember(int uid)
  {
    return Db.findFirst("SELECT 1 FROM role WHERE uid=? AND (role='root' OR role='administrator' OR role='member') AND status=1 LIMIT 1", uid) != null;
  }

  public boolean isSourceBrowser(int uid)
  {
    return Db.findFirst(
        "SELECT 1 FROM role WHERE uid=? AND (role='root' OR role='administrator' OR role='member' OR role='source_browser') AND status=1 LIMIT 1", uid) != null;
  }

  public boolean isTitle(int uid)
  {
    return Db
        .findFirst(
            "SELECT 1 FROM role WHERE uid=? AND (role='root' OR role='administrator' OR role='member' OR role='source_browser' OR role='title') AND status=1 LIMIT 1",
            uid) != null;
  }

  public String getRole(int uid)
  {
    Record record = Db.findFirst("SELECT role FROM role WHERE uid=? AND status=1 LIMIT 1", uid);
    if (record != null)
      return record.getStr("role");
    return null;
  }

  public List<UserModel> searchUser(String scope, String word)
  {
    List<UserModel> userList = null;
    List<Object> paras = new ArrayList<Object>();

    if (StringUtil.isNotBlank(word))
    {
      word = new StringBand(3).append("%").append(word).append("%").toString();
      StringBand sb = new StringBand("SELECT uid,name,nick,school,solved,submit FROM user WHERE (");
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
      userList = dao.find(sb.toString(), paras.toArray());
    }
    return userList;
  }

  public List<Record> onlineUser()
  {
    List<Record> userList = Db
        .find("SELECT uid,name,ip_address,user_agent,FROM_UNIXTIME(last_activity, '%Y-%m-%d %H:%i:%s') AS time,uri FROM session WHERE session_expires > UNIX_TIMESTAMP() ORDER BY last_activity DESC");
    return userList;
  }

  public boolean saveUser()
  {
    String password = this.getStr("pass");
    password = BCrypt.hashpw(password, BCrypt.gensalt());
    this.set("pass", password);
    this.set("name", HtmlEncoder.text(this.getStr("name")));

    long ctime = OjConfig.timeStamp;
    this.set("atime", ctime).set("ctime", ctime).set("mtime", ctime).set("login", ctime);

    return this.save();
  }

  public boolean updateUser()
  {
    String password = this.getStr("pass");
    if (StringUtil.isNotBlank(password))
    {
      password = BCrypt.hashpw(password, BCrypt.gensalt());
      this.set("pass", password);
    } else
    {
      this.remove("pass");
    }
    this.set("nick", HtmlEncoder.text(this.getStr("nick")));
    this.set("school", HtmlEncoder.text(this.getStr("school")));
    this.set("realname", HtmlEncoder.text(this.getStr("realname")));
    this.set("blog", HtmlEncoder.text(this.getStr("blog")));
    // this.set("email", HtmlEncoder.text(this.getStr("email")));
    this.set("phone", HtmlEncoder.text(this.getStr("phone")));

    long mtime = OjConfig.timeStamp;
    this.set("mtime", mtime);
    return this.update();
  }

  public boolean updateLogin(String token)
  {
    int login = (int) (OjConfig.timeStamp);
    this.set("login", login);
    if (StringUtil.isNotBlank(token))
      this.set("token", token);
    return this.update();
  }

  public boolean build()
  {
    int uid = this.getUid();
    long accept = 0;
    long submit = 0;
    long solved = 0;

    Record record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE uid=? LIMIT 1", uid);

    if (record != null)
    {
      submit = record.getLong("count");
      this.set("submit", submit);
    }

    record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE uid=? AND result=0 LIMIT 1", uid);
    if (record != null)
    {
      accept = record.getLong("count");
      this.set("accept", accept);
    }

    record = Db.findFirst("SELECT COUNT(pid) AS count FROM solution WHERE uid=? AND result=0 LIMIT 1", uid);
    if (record != null)
    {
      solved = record.getLong("count");
      this.set("solved", solved);
    }

    return this.update();
  }

  public boolean containEmail(String email)
  {
    return dao.findFirst("select email from user where email=? limit 1", email) != null;
  }

  public boolean checkPass(int uid, String password)
  {
    String stored_hash = dao.findById(uid, "pass").getStr("pass");
    return BCrypt.checkpw(password, stored_hash);
  }

  public boolean containUsername(String username)
  {
    return dao.findFirst("select name from user where name=? limit 1", username) != null;
  }

  public boolean containEmailExceptThis(int userID, String email)
  {
    return dao.findFirst("select email from user where email=? and uid!=? limit 1", email, userID) != null;
  }

  public boolean containUsernameExceptThis(int userID, String username)
  {
    return dao.findFirst("select name from user where name=? and uid!=? limit 1", username, userID) != null;
  }
}
