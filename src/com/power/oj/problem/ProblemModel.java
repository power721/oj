package com.power.oj.problem;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

@SuppressWarnings("serial")
public class ProblemModel extends Model<ProblemModel>
{
	public static final ProblemModel dao = new ProblemModel();

	public int getNextPid(int pid, boolean isAdmin)
	{
		int nextPid = 0;
		StringBand sb = new StringBand("SELECT pid FROM problem WHERE pid>?");
		if (!isAdmin)
			sb.append(" AND status=1");
		sb.append(" ORDER BY pid LIMIT 1");

		try
		{
			nextPid = dao.findFirst(sb.toString(), pid).getInt("pid");
		} catch (Exception e)
		{
			nextPid = pid;
		}
		return nextPid;
	}

	public int getPrevPid(int pid, boolean isAdmin)
	{
		int prevPid = 0;
		StringBand sb = new StringBand("SELECT pid FROM problem WHERE pid<?");
		if (!isAdmin)
			sb.append(" AND status=1");
		sb.append(" ORDER BY pid DESC LIMIT 1");

		try
		{
			prevPid = dao.findFirst(sb.toString(), pid).getInt("pid");
		} catch (Exception e)
		{
			prevPid = pid;
		}
		return prevPid;
	}

	public int getRandomProblem()
	{
		int pid = 0;
		pid = dao
				.findFirst(
						"SELECT t1.pid FROM `problem` AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(pid) FROM `problem`)-(SELECT MIN(pid) FROM `problem`))+(SELECT MIN(pid) FROM `problem`)) AS pid) AS t2 WHERE t1.pid >= t2.pid AND status=1 ORDER BY t1.pid LIMIT 1")
				.getInt("pid");
		return pid;
	}

	public int getPageNumber(int pid, int pageSize)
	{
		long pageNumber = 0;
		pageNumber = findFirst("SELECT COUNT(*) AS idx FROM problem WHERE pid<? AND status=1 ORDER BY pid LIMIT 1", pid).getLong("idx");
		pageNumber = (pageNumber + pageSize) / pageSize;
		return (int) pageNumber;
	}

	public String getProblemTitle(int pid)
	{
		ProblemModel problemModel = findFirst("SELECT title FROM problem WHERE pid=? AND status=1 LIMIT 1", pid);
		String title = null;
		if (problemModel != null)
			title = problemModel.getStr("title");

		return title;
	}

	public List<Record> getUserInfo(int pid, int uid)
	{
		List<Record> userInfo = Db.find("SELECT uid,sid,pid,cid,result,ctime,num,time,memory,code_len,language FROM solution WHERE uid=? AND pid=?", uid, pid);
		return userInfo;
	}

	public Record getUserResult(int pid, int uid)
	{
		Record record = Db.findFirst("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? LIMIT 1", uid, pid);
		return record;
	}

	public List<Record> getTags(int pid)
	{
		List<Record> tagList = Db.find("SELECT tag.tag,user.name FROM tag LEFT JOIN user on user.uid=tag.uid WHERE tag.pid=? AND tag.status=1", pid);
		if (tagList.isEmpty())
			return null;
		return tagList;
	}

	public ProblemModel findByPid(int pid, boolean isAdmin)
	{
		ProblemModel problemModel = null;
		if (isAdmin)
			problemModel = dao.findById(pid);
		else
			problemModel = dao.findFirst("SELECT * FROM problem WHERE status=1 AND pid=? LIMIT 1", pid);

		return problemModel;
	}

	public List<ProblemModel> searchProblem(String scope, String word)
	{
		List<ProblemModel> problemList = null;
		List<Object> paras = new ArrayList<Object>();

		if (StringUtil.isNotBlank(word))
		{
			word = new StringBand(3).append("%").append(word).append("%").toString();
			StringBand sb = new StringBand("SELECT pid,title,accept,submit,source,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime FROM problem WHERE (");
			if (StringUtil.isNotBlank(scope))
			{
				String scopes[] =
				{ "title", "source", "content", "tag" };
				if (StringUtil.equalsOneIgnoreCase(scope, scopes) == -1)
					return null;
				if ("tag".equalsIgnoreCase(scope))
				{
					sb.append("pid IN (SELECT pid FROM tag WHERE tag LIKE ? AND status=1)");
				} else if ("content".equalsIgnoreCase(scope))
				{
					sb.append("description LIKE ? ");
				} else
				{
					sb.append(scope).append(" LIKE ? ");
				}
				paras.add(word);
			} else
			{
				sb.append("title LIKE ? OR source LIKE ? OR description LIKE ?");
				paras.add(word);
				paras.add(word);
				paras.add(word);
			}
			sb.append(" ) AND status=1 ORDER BY accept desc,submit desc,pid");
			problemList = find(sb.toString(), paras.toArray());
		}
		return problemList;
	}

	public boolean updateProblem()
	{
		long mtime = System.currentTimeMillis() / 1000;
		this.set("mtime", mtime);
		if (this.get("status") == null)
			this.set("status", false);

		return this.update();
	}

	public boolean saveProblem()
	{
		long ctime = System.currentTimeMillis() / 1000;
		this.set("ctime", ctime).set("mtime", ctime);
		if (this.get("status") == null)
			this.set("status", false);

		return this.save();
	}

	public boolean incView()
	{
		this.set("view", this.getInt("view") + 1);
		return this.update();
	}

	public boolean addTag(int pid, int uid, String tag)
	{
		Record Tag = new Record().set("pid", pid).set("uid", uid).set("tag", tag).set("ctime", System.currentTimeMillis() / 1000);
		return Db.save("tag", Tag);
	}

	public boolean build()
	{
		int pid = this.getInt("pid");
		long accept = 0;
		long submit = 0;
		long ratio = 0;
		long submit_user = 0;
		long solved = 0;
		long difficulty = 0;

		Record record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE pid=? LIMIT 1", pid);

		if (record != null)
		{
			submit = record.getLong("count");
			this.set("submit", submit);
		}

		record = Db.findFirst("SELECT COUNT(*) AS count FROM solution WHERE pid=? AND result=0 LIMIT 1", pid);
		if (record != null)
		{
			accept = record.getLong("count");
			this.set("accept", accept);
		}

		if (submit > 0)
			ratio = accept / submit;
		this.set("ratio", ratio);

		record = Db.findFirst("SELECT COUNT(uid) AS count FROM solution WHERE pid=? LIMIT 1", pid);
		if (record != null)
		{
			submit_user = record.getLong("count");
			this.set("submit_user", submit_user);
		}

		record = Db.findFirst("SELECT COUNT(uid) AS count FROM solution WHERE pid=? AND result=0 LIMIT 1", pid);
		if (record != null)
		{
			solved = record.getLong("count");
			this.set("solved", solved);
		}

		if (submit_user > 0)
			difficulty = solved / submit_user;
		this.set("difficulty", difficulty);

		return this.update();
	}
}
