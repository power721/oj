package com.power.oj.contest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.HtmlDecoder;
import jodd.util.StringUtil;

import org.apache.http.conn.HttpHostConnectException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.power.oj.contest.model.BoardModel;
import com.power.oj.contest.model.ContestClarifyModel;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestProblemModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.judge.JudgeService;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import com.power.oj.user.UserService;
import com.power.oj.util.HttpUtil;

public class ContestService {
	private static final Logger log = Logger.getLogger(ContestService.class);
	private static final ContestModel dao = ContestModel.dao;
	private static final ContestService me = new ContestService();

	private static final JudgeService judgeService = JudgeService.me();
	private static final UserService userService = UserService.me();
	private static final ProblemService problemService = ProblemService.me();

	private ContestService() {
	}

	public static ContestService me() {
		return me;
	}

	public ContestModel getContest(Integer cid) {
		ContestModel contestModel = null;
		if (OjConfig.isDevMode()) {
			contestModel = dao.findFirst("SELECT *,FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS startDateTime,"
					+ "FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS endDateTime FROM contest WHERE cid=?", cid);
		} else {
			contestModel = dao.findFirstByCache("contest", cid,
					"SELECT *,FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS startDateTime,"
							+ "FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS endDateTime FROM contest WHERE cid=?", cid);
		}
		return contestModel;
	}

	public String getContestTitle(Integer cid) {
		ContestModel contestModel = getContest(cid);

		if (contestModel == null) {
			return null;
		}
		return contestModel.getTitle();
	}

	public List<Record> getContestProblems(Integer cid, Integer uid) {
		String sql = "SELECT * FROM contest_problem WHERE cid=? ORDER BY num";
		List<Record> contestProblems;
		if (uid != null && uid > 0) {
			sql = "SELECT cp.pid,num,accepted,submission,title,status FROM contest_problem cp LEFT OUTER JOIN"
					+ " (SELECT pid,MIN(result) AS status FROM contest_solution WHERE uid=? "
					+ "AND cid=? AND status=1 GROUP BY pid)AS temp ON cp.pid=temp.pid WHERE cp.cid=? ORDER BY num";
			contestProblems = Db.find(sql, uid, cid, cid);
		} else {
			contestProblems = Db.find(sql, cid);
		}

		for (Record problem : contestProblems) {
			problem.set("id", (char) (problem.getInt("num") + 'A'));
		}

		return contestProblems;
	}

	public List<Record> getContestProblems(Integer cid) {
		List<Record> contestProblems = Db
				.find("SELECT p.*,cp.title,cp.num FROM contest_problem cp LEFT JOIN problem p ON p.pid=cp.pid WHERE cid=? ORDER BY num",
						cid);
		for (Record problem : contestProblems) {
			problem.set("id", (char) (problem.getInt("num") + 'A'));
		}

		return contestProblems;
	}

	public ProblemModel getProblem(Integer cid, Integer num) {
		Record record = Db.findFirst("SELECT pid,title,accepted,submission FROM contest_problem WHERE cid=? AND num=? LIMIT 1",
				cid, num);
		if (record == null)
			return null;

		Integer pid = record.getInt("pid");
		ProblemModel problem = problemService.findProblemForContest(pid);
		if (problem == null)
			return null;

		String title = record.getStr("title");
		if (StringUtil.isNotBlank(title))
			problem.setTitle(title);

		long submitUser = Db.queryLong("SELECT COUNT(uid) FROM contest_solution WHERE cid=? AND num=? AND status=1", cid, num);
		long solved = Db.queryLong("SELECT COUNT(uid) FROM contest_solution WHERE cid=? AND num=? AND result=? AND status=1",
				cid, num, ResultType.AC);
		problem.setAccepted(record.getInt("accepted"));
		problem.setSubmission(record.getInt("submission"));
		problem.setSubmitUser((int) submitUser);
		problem.setSolved((int) solved);
		problem.put("id", (char) (num + 'A'));
		problem.put("num", num);

		return problem;
	}

	public boolean updateProblem(ProblemModel problemModel, int cid, int num, String title) {
		Record record = Db.findFirst("SELECT * FROM contest_problem WHERE cid=? AND num=? LIMIT 1", cid, num);
		record.set("title", title);
		Db.update("contest_problem", record);

		return problemService.updateProblem(problemModel);
	}

	public Integer getPid(Integer cid, Integer num) {
		return Db.queryInt("SELECT pid FROM contest_problem WHERE cid=? AND num=? LIMIT 1", cid, num);
	}

	public String getProblemTitle(int cid, int num) {
		return Db.queryStr("SELECT title FROM contest_problem WHERE cid=? AND num=? LIMIT 1", cid, num);
	}

	public List<Record> getContestUsers(Integer cid) {
		return Db.find("SELECT c.*,u.name,u.realName,u.nick FROM contest_user c LEFT JOIN user u ON u.uid=c.uid WHERE cid=?",
				cid);
	}

	public boolean isUserInContest(Integer uid, Integer cid) {
		return Db.queryInt("SELECT uid FROM contest_user WHERE uid=? AND cid=? LIMIT 1", uid, cid) != null;
	}

	public Integer getUserResult(Integer cid, Integer num) {
		Integer uid = userService.getCurrentUid();
		if (uid == null)
			return null;

		return Db.queryInt(
				"SELECT MIN(result) AS result FROM contest_solution WHERE cid=? AND uid=? AND num=? AND status=1 LIMIT 1", cid,
				uid, num);
	}

	public Page<ContestModel> getContestList(int pageNumber, int pageSize, Integer type, Integer status) {
		List<Object> paras = new ArrayList<Object>();
		String sql = "SELECT *,FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS startDateTime,FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS endDateTime";
		StringBuilder sb = new StringBuilder("FROM contest WHERE 1=1");
		if (type > -1) {
			sb.append(" AND type=?");
			paras.add(type);
		}
		// TODO only admin and attendee can see test contest

		if (status == ContestModel.PENDING) {
			sb.append(" AND startTime>UNIX_TIMESTAMP()");
		} else if (status == ContestModel.RUNNING) {
			sb.append(" AND startTime<UNIX_TIMESTAMP() AND endTime>UNIX_TIMESTAMP()");
		} else if (status == ContestModel.FINISHED) {
			sb.append(" AND endTime<UNIX_TIMESTAMP()");
		}

		sb.append(" ORDER BY cid DESC");

		Page<ContestModel> ContestList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

		for (ContestModel contest : ContestList.getList()) {
			int ctime = OjConfig.timeStamp;
			int startTime = contest.getStartTime();
			int endTime = contest.getEndTime();
			String cstatus = "Running";

			if (startTime > ctime) {
				cstatus = "Pending";
			} else if (endTime < ctime) {
				cstatus = "Finished";
			}

			contest.put("cstatus", cstatus);

			String ctype = "Public";
			if (contest.hasPassword()) {
				ctype = "Password";
			} else if (contest.isPrivate()) {
				ctype = "Private";
			} else if (contest.isStrictPrivate()) {
				ctype = "Strict Private";
			} else if (contest.isTest()) {
				ctype = "Test";
			}
			contest.put("ctype", ctype);
		}

		return ContestList;
	}

	public Page<ContestModel> getContestListDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir,
			String sSearch) {
		List<Object> paras = new ArrayList<Object>();
		String sql = "SELECT *";
		StringBuilder sb = new StringBuilder("FROM contest WHERE 1=1");

		if ("running".equals(sSearch.toLowerCase())) {
			sb.append(" AND startTime>UNIX_TIMESTAMP()");
		} else if ("pending".equals(sSearch.toLowerCase())) {
			sb.append(" AND startTime<UNIX_TIMESTAMP() AND endTime>UNIX_TIMESTAMP()");
		} else if ("finished".equals(sSearch.toLowerCase())) {
			sb.append(" AND endTime<UNIX_TIMESTAMP()");
		} else {
			sb.append(" AND title LIKE ?");
			paras.add(new StringBuilder(3).append("%").append(sSearch).append("%").toString());
		}

		sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", cid DESC");

		Page<ContestModel> ContestList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

		for (ContestModel contest : ContestList.getList()) {
			int ctime = OjConfig.timeStamp;
			int startTime = contest.getInt("startTime");
			int endTime = contest.getInt("endTime");
			String cstatus = "Running";

			if (startTime > ctime) {
				cstatus = "Pending";
			} else if (endTime < ctime) {
				cstatus = "Finished";
			}

			contest.put("cstatus", cstatus);

			String ctype = "Public";
			if (contest.hasPassword()) {
				ctype = "Password";
			} else if (contest.isPrivate()) {
				ctype = "Private";
			} else if (contest.isStrictPrivate()) {
				ctype = "Strict Private";
			} else if (contest.isTest()) {
				ctype = "Test";
			}
			contest.put("ctype", ctype);
		}

		return ContestList;
	}

	public Page<Record> getContestRank(int pageNumber, int pageSize, Integer cid) {
		String sql = "FROM board b LEFT JOIN user u ON u.uid=b.uid WHERE b.cid=? ORDER BY solved DESC,penalty";
		Page<Record> userRank = Db.paginate(pageNumber, pageSize, "SELECT b.*,u.name,u.nick,u.realName", sql, cid);

		return userRank;
	}

	public List<Record> getClarifyList(Integer cid, Integer num) {
		if (num != null && num > -1) {
			return Db.find("SELECT c.*,u.name,p.title FROM contest_clarify c LEFT JOIN user u ON u.uid=c.uid "
					+ "LEFT JOIN contest_problem p ON p.num=c.num AND p.cid=c.cid WHERE c.cid=? AND c.num=? ORDER BY c.id DESC",
					cid, num);
		} else {
			return Db.find("SELECT c.*,u.name,p.title FROM contest_clarify c LEFT JOIN user u ON u.uid=c.uid "
					+ "LEFT JOIN contest_problem p ON p.num=c.num AND p.cid=c.cid WHERE c.cid=? ORDER BY c.id DESC", cid);
		}
	}

	public List<Record> getPrivateClarifyList(Integer cid, Integer num, Integer uid) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c.*,u.name,p.title FROM contest_clarify c LEFT JOIN user u ON u.uid=c.uid ");
		sb.append("LEFT JOIN contest_problem p ON p.num=c.num AND p.cid=c.cid WHERE c.cid=? AND c.uid=? AND c.public=0 ");
		if (num != null && num > -1) {
			sb.append(" AND c.num=? ");
		}
		sb.append("ORDER BY c.id DESC");
		if (num != null && num > -1) {
			return Db.find(sb.toString(), cid, uid, num);
		}
		return Db.find(sb.toString(), cid, uid);
	}

	public List<Record> getPublicClarifyList(Integer cid, Integer num) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c.*,u.name,p.title FROM contest_clarify c LEFT JOIN user u ON u.uid=c.uid ");
		sb.append("LEFT JOIN contest_problem p ON p.num=c.num AND p.cid=c.cid WHERE c.cid=? AND c.public=1 ");
		if (num != null && num > -1) {
			sb.append(" AND c.num=? ");
		}
		sb.append("ORDER BY c.id DESC");
		if (num != null && num > -1) {
			return Db.find(sb.toString(), cid, num);
		}
		return Db.find(sb.toString(), cid);
	}

	public boolean addClarify(Integer cid, Integer num, String question) {
		ContestClarifyModel clarify = new ContestClarifyModel();

		clarify.setCid(cid);
		clarify.setNum(num);
		clarify.setUid(userService.getCurrentUid());
		clarify.setQuestion(question);
		clarify.setCtime(OjConfig.timeStamp);
		if (userService.isAdmin()) {
			clarify.setPublic(true);
		}

		return clarify.save();
	}

	public boolean updateClarify(Integer id, String reply, boolean isPublic) {
		Record clarify = Db.findById("contest_clarify", id);

		if (StringUtil.isNotBlank(clarify.getStr("reply"))) {
			clarify.set("mtime", OjConfig.timeStamp);
		} else {
			clarify.set("admin", userService.getCurrentUid());
		}
		clarify.set("reply", reply);
		clarify.set("public", isPublic);
		clarify.set("atime", OjConfig.timeStamp);

		return Db.update("contest_clarify", clarify);
	}

	public String getRecentContest() {
		String json = null;

		if (!OjConfig.isDevMode()) {
			json = CacheKit.get("contest", "recent");
		}
		if (json == null) {
			List<ContestkendoSchedulerTask> contests = new ArrayList<ContestkendoSchedulerTask>();
			String html = null;
			try {
				html = HttpUtil.doGet("http://contests.acmicpc.info/contests.json");
			} catch (HttpHostConnectException e) {
				if (OjConfig.isDevMode()) {
					log.warn("get recent contest failed!", e);
				} else {
					log.info(e.getLocalizedMessage());
				}
			}

			if (html == null) {
				try {
					html = HttpUtil.doGet("http://acm.nankai.edu.cn/contests.json");
				} catch (HttpHostConnectException e) {
					if (OjConfig.isDevMode()) {
						log.warn("get recent contest failed!", e);
					} else {
						log.info(e.getLocalizedMessage());
					}
				}
			}
			if (html == null) {
				return null;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long timeStamp = 0;

			JSONArray jsonArray;
			try {
				jsonArray = JSON.parseArray(html);
			} catch (JSONException e) {
				try {
					html = HttpUtil.doGet("http://contests.acmicpc.info/contests.json");
				} catch (HttpHostConnectException e1) {
					if (OjConfig.isDevMode())
						e1.printStackTrace();
					log.info(e1.getLocalizedMessage());
				}
				jsonArray = JSON.parseArray(html);
			}
			log.info(html);

			for (int i = 0; i < jsonArray.size(); ++i) {
				JSONObject data = jsonArray.getJSONObject(i);
				ContestkendoSchedulerTask contest = new ContestkendoSchedulerTask();
				try {
					timeStamp = sdf.parse(data.getString("start_time")).getTime();
				} catch (ParseException e) {
					timeStamp = 0;
					log.warn(e.getLocalizedMessage());
				}
				String start = "/Date(" + timeStamp + ")/";
				String end = "/Date(" + (timeStamp + 18000000) + ")/";
				String link = HtmlDecoder.decode(data.getString("link"));
				String title = data.getString("oj") + " -- " + data.getString("name");

				contest.setTaskId(data.getString("id"));
				contest.setOj(data.getString("oj"));
				contest.setTitle(title);
				contest.setUrl(link);
				contest.setDescription(link);
				contest.setStart(start);
				contest.setEnd(end);

				contests.add(contest);
			}
			json = JsonKit.toJson(contests, 2);
			CacheKit.put("contest", "recent", json);
		}

		return json;
	}

	public void evictRecentContest() {
		CacheKit.remove("contest", "recent");
		getRecentContest();
	}

	public int getContestStatus(Integer cid) {
		ContestModel contestModel = getContest(cid);
		if (contestModel == null) {
			return -1;
		}

		int ctime = OjConfig.timeStamp;
		int startTime = contestModel.getInt("startTime");
		int endTime = contestModel.getInt("endTime");

		if (startTime > ctime) {
			return ContestModel.PENDING;
		} else if (endTime < ctime) {
			return ContestModel.FINISHED;
		}

		return ContestModel.RUNNING;
	}

	public List<Record> getContestStatistics(Integer cid) {
		StringBuilder sb = new StringBuilder("SELECT ");
		for (ProgramLanguageModel language : OjConfig.programLanguages) {
			sb.append("COUNT(IF(language=").append(language.getId()).append(",1,NULL)) AS ").append(language.getExt())
					.append(",");
		}
		for (ResultType resultType : OjConfig.judgeResult) {
			if (resultType.getId() > ResultType.RF)
				break;
			sb.append("COUNT(IF(result=").append(resultType.getId()).append(",1,NULL)) AS ").append(resultType.getName())
					.append(",");
		}
		sb.append("pid,num,COUNT(IF(result>?,1,NULL)) AS Others,COUNT(*) AS total FROM contest_solution WHERE cid=? AND status=1 GROUP BY num ORDER BY num");
		List<Record> statistics = Db.find(sb.toString(), ResultType.RF, cid);
		for (Record record : statistics) {
			record.set("id", (char) (record.getInt("num") + 'A'));
		}

		return statistics;
	}

	public ContestSolutionModel getContestSolution(Integer cid, Integer sid) {
		Integer uid = userService.getCurrentUid();
		StringBuilder sb = new StringBuilder(
				"SELECT pid,uid,language,source FROM contest_solution WHERE sid=? AND cid=? AND status=1");

		if (!userService.isAdmin())
			sb.append(" AND uid=").append(uid);
		sb.append(" LIMIT 1");

		return ContestSolutionModel.dao.findFirst(sb.toString(), sid, cid);
	}

	public int submitSolution(ContestSolutionModel contestSolution) {
		Integer cid = contestSolution.getCid();
		Integer uid = userService.getCurrentUid();
		Integer pid = getPid(contestSolution.getCid(), contestSolution.getNum());
		ProblemModel problemModel = problemService.findProblemForContest(pid);

		if (problemModel == null) {
			return -1;
		}

		contestSolution.setUid(uid);
		contestSolution.setPid(pid);

		if (contestSolution.addSolution()) {
			Db.update("UPDATE contest_problem SET submission=submission+1 WHERE cid=? AND pid=?", cid, pid);

			contestSolution = ContestSolutionModel.dao.findById(contestSolution.getSid());
			judgeService.judge(contestSolution);
		} else {
			return -2;
		}

		return 0;
	}

	public boolean addContest(ContestModel contestModel, String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		contestModel.setUid(userService.getCurrentUid());
		contestModel.setCtime(OjConfig.timeStamp);
		try {
			contestModel.setStartTime((int) (sdf.parse(startTime).getTime() / 1000));
			contestModel.setEndTime((int) (sdf.parse(endTime).getTime() / 1000));
			log.info(contestModel.getEndTime().toString());
		} catch (ParseException e) {
			if (OjConfig.isDevMode()) {
				log.error("add contest failed!", e);
			} else {
				log.error(e.getLocalizedMessage());
			}
		}

		return contestModel.save();
	}

	public boolean updateContest(ContestModel contestModel, String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ContestModel newContest = getContest(contestModel.getCid());

		try {
			contestModel.setStartTime((int) (sdf.parse(startTime).getTime() / 1000));
			contestModel.setEndTime((int) (sdf.parse(endTime).getTime() / 1000));
		} catch (ParseException e) {
			if (OjConfig.isDevMode()) {
				log.error("update contest time failed!", e);
			}
		}

		if (contestModel.hasPassword()) {
			if (StringUtil.isNotBlank(contestModel.getPassword())) {
				newContest.setPassword(contestModel.getPassword());
			} else if (newContest.getType() != ContestModel.TYPE_PASSWORD) {
				return false;
			}
		}
		newContest.setTitle(contestModel.getTitle());
		newContest.setDescription(contestModel.getDescription());
		newContest.setReport(contestModel.getReport());
		newContest.setStartTime(contestModel.getStartTime());
		newContest.setEndTime(contestModel.getEndTime());
		newContest.setType(contestModel.getType());
		newContest.setFreeze(contestModel.getFreeze());
		newContest.setMtime(OjConfig.timeStamp);
		updateCache(contestModel);

		return contestModel.update();
	}

	public int addProblem(Integer cid, Integer pid, String title) {
		if (isContestFinished(cid)) {
			return -5;
		}

		if (problemService.findProblem(pid) == null) {
			return -4;
		}

		if (Db.queryInt("SELECT id FROM contest_problem WHERE cid=? AND pid=?", cid, pid) != null) {
			return -3;
		}

		long num = 0;
		try {
			num = Db.queryLong("SELECT MAX(num)+1 FROM contest_problem WHERE cid=?", cid);
		} catch (NullPointerException e) {
			num = 0;
		}
		if (num >= OjConstants.MAX_PROBLEMS_IN_CONTEST) {
			return -2;
		}

		Record record = new Record();
		record.set("cid", cid);
		record.set("pid", pid);
		record.set("title", title);
		record.set("num", num);

		if (Db.save("contest_problem", record)) {
			return (int) num;
		}
		return -1;
	}

	public int removeProblem(Integer cid, Integer pid) {
		if (!isContestPending(cid)) {
			return -1;
		}

		int num = Db.queryInt("SELECT num FROM contest_problem WHERE cid=? AND pid=?", cid, pid);
		int result = Db.update("DELETE FROM contest_problem WHERE cid=? AND pid=?", cid, pid);
		List<Record> problems = Db.find("SELECT * FROM contest_problem WHERE cid=? AND num>? ORDER BY num", cid, num);

		for (Record problem : problems) {
			problem.set("num", num++);
			Db.update("contest_problem", problem);
		}

		return result;
	}

	public int reorderProblem(Integer cid, String str) {
		if (!isContestPending(cid)) {
			return -1;
		}

		int result = 0;
		int i = 0;
		String[] pids = str.split(",");
		for (String sPid : pids) {
			Integer pid = Integer.parseInt(sPid);
			result += Db.update("UPDATE contest_problem SET num=? WHERE cid=? AND pid=?", i++, cid, pid);
		}
		return result;
	}

	public int addUser(Integer cid, Integer uid) {
		if (isContestFinished(cid)) {
			return 4;
		}

		if (Db.queryInt("SELECT uid FROM user WHERE uid=? AND status=1", uid) == null) {
			return 3;
		}

		if (Db.queryInt("SELECT id FROM contest_user WHERE cid=? AND uid=?", cid, uid) != null) {
			return 2;
		}

		Record record = new Record();
		record.set("cid", cid);
		record.set("uid", uid);
		record.set("ctime", OjConfig.timeStamp);

		if (Db.save("contest_user", record)) {
			return 0;
		}
		return 1;
	}

	public int removeUser(Integer cid, Integer uid) {
		return Db.update("DELETE FROM contest_user WHERE cid=? AND uid=?", cid, uid);
	}

	public boolean isContestPending(Integer cid) {
		ContestModel contestModel = getContest(cid);
		if (contestModel != null && contestModel.getStartTime() > OjConfig.timeStamp) {
			return true;
		}
		return false;
	}

	public boolean isContestRunning(Integer cid) {
		ContestModel contestModel = getContest(cid);
		if (contestModel != null && contestModel.getStartTime() <= OjConfig.timeStamp
				&& contestModel.getEndTime() >= OjConfig.timeStamp) {
			return true;
		}
		return false;
	}

	public boolean isContestFinished(Integer cid) {
		ContestModel contestModel = getContest(cid);
		if (contestModel != null && contestModel.getEndTime() < OjConfig.timeStamp) {
			return true;
		}
		return false;
	}

	public boolean isContestHasPassword(Integer cid) {
		ContestModel contestModel = getContest(cid);
		if (contestModel != null && contestModel.getType() == ContestModel.TYPE_PASSWORD) {
			return true;
		}
		return false;
	}

	public boolean checkContestPassword(Integer cid, String password) {
		ContestModel contestModel = getContest(cid);
		if (contestModel != null && contestModel.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	public boolean updateBoard(ContestSolutionModel solutionModel) {
		Integer cid = solutionModel.getCid();
		Integer uid = solutionModel.getUid();
		Integer num = solutionModel.getNum();
		Integer submitTime = solutionModel.getCtime();
		char c = (char) (num + 'A');
		Record board = Db.findFirst("SELECT * FROM board WHERE cid=? AND uid=?", cid, uid);

		if (board == null) {
			board = new Record();
			board.set("cid", cid);
			board.set("uid", uid);
			Db.save("board", board);
			board = Db.findById("board", board.get("id"));
		}
		Integer wrongSubmissions = board.getInt(c + "_WrongNum");
		Integer acTime = board.getInt(c + "_SolvedTime");

		if (solutionModel.getResult() == ResultType.AC) {
			ContestProblemModel contestProblem = ContestProblemModel.dao.findFirst(
					"SELECT * FROM contest_problem WHERE cid=? AND num=?", cid, num);
			ContestModel contestModle = getContest(cid);
			Integer contestStartTime = contestModle.getStartTime();

			if (contestProblem.getFirstBloodUid() == 0) {
				contestProblem.setFirstBloodUid(solutionModel.getUid());
				contestProblem.setFirstBloodTime(submitTime - contestStartTime);
			}
			contestProblem.setAccepted(contestProblem.getAccepted() + 1);
			contestProblem.update();

			if (acTime == null || acTime == 0) {
				acTime = submitTime - contestStartTime;
				board.set(c + "_SolvedTime", acTime);
				board.set("solved", board.getInt("solved") + 1);
				board.set("penalty", board.getInt("penalty") + acTime + wrongSubmissions
						* OjConstants.PENALTY_FOR_WRONG_SUBMISSION);
			}
		} else if ((acTime == null || acTime == 0) && solutionModel.getResult() < ResultType.SE) {
			board.set(c + "_WrongNum", wrongSubmissions + 1);
		}

		return Db.update("board", board);
	}

	public boolean updateBoard4Rejudge(ContestSolutionModel solutionModel) {
		int result = solutionModel.getInt("originalResult");
		boolean ac = true;
		if (result == ResultType.AC) {
			return true; // should not rejudge AC in contest!
		}

		if (solutionModel.getResult() != ResultType.AC) // still not AC
		{
			if (result >= ResultType.SE && solutionModel.getResult() < ResultType.SE) {
				// update wrong submissions
				ac = false;
			} else {
				return true;
			}
		}

		int sid = solutionModel.getSid();
		Integer cid = solutionModel.getCid();
		Integer uid = solutionModel.getUid();
		Integer num = solutionModel.getNum();
		if (Db.queryLong(
				"SELECT 1 FROM contest_solution WHERE cid=? AND num=? AND uid=? AND sid<? AND result=? AND status=1 LIMIT 1",
				cid, num, uid, sid, ResultType.AC) != null) {
			return true; // already AC
		}

		// Not-AC --> AC
		Integer submitTime = solutionModel.getCtime();
		char c = (char) (num + 'A');
		Record board = Db.findFirst("SELECT * FROM board WHERE cid=? AND uid=?", cid, uid);
		ContestProblemModel contestProblem = ContestProblemModel.dao.findFirst(
				"SELECT * FROM contest_problem WHERE cid=? AND num=?", cid, num);
		ContestModel contestModle = getContest(cid);
		Integer contestStartTime = contestModle.getStartTime();
		Integer acTime = submitTime - contestStartTime;
		Long wrongSubmissions = 0L;
		if (ac) {
			wrongSubmissions = Db
					.queryLong(
							"SELECT COUNT(*) FROM contest_solution WHERE cid=? AND num=? AND uid=? AND sid<? AND result!=? AND result<? AND status=1",
							cid, num, uid, sid, ResultType.AC, ResultType.SE);
			board.set(c + "_WrongNum", wrongSubmissions);
			board.set(c + "_SolvedTime", acTime);
		}

		// AC after this submission
		if (Db.queryLong(
				"SELECT 1 FROM contest_solution WHERE cid=? AND num=? AND uid=? AND sid>? AND result=? AND status=1 LIMIT 1",
				cid, num, uid, sid, ResultType.AC) != null) {
			if (ac) {
				if (contestProblem.getFirstBloodTime() > acTime) {
					contestProblem.setFirstBloodUid(uid);
					contestProblem.setFirstBloodTime(acTime);
					contestProblem.update();
				}

				long penalty = board.getInt(c + "_SolvedTime") + board.getLong(c + "_WrongNum")
						* OjConstants.PENALTY_FOR_WRONG_SUBMISSION;
				board.set("penalty", board.getInt("penalty") - penalty + acTime + wrongSubmissions
						* OjConstants.PENALTY_FOR_WRONG_SUBMISSION);
			} else {
				board.set(c + "_WrongNum", board.getInt(c + "_WrongNum") + 1);
				board.set("penalty", board.getInt("penalty") + OjConstants.PENALTY_FOR_WRONG_SUBMISSION);
			}
		} else {
			if (ac) {
				// user's first AC for this contest problem
				if (contestProblem.getFirstBloodUid() == 0 || contestProblem.getFirstBloodTime() > acTime) {
					contestProblem.setFirstBloodUid(uid);
					contestProblem.setFirstBloodTime(acTime);
				}
				contestProblem.setAccepted(contestProblem.getAccepted() + 1);
				contestProblem.update();

				board.set("solved", board.getInt("solved") + 1);
				board.set("penalty", board.getInt("penalty") + acTime + wrongSubmissions
						* OjConstants.PENALTY_FOR_WRONG_SUBMISSION);
			} else {
				board.set(c + "_WrongNum", board.getInt(c + "_WrongNum") + 1);
			}
		}
		return Db.update("board", board);
	}

	public boolean reset(Integer cid) {
		Db.update("DELETE FROM board WHERE cid=?", cid);
		List<ContestProblemModel> contestProblems = ContestProblemModel.dao.find("SELECT * FROM contest_problem WHERE cid=?",
				cid);
		for (ContestProblemModel problem : contestProblems) {
			long submission = Db.queryLong("SELECT COUNT(*) FROM contest_solution WHERE cid=? AND num=?", cid, problem.getNum());
			problem.setAccepted(0);
			problem.setSubmission((int) submission);
			problem.update();
		}
		return true;
	}

	public boolean build(Integer cid) {
		Db.update("DELETE FROM board WHERE cid=?", cid);
		ContestModel contestModel = getContest(cid);
		int contestStartTime = contestModel.getStartTime();
		int problemNum = Db.queryInt("SELECT MAX(num) FROM contest_problem WHERE cid=?", cid) + 1;
		List<ContestSolutionModel> solutions = ContestSolutionModel.dao.find(
				"SELECT * FROM contest_solution WHERE cid=? AND status=1 ORDER BY sid", cid);
		HashMap<Integer, UserInfo> userRank = new HashMap<Integer, UserInfo>();
		UserInfo userInfo = null;
		int uid = 0;
		int num = 0;
		int result = 0;
		int ctime = 0;
		int penalty = 0;
		int firstBooldUid[] = new int[problemNum];
		int firstBooldTime[] = new int[problemNum];
		for (int i = 0; i < problemNum; ++i)
			firstBooldTime[i] = -1;
		int accepted[] = new int[problemNum];
		int submission[] = new int[problemNum];

		for (ContestSolutionModel solution : solutions) {
			uid = solution.getUid();
			num = solution.getNum();
			result = solution.getResult();
			ctime = solution.getCtime();
			++submission[num];
			userInfo = userRank.get(uid);
			int elapseTime = ctime - contestStartTime;
			penalty = elapseTime;
			if (result == ResultType.AC && firstBooldUid[num] == 0) {
				firstBooldUid[num] = uid;
				firstBooldTime[num] = elapseTime;
			}
			if (result == ResultType.AC) {
				++accepted[num];
			}
			if (userInfo == null) {
				userInfo = new UserInfo(uid);
				if (result == ResultType.AC) {
					++userInfo.solved;
					userInfo.acTime[num] = elapseTime;
					userInfo.penalty += penalty;
				} else if (result < ResultType.SE) {
					++userInfo.waNum[num];
				}
				userRank.put(uid, userInfo);
			} else if (userInfo.acTime[num] == 0) {
				if (result == ResultType.AC) {
					++userInfo.solved;
					userInfo.acTime[num] = elapseTime;
					penalty += userInfo.waNum[num] * OjConstants.PENALTY_FOR_WRONG_SUBMISSION;
					userInfo.penalty += penalty;
				} else if (result < ResultType.SE) {
					++userInfo.waNum[num];
				}
			}
		}

		for (Map.Entry<Integer, UserInfo> entry : userRank.entrySet()) {
			userInfo = entry.getValue();
			BoardModel board = new BoardModel();
			board.setCid(cid);
			board.setUid(userInfo.getUid());
			board.setSolved(userInfo.getSolved());
			board.setPenalty(userInfo.getPenalty());

			for (int i = 0; i < problemNum; ++i) {
				if (userInfo.getAcTime(i) > 0) {
					board.setSolvedTime(i, userInfo.getAcTime(i));
				}

				if (userInfo.getWaNum(i) > 0) {
					board.setWrongNum(i, userInfo.getWaNum(i));
				}
			}
			board.save();
		}
		for (int i = 0; i < problemNum; ++i) {
			Db.update(
					"UPDATE contest_problem SET firstBloodUid=?,firstBloodTime=?,accepted=?,submission=? WHERE cid=? AND num=?",
					firstBooldUid[i], firstBooldTime[i], accepted[i], submission[i], cid, i);
		}

		return true;
	}

	class UserInfo {
		private Integer uid;
		private int solved = 0;
		private int penalty = 0;
		private int acTime[] = new int[26];
		private int waNum[] = new int[26];

		UserInfo() {
		}

		UserInfo(Integer uid) {
			this.uid = uid;
		}

		public Integer getUid() {
			return uid;
		}

		public void setUid(Integer uid) {
			this.uid = uid;
		}

		public int getSolved() {
			return solved;
		}

		public void setSolved(int solved) {
			this.solved = solved;
		}

		public int getPenalty() {
			return penalty;
		}

		public void setPenalty(int penalty) {
			this.penalty = penalty;
		}

		public int getAcTime(int num) {
			return acTime[num];
		}

		public void setAcTime(int num, int value) {
			this.acTime[num] = value;
		}

		public int getWaNum(int num) {
			return waNum[num];
		}

		public void setWaNum(int num, int value) {
			this.waNum[num] = value;
		}
	}

	private void updateCache(ContestModel contestModel) {
		CacheKit.put("contest", contestModel.getCid(), contestModel);
	}
}
