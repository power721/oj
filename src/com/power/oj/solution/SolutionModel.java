package com.power.oj.solution;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.contest.ContestModel;
import com.power.oj.core.OjConstants;
import com.power.oj.core.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.user.UserModel;

@SuppressWarnings("serial")
public class SolutionModel extends Model<SolutionModel>
{
	public static final SolutionModel dao = new SolutionModel();

	public Page<SolutionModel> getPage(int pageNumber, int pageSize, int result, int language, int pid, String userName)
	{
		int uid = 0;
		String name = userName;
		String sql = "SELECT sid,uid,pid,cid,num,result,time,memory,language,code_len,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";
		StringBand sb = new StringBand("FROM solution WHERE 1=1");

		List<Object> paras = new ArrayList<Object>();
		if (result > -1)
		{
			sb.append(" AND result=?");
			paras.add(result);
		}
		if (language > -1)
		{
			sb.append(" AND language=?");
			paras.add(language);
		}
		if (pid > 0)
		{
			sb.append(" AND pid=?");
			paras.add(pid);
		}
		if (StringUtil.isNotBlank(userName))
		{
			uid = UserModel.dao.getUidByName(userName);
			sb.append(" AND uid=?");
			paras.add(uid);

			if (uid == 0)
				userName = "";
		}

		sb.append(" ORDER BY sid DESC");
		Page<SolutionModel> solutionList = SolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras
				.toArray());

		for (SolutionModel solution : solutionList.getList())
		{
			if (StringUtil.isBlank(userName))
			{
				uid = solution.getInt("uid");
				name = UserModel.dao.findById(uid, "name").get("name");
			}
			solution.put("name", name);

			solution.set("language", ((LanguageModel) OjConstants.language_type.get(solution.getInt("language")))
					.get("name"));

			ResultType resultType = (ResultType) OjConstants.result_type.get(solution.getInt("result"));
			solution.put("resultName", resultType.getName());
			solution.put("resultLongName", resultType.getLongName());
			
			if(solution.get("num") != null && solution.getInt("num") > -1)
			{
				solution.put("alpha", (char)(solution.getInt("num")+'A'));
			}
		}

		return solutionList;
	}

	public Page<SolutionModel> getPageForContest(int pageNumber, int pageSize, int result, int language, int cid, int num, String userName)
	{
		int uid = 0;
		String name = userName;
		String sql = "SELECT sid,uid,pid,cid,num,result,time,memory,language,code_len,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";
		StringBand sb = new StringBand("FROM solution WHERE cid=?");
		UserModel userModel;

		List<Object> paras = new ArrayList<Object>();
		paras.add(cid);
		if (result > -1)
		{
			sb.append(" AND result=?");
			paras.add(result);
		}
		if (language > -1)
		{
			sb.append(" AND language=?");
			paras.add(language);
		}
		if (num > -1)
		{
			sb.append(" AND num=?");
			paras.add(num);
		}
		if (StringUtil.isNotBlank(userName))
		{
			uid = UserModel.dao.getUidByName(userName);
			sb.append(" AND uid=?");
			paras.add(uid);

			if (uid == 0)
				userName = "";
		}

		sb.append(" ORDER BY sid DESC");
		Page<SolutionModel> solutionList = SolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras
				.toArray());
		
		for (SolutionModel solution : solutionList.getList())
		{
			uid = solution.getInt("uid");
			userModel = UserModel.dao.findById(uid, "name,nick");
			if (StringUtil.isBlank(userName))
			{
				name = userModel.get("name");
			}
			solution.put("name", name);
			solution.put("nick", userModel.get("nick"));

			solution.set("language", ((LanguageModel) OjConstants.language_type.get(solution.getInt("language")))
					.get("name"));

			ResultType resultType = (ResultType) OjConstants.result_type.get(solution.getInt("result"));
			solution.put("resultName", resultType.getName());
			solution.put("resultLongName", resultType.getLongName());
			solution.put("alpha", (char)(solution.getInt("num") + 'A'));
		}

		return solutionList;
	}

	public Page<SolutionModel> getProblemStatusPage(int pageNumber, int pageSize, int language, int pid)
	{
		int uid = 0;
		String name = "";
		String sql = "SELECT sid,uid,pid,result,time,memory,language,code_len,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";
		StringBand sb = new StringBand("FROM solution WHERE result=0");

		List<Object> paras = new ArrayList<Object>();

		if (language > -1)
		{
			sb.append(" AND language=?");
			paras.add(language);
		}
		
		sb.append(" AND pid=?");
		paras.add(pid);

		sb.append(" ORDER BY time,memory,code_len,sid");
		Page<SolutionModel> solutionList = SolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras
				.toArray());

		for (SolutionModel solution : solutionList.getList())
		{

			uid = solution.getInt("uid");
			try {
				name = UserModel.dao.findById(uid, "name").get("name");
			} catch(NullPointerException e)
			{
				name = "";
			}
			solution.put("name", name);

			solution.set("language", ((LanguageModel) OjConstants.language_type.get(solution.getInt("language")))
					.get("name"));
		}

		return solutionList;
	}

	public Page<SolutionModel> getProblemStatusPageForContest(int pageNumber, int pageSize, int language, int cid, int num)
	{
		int uid = 0;
		String name = "";
		String sql = "SELECT sid,uid,pid,result,time,memory,language,code_len,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime";
		StringBand sb = new StringBand("FROM solution WHERE result=0");

		List<Object> paras = new ArrayList<Object>();

		if (language > -1)
		{
			sb.append(" AND language=?");
			paras.add(language);
		}
		
		sb.append(" AND cid=?");
		paras.add(cid);

		sb.append(" AND num=?");
		paras.add(num);

		sb.append(" ORDER BY time,memory,code_len,sid");
		Page<SolutionModel> solutionList = SolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras
				.toArray());

		for (SolutionModel solution : solutionList.getList())
		{

			uid = solution.getInt("uid");
			name = UserModel.dao.findById(uid, "name").get("name");
			solution.put("name", name);

			solution.set("language", ((LanguageModel) OjConstants.language_type.get(solution.getInt("language")))
					.get("name"));
		}

		return solutionList;
	}

	public boolean addSolution()
	{
		long ctime = System.currentTimeMillis() / 1000;
		if(get("cid") != null)
		{
			int pid = ContestModel.dao.getPid(getInt("cid"), getInt("num"));
			set("pid", pid);
		}
		this.set("ctime", ctime).set("result", ResultType.Wait).set("code_len", this.getStr("source").length());
		System.out.println(this.getInt("code_len"));
		if (this.getInt("code_len") < 10 || this.getInt("code_len") > 30000)
			return false;

		return this.save();
	}
}
