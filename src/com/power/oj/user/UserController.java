package com.power.oj.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import jodd.util.HtmlEncoder;
import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.admin.AdminInterceptor;
import com.power.oj.common.OjConstants;
import com.power.oj.common.OjController;

public class UserController extends OjController
{
	public void index()
	{
		render("index.html");
	}

	// @Before(LoginValidator.class)
	@ActionKey("/login")
	public void login()
	{
		String uri = getPara("redirectURI");
		if (StringUtil.isBlank(uri))
			uri = "/";
		if (getRequest().getMethod() == "GET")
			setAttr("redirectURI", uri);

		if (getSessionAttr("user") != null)// user already login
		{
			redirect(uri, "You already login.", "error", "Error!");
			return;
		}

		setAttr("pageTitle", "Login");
		UserModel userModel = null;
		String name = null;
		String password = null;

		if (getRequest().getMethod() != "GET")
		{
			name = getPara("name").trim();
			password = getPara("password");
			userModel = UserModel.dao.getUserByNameAndPassword(name, password);

			if (userModel != null)
			{
				String token = UUID.randomUUID().toString();
				setCookie("name", name, 3600 * 24 * 7);
				if (getParaToBoolean("rememberPassword"))
					setCookie("token", token, 3600 * 24 * 7);

				userModel.updateLogin(token);
				setSessionAttr("user", userModel);

				int uid = userModel.getInt("uid");
				if (userModel.isAdmin(uid))
					setSessionAttr("adminUser", uid);

				redirect(uri);
				return;
			} else
			{
				setAttr("msgType", "error");
				setAttr("msgTitle", "Error!");
				setAttr("msg", "Sorry, you entered an invalid username or password.");
				keepPara("name");
				keepPara("redirectURI");
			}
		}
		boolean ajax = getParaToBoolean("ajax", false);
		if (ajax)
			render("ajax/login.html");
		else
			render("login.html");
	}

	@Before(LoginInterceptor.class)
	@ActionKey("/logout")
	public void logout()
	{
		UserModel user = getSessionAttr("user");
		if (user != null)
		{
			user.set("token", null);
			user.update();
		}
		removeSessionAttr("user");
		removeCookie("name");
		removeCookie("token");
		getSession().invalidate();

		String uri = getPara("redirectURI");
		if (StringUtil.isBlank(uri))
			uri = "/";

		redirect(uri);
	}

	public void profile()
	{
		String name = getPara(0);
		UserModel user = null;
		if (name == null)
		{
			user = getSessionAttr("user");
			if (user == null)
			{
				redirect("/");
				return;
			}
		} else
		{
			user = UserModel.dao.getUserByName(name);
			if (user == null)
			{
				redirect("/");
				return;
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		setAttr("createTime", sdf.format(new Date(user.getInt("ctime") * 1000L)));
		setAttr("loginTime", sdf.format(new Date(user.getInt("login") * 1000L)));
		setAttr("user", user);
		setAttr("userRank", UserModel.dao.getUserRank(user.getInt("uid")));
		setAttr("pageTitle", "User Profile");
		render("profile.html");
	}

	@Before(LoginInterceptor.class)
	public void avatar()
	{
		render("avatar.html");
	}

	@ActionKey("/signup")
	public void signup()
	{
		if (getSessionAttr("user") != null)// user already login
		{
			redirect("/");
			return;
		}

		setAttr("pageTitle", "Signup");
		render("signup.html");
	}

	@Before(SignupValidator.class)
	public void save()
	{
		UserModel userModel = getModel(UserModel.class, "user");
		userModel.saveUser();

		userModel = userModel.findById(userModel.getInt("uid"));
		setSessionAttr("user", userModel);

		redirect("/user/edit", "Congratulations!You have a new account now.<br>Please update your information.");
	}

	@Before(LoginInterceptor.class)
	public void edit()
	{
		setAttr("pageTitle", "Account");
		UserModel user = UserModel.dao.findById(getAttr("userID"));
		setAttr("user", user);
		setAttr("program_languages", OjConstants.program_languages);

		render("edit.html");
	}

	@Before( { LoginInterceptor.class, UpdateUserValidator.class })
	public void update()
	{
		UserModel userModel = getModel(UserModel.class, "user");// we must
		// specify the
		// right name in
		// edit page!!!
		userModel.updateUser();

		redirect(new StringBand(2).append("/user/profile/").append(getAttr("userName")).toString(),
				"The changes have been saved.");
	}

	public void delete()
	{
		renderText("TODO");
	}

	public void search()
	{
		String word = HtmlEncoder.text(getPara("word").trim());
		String scope = getPara("scope");
		setAttr("userList", UserModel.dao.searchUser(scope, word));
		setAttr("word", word);
		setAttr("scope", scope != null ? scope : "all");
		setAttr("pageTitle", new StringBand(2).append("Search user: ").append(word).toString());

		render("search.html");
	}

	/*
	 * public void searchUser() { String word =
	 * HtmlEncoder.text(getPara("word").trim()); String scope =
	 * getPara("scope"); int pageNumber = getParaToInt("p", 1); Page<UserModel>
	 * userList = null;
	 * 
	 * if(StringUtil.isNotBlank(word)) { StringBand sb = new
	 * StringBand("FROM user WHERE "); if(StringUtil.isNotBlank(scope))
	 * sb.append(scope).append(" LIKE '%").append(word).append("%' "); else
	 * sb.append
	 * ("name LIKE '%").append(word).append("%' OR nick LIKE '%").append
	 * (word).append("%' OR email LIKE '%").append(word).append("%'");
	 * sb.append(" AND status=1 ORDER BY solved desc,submit,uid");
	 * 
	 * userList = UserModel.dao.paginate(pageNumber, 50,
	 * "SELECT uid,name,nick,school,solved,submit", sb.toString()); }
	 * 
	 * renderJson(userList); }
	 */

	@Before(AdminInterceptor.class)
	public void online()
	{
		setAttr("pageTitle", "Online Users");
		setAttr("loginUserNum", Db.findFirst(
				"SELECT COUNT(uid) AS count FROM session WHERE session_expires > UNIX_TIMESTAMP() AND uid>0").getLong(
				"count"));
		setAttr("userList", UserModel.dao.onlineUser());

		render("online.html");
	}

	@ActionKey("/rank")
	public void rank()
	{
		int pageNumber = getParaToInt("p", 1);
		int pageSize = getParaToInt("s", 20);
		Page<UserModel> userList = UserModel.dao.paginate(pageNumber, pageSize,
				"SELECT @rank:=@rank+1 AS rank,uid,name,nick,realname,solved,submit",
				"FROM user,(SELECT @rank:=?)r WHERE status=1 ORDER BY solved DESC,submit,uid", (pageNumber - 1)
						* pageSize);

		setAttr("pageTitle", "Ranklist");
		setAttr("userList", userList);

		render("rank.html");
	}

	@Before(AdminInterceptor.class)
	public void build()
	{
		int uid = getParaToInt(0);
		UserModel user = UserModel.dao.findById(uid);

		if (user != null)
			user.build();

		redirect(new StringBand(2).append("/user/profile/").append(user.getStr("name")).toString(),
				"The user statistics have been saved.");
	}
}
