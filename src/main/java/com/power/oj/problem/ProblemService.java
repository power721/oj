package com.power.oj.problem;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.judge.JudgeService;
import com.power.oj.judge.RejudgeType;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;
import jodd.io.FileUtil;
import jodd.util.HtmlDecoder;
import jodd.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ProblemService {
    private static final Logger log = Logger.getLogger(ProblemService.class);
    private static final ProblemService me = new ProblemService();
    private static final UserService userService = UserService.me();
    private static final ProblemModel dao = ProblemModel.dao;

    private ProblemService() {
    }

    public static ProblemService me() {
        return me;
    }

    public boolean isRejudging(Integer pid) {
        return JudgeService.me().isRejudging(RejudgeType.PROBLEM.getKey(pid));
    }

    public ProblemModel findProblem(Integer pid) {
        ProblemModel problemModel = null;

        if (OjConfig.isDevMode()) {
            problemModel = dao.findById(pid);
        } else {
            problemModel = dao.findFirstByCache("problem", pid, "SELECT * FROM problem WHERE pid=?", pid);
        }

        if (userService.isAdmin()) {
            return problemModel;
        } else if (problemModel != null && problemModel.getStatus()) {
            return problemModel;
        }

        return null;
    }

    public ProblemModel findProblemForShow(Integer pid) {
        ProblemModel problemModel = findProblem(pid);

        if (problemModel == null) {
            return null;
        }

        int sampleInputRows = 1;
        if (StringUtil.isNotBlank(problemModel.getSampleInput())) {
            sampleInputRows = StringUtil.count(problemModel.getSampleInput(), '\n') + 1;
        }
        problemModel.put("sample_input_rows", sampleInputRows);

        int sampleOutputRows = 1;
        if (StringUtil.isNotBlank(problemModel.getSampleOutput())) {
            sampleOutputRows = StringUtil.count(problemModel.getSampleOutput(), '\n') + 1;
        }
        problemModel.put("sample_output_rows", sampleOutputRows);

        problemModel.setView(problemModel.getView() + 1);
        if (OjConfig.isDevMode()) {
            problemModel.update();
        } else {
            updateCache(problemModel);
        }

        return problemModel;
    }

    public ProblemModel findProblemForContest(Integer pid) {
        ProblemModel problemModel = null;

        if (OjConfig.isDevMode()) {
            problemModel = dao.findById(pid);
        } else {
            problemModel = dao.findFirstByCache("problem", pid, "SELECT * FROM problem WHERE pid=?", pid);
        }

        if (problemModel == null) {
            return null;
        }

        int sampleInputRows = 1;
        if (StringUtil.isNotBlank(problemModel.getSampleInput())) {
            sampleInputRows = StringUtil.count(problemModel.getSampleInput(), '\n') + 1;
        }
        problemModel.put("sample_input_rows", sampleInputRows);

        int sampleOutputRows = 1;
        if (StringUtil.isNotBlank(problemModel.getSampleOutput())) {
            sampleOutputRows = StringUtil.count(problemModel.getSampleOutput(), '\n') + 1;
        }
        problemModel.put("sample_output_rows", sampleOutputRows);

        return problemModel;
    }

    public Long getProblemsNumber() {
        return Db.queryLong("SELECT COUNT(*) FROM problem WHERE status=1");
    }

    public int getNextPid(Integer pid) {
        int nextPid = 0;
        StringBuilder sb = new StringBuilder().append("SELECT pid FROM problem WHERE pid>?");
        if (!userService.isAdmin()) {
            sb.append(" AND status=1");
        }
        sb.append(" ORDER BY pid LIMIT 1");

        try {
            nextPid = dao.findFirst(sb.toString(), pid).getPid();
        } catch (Exception e) {
            nextPid = pid;
        }
        return nextPid;
    }

    public int getPrevPid(Integer pid) {
        int prevPid = 0;
        StringBuilder sb = new StringBuilder().append("SELECT pid FROM problem WHERE pid<?");
        if (!userService.isAdmin()) {
            sb.append(" AND status=1");
        }
        sb.append(" ORDER BY pid DESC LIMIT 1");

        try {
            prevPid = dao.findFirst(sb.toString(), pid).getPid();
        } catch (Exception e) {
            prevPid = pid;
        }
        return prevPid;
    }

    public Integer getRandomPid() {
        return dao.findFirst(
            "SELECT t1.pid FROM `problem` AS t1 JOIN (SELECT ROUND(RAND() * ((SELECT MAX(pid) FROM `problem`)-(SELECT MIN(pid) FROM `problem`))+(SELECT MIN(pid) FROM `problem`)) AS pid) AS t2 WHERE t1.pid >= t2.pid AND status=1 ORDER BY t1.pid LIMIT 1")
            .getPid();
    }

    public List<Record> getTags(Integer pid) {
        List<Record> tagList =
            Db.find("SELECT t.tag,u.name FROM tag t LEFT JOIN user u on u.uid=t.uid WHERE t.pid=? AND t.status=1", pid);

        if (tagList.isEmpty()) {
            return null;
        }
        return tagList;
    }

    public List<Record> getUserInfo(Integer pid, Integer uid) {
        List<Record> userInfo = Db.find(
            "SELECT uid,sid,pid,cid,result,ctime,num,time,memory,codeLen,language FROM solution WHERE uid=? AND pid=? AND status=1 GROUP BY result",
            uid, pid);
        return userInfo;
    }

    public Record getUserResult(Integer pid, Integer uid) {
        Record record =
            Db.findFirst("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? AND status=1 LIMIT 1", uid,
                pid);
        return record;
    }

    public List<Record> getUserProblemResult(Integer uid) {
        List<Record> records = null;
        if (OjConfig.isDevMode()) {
            records =
                Db.find("SELECT pid,MIN(result) AS result FROM solution WHERE uid=? AND status=1 GROUP BY pid", uid);
        } else {
            records = Db.findByCache("userResult", uid,
                "SELECT pid,MIN(result) AS result FROM solution WHERE uid=? AND status=1 GROUP BY pid", uid);
        }
        return records;
    }

    public Integer getUserResult(Integer pid) {
        Integer uid = userService.getCurrentUid();
        if (uid == null)
            return null;

        return Db.queryInt("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? AND status=1 LIMIT 1", uid,
            pid);
    }

    public <T> T getProblemField(Integer pid, String name) {
        String[] fields =
            {"title", "timeLimit", "memoryLimit", "description", "input", "output", "sampleInput", "sampleOutput",
                "hint", "source", "status"};
        if (StringUtil.equalsOne(name, fields) == -1) {
            return null;
        }

        ProblemModel problemModel = findProblem(pid);
        if (problemModel == null) {
            return null;
        }

        return problemModel.get(name);
    }

    public Page<ProblemModel> getProblemPage(int pageNumber, int pageSize) {
        String sql =
            "SELECT pid,title,source,accepted,submission,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime_t,status";
        StringBuilder sb = new StringBuilder().append("FROM problem");
        if (!userService.isAdmin())
            sb.append(" WHERE status=1");
        sb.append(" ORDER BY pid");

        Page<ProblemModel> problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString());

        return problemList;
    }

    public Page<ProblemModel> getProblemPageDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir,
        String sSearch) {
        List<Object> param = new ArrayList<Object>();
        String sql = "SELECT pid,title,source,accepted,submission,ctime,status";
        StringBuilder sb = new StringBuilder().append("FROM problem WHERE 1=1");
        if (StringUtil.isNotEmpty(sSearch)) {
            sb.append(" AND (pid LIKE ? OR title LIKE ?)");
            param.add(new StringBuilder(3).append(sSearch).append("%").toString());
            param.add(new StringBuilder(3).append("%").append(sSearch).append("%").toString());
        }
        sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", pid");

        Page<ProblemModel> problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), param.toArray());

        return problemList;
    }

    public int getPageNumber(Integer pid, int pageSize) {
        long pageNumber = 0;
        StringBuilder sb = new StringBuilder().append("SELECT COUNT(*) AS idx FROM problem WHERE pid<?");
        if (!userService.isAdmin())
            sb.append(" AND status=1");
        sb.append(" ORDER BY pid LIMIT 1");

        pageNumber = dao.findFirst(sb.toString(), pid).getLong("idx");
        pageNumber = (pageNumber + pageSize) / pageSize;
        return (int) pageNumber;
    }

    public SolutionModel getSolution(Integer pid, Integer sid) {
        Integer uid = userService.getCurrentUid();
        StringBuilder sb =
            new StringBuilder("SELECT pid,uid,language,source FROM solution WHERE sid=? AND pid=? AND status=1");

        if (!userService.isAdmin())
            sb.append(" AND uid=").append(uid);
        sb.append(" LIMIT 1");

        return SolutionModel.dao.findFirst(sb.toString(), sid, pid);
    }

    public List<SolutionModel> getProblemStatus(Integer pid) {
        List<SolutionModel> resultList = SolutionModel.dao
            .find("SELECT result,COUNT(*) AS count FROM solution WHERE pid=? AND status=1 GROUP BY result", pid);

        for (SolutionModel record : resultList) {
            try {
                ResultType resultType = OjConfig.resultType.get(record.getInt("result"));
                record.put("longName", resultType.getLongName());
                record.put("name", resultType.getName());
            } catch (NullPointerException e) {
                if (OjConfig.isDevMode())
                    e.printStackTrace();
                log.warn(e.getLocalizedMessage());
            }
        }

        return resultList;
    }

    public Page<ProblemModel> searchProblem(int pageNumber, int pageSize, String scope, String word) {
        Page<ProblemModel> problemList = null;
        List<Object> paras = new ArrayList<Object>();
        String sql = "SELECT pid,title,accepted,submission,source,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime_t";

        if (StringUtil.isNotBlank(word)) {
            word = new StringBuilder(3).append("%").append(HtmlDecoder.decode(word)).append("%").toString();
            StringBuilder sb = new StringBuilder("FROM problem WHERE (");
            if (StringUtil.isNotBlank(scope)) {
                String scopes[] = {"title", "source", "content", "tag"};
                if (StringUtil.equalsOneIgnoreCase(scope, scopes) == -1)
                    return null;
                if ("tag".equalsIgnoreCase(scope)) {
                    sb.append("pid IN (SELECT pid FROM tag WHERE tag LIKE ? AND status=1)");
                } else if ("content".equalsIgnoreCase(scope)) {
                    sb.append("description LIKE ? ");
                } else {
                    sb.append(scope).append(" LIKE ? ");
                }
                paras.add(word);
            } else {
                sb.append("title LIKE ? OR source LIKE ? OR description LIKE ?");
                paras.add(word);
                paras.add(word);
                paras.add(word);
            }
            sb.append(" ) AND status=1 ORDER BY accepted desc,submission desc,pid");
            problemList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());
        }

        return problemList;
    }

    public boolean checkSpj(Integer pid) {
        File dataDir = new File(
            new StringBuilder(3).append(OjConfig.getString("dataPath")).append(File.separator).append(pid).toString());
        if (!dataDir.isDirectory()) {
            return false;
        }

        StringBuilder sb = new StringBuilder(4).append(dataDir.getAbsolutePath()).append(File.separator).append("spj");
        File spjFile = new File(sb.toString());
        if (spjFile.isFile()) {
            return true;
        }
        spjFile = new File(sb.append(".c").toString());
        if (spjFile.isFile()) {
            String cmd = new StringBuilder(4).append("gcc -o ").append(sb.toString()).append(" ")
                .append(sb.append(".c").toString()).toString();
            try {
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                if (OjConfig.isDevMode())
                    e.printStackTrace();
                log.error(e.getLocalizedMessage());
                return false;
            }
            return true;
        }

        spjFile = new File(sb.append(".cc").toString());
        if (spjFile.isFile()) {
            String cmd = new StringBuilder(4).append("g++ -o ").append(sb.toString()).append(" ")
                .append(sb.append(".cc").toString()).toString();
            try {
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                if (OjConfig.isDevMode())
                    e.printStackTrace();
                log.error(e.getLocalizedMessage());
                return false;
            }
            return true;
        }

        return false;
    }

    public boolean addTag(Integer pid, String tag) {
        Integer uid = userService.getCurrentUid();
        Record Tag = new Record().set("pid", pid).set("uid", uid).set("tag", tag).set("ctime", OjConfig.timeStamp);
        return Db.save("tag", Tag);
    }

    public boolean addProblem(ProblemModel problemModel) throws IOException {
        int ctime = OjConfig.timeStamp;
        problemModel.setCtime(ctime).setMtime(ctime);
        if (problemModel.getStatus() == null) {
            problemModel.setStatus(false);
        }

        problemModel.set("uid", userService.getCurrentUid());
        problemModel.save();

        File dataDir = new File(new StringBuilder(3).append(OjConfig.getString("dataPath")).append(File.separator)
            .append(problemModel.getInt("pid")).toString());
        if (dataDir.isDirectory()) {
            log.warn("Data directory already exists: " + dataDir.getPath());
            return false;
        }

        FileUtil.mkdirs(dataDir);

        return true;
    }

    public Boolean updateProblemByField(Integer pid, String name, String value) {
        // TODO store tags in problem table
        String[] fields =
            {"title", "timeLimit", "memoryLimit", "description", "input", "output", "sampleInput", "sampleOutput",
                "hint", "source", "status"};
        if (StringUtil.equalsOne(name, fields) == -1) {
            return false;
        }

        ProblemModel problemModel = findProblem(pid);
        if (problemModel == null) {
            return false;
        }

        int mtime = OjConfig.timeStamp;
        if ("status".equals(name)) {
            problemModel.set(name, Boolean.valueOf(value));
        } else if ("timeLimit".equals(name) || "memoryLimit".equals(name)) {
            Integer intValue = Integer.parseInt(value);
            problemModel.set(name, intValue);
        } else {
            problemModel.set(name, value);
        }
        updateCache(problemModel);

        return problemModel.setMtime(mtime).update();
    }

    public boolean updateProblem(ProblemModel newProblemModel) {
        ProblemModel problemModel = findProblem(newProblemModel.getPid());
        problemModel.merge(newProblemModel);
        problemModel.setMtime(OjConfig.timeStamp);
        if (newProblemModel.getStatus() == null) {
            problemModel.setStatus(false);
        }
        updateCache(problemModel);

        return problemModel.update();
    }

    public Integer getViewCount(Integer pid) {
        return dao.findById(pid).getView();
    }

    public void setViewCount(Integer pid, Integer view) {
        ProblemModel problemModel = dao.findById(pid);
        problemModel.setView(view).update();
        updateCache(problemModel);
    }

    public boolean incSubmission(Integer pid) {
        ProblemModel problemModel = findProblem(pid);

        problemModel.setSubmission(problemModel.getSubmission() + 1);
        problemModel.setStime(OjConfig.timeStamp);
        updateCache(problemModel);

        return problemModel.update();

    }

    public boolean incAccepted(SolutionModel solutionModel) {
        Integer pid = solutionModel.getPid();
        Integer sid = solutionModel.getSid();
        Integer uid = solutionModel.getUid();
        ProblemModel problemModel = findProblem(pid);

        problemModel.setAccepted(problemModel.getAccepted() + 1);
        Integer lastAccepted =
            Db.queryInt("SELECT sid FROM solution WHERE pid=? AND uid=? AND sid<? AND result=? AND status=1 LIMIT 1",
                pid, uid, sid, ResultType.AC);
        if (lastAccepted == null) {
            problemModel.setSolved(problemModel.getSolved() + 1);
        }
        updateCache(problemModel);

        return problemModel.update();
    }

    public boolean revertAccepted(SolutionModel solutionModel) {
        if (solutionModel.getResult() != ResultType.AC) {
            return false;
        }

        Integer pid = solutionModel.getPid();
        Integer sid = solutionModel.getSid();
        Integer uid = solutionModel.getUid();
        ProblemModel problemModel = findProblem(pid);

        problemModel.setAccepted(problemModel.getAccepted() - 1);
        Integer lastAccepted =
            Db.queryInt("SELECT sid FROM solution WHERE pid=? AND uid=? AND sid<? AND result=? AND status=1 LIMIT 1",
                pid, uid, sid, ResultType.AC);
        if (lastAccepted == null) {
            problemModel.setSolved(problemModel.getSolved() - 1);
        }

        return problemModel.update();
    }

    public boolean reset(Integer pid) {
        ProblemModel problemModel = findProblem(pid);
        if (problemModel == null) {
            throw new ProblemException("Problem is not exist!");
        }

        long submission = Db.queryLong("SELECT COUNT(*) AS count FROM solution WHERE pid=? AND status=1 LIMIT 1", pid);
        long submitUser =
            Db.queryLong("SELECT COUNT(uid) AS count FROM solution WHERE pid=? AND status=1 LIMIT 1", pid);
        problemModel.setSubmission((int) submission);
        problemModel.setSubmitUser((int) submitUser);
        problemModel.setAccepted(0);
        problemModel.setSolved(0);
        problemModel.setRatio(0);
        problemModel.setDifficulty(0);

        return problemModel.update();
    }

    public boolean build(Integer pid) {
        ProblemModel problemModel = findProblem(pid);
        if (problemModel == null) {
            throw new ProblemException("Problem is not exist!");
        }

        long accepted =
            Db.queryLong("SELECT COUNT(*) AS count FROM solution WHERE pid=? AND result=? AND status=1 LIMIT 1", pid,
                ResultType.AC);
        long submission = Db.queryLong("SELECT COUNT(*) AS count FROM solution WHERE pid=? AND status=1 LIMIT 1", pid);
        long submitUser =
            Db.queryLong("SELECT COUNT(uid) AS count FROM solution WHERE pid=? AND status=1 LIMIT 1", pid);
        long solved =
            Db.queryLong("SELECT COUNT(uid) AS count FROM solution WHERE pid=? AND result=? AND status=1 LIMIT 1", pid,
                ResultType.AC);

        problemModel.setSubmission((int) submission);
        problemModel.setAccepted((int) accepted);
        problemModel.setSubmitUser((int) submitUser);
        problemModel.setSolved((int) solved);
        updateCache(problemModel);

        return problemModel.update();
    }

    public boolean isUserSolvedProblem(Integer uid, Integer pid) {
        Long result =
            Db.queryLong("SELECT 1 FROM solution WHERE result=? AND uid=? AND pid=? LIMIT 1", ResultType.AC, uid, pid);
        return result != null && result.intValue() > 0;
    }

    private void updateCache(ProblemModel problemModel) {
        CacheKit.put("problem", problemModel.getPid(), problemModel);
    }

}
