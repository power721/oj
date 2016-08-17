package com.power.oj.problem;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.judge.JudgeService;
import com.power.oj.judge.RejudgeType;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;
import jodd.io.FileUtil;
import jodd.util.HtmlDecoder;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        ProblemModel problemModel;

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
        ProblemModel problemModel;

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
        int nextPid;
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
        int prevPid;
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
        List<Record> tagList = Db.find("SELECT t.id,t.tag AS name,u.name AS user,u.uid FROM tag t "
            + "LEFT JOIN user u on u.uid=t.uid WHERE t.pid=? AND t.status=1", pid);

        if (tagList.isEmpty()) {
            return null;
        }
        return tagList;
    }

    public Record addTag(Integer pid, String tag) {
        if (StringUtils.isBlank(tag)) {
            return null;
        }

        Integer uid = userService.getCurrentUid();
        Record old = Db.findFirst("SELECT id FROM tag WHERE pid=? AND tag=? LIMIT 1", pid, tag);
        if (old != null) {
            return old;
        }

        Record Tag = new Record().set("pid", pid).set("uid", uid).set("tag", tag).set("ctime", OjConfig.timeStamp);
        if (Db.save("tag", Tag)) {
            Tag.set("name", Tag.getStr("tag"));
            Tag.set("user", userService.getCurrentUserName());
            return Tag;
        }
        return null;
    }

    public boolean removeTag(Integer pid, Integer tid) {
        if (userService.isAdmin()) {
            return Db.deleteById("tag", tid);
        } else {
            Integer uid = userService.getCurrentUid();
            return Db.update("DELETE FROM tag WHERE id=? AND pid=? AND uid=?", tid, pid, uid) == 1;
        }
    }

    public List<Record> getUserInfo(Integer pid, Integer uid) {
        return Db.find(
            "SELECT uid,sid,pid,cid,result,ctime,num,time,memory,codeLen,language FROM solution WHERE uid=? AND pid=? AND status=1 GROUP BY result",
            uid, pid);
    }

    public Record getUserResult(Integer pid, Integer uid) {
        return Db
            .findFirst("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? AND status=1 LIMIT 1", uid,
                pid);
    }

    public List<Record> getUserProblemResult(Integer uid) {
        List<Record> records;
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

        return dao.paginate(pageNumber, pageSize, sql, sb.toString());
    }

    public Page<ProblemModel> getProblemPageDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir,
        String sSearch) {
        List<Object> param = new ArrayList<>();
        String sql = "SELECT pid,title,source,accepted,submission,ctime,status";
        StringBuilder sb = new StringBuilder().append("FROM problem WHERE 1=1");
        if (StringUtil.isNotEmpty(sSearch)) {
            sb.append(" AND (pid LIKE ? OR title LIKE ?)");
            param.add(sSearch + "%");
            param.add("%" + sSearch + "%");
        }
        sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", pid");

        return dao.paginate(pageNumber, pageSize, sql, sb.toString(), param.toArray());
    }

    public int getPageNumber(Integer pid, int pageSize) {
        long pageNumber;
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
        List<Object> paras = new ArrayList<>();
        String sql = "SELECT pid,title,accepted,submission,source,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime_t";

        if (StringUtil.isNotBlank(word)) {
            word = "%" + HtmlDecoder.decode(word) + "%";
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
        File dataDir = new File(OjConfig.getString("dataPath") + File.separator + pid);
        if (!dataDir.isDirectory()) {
            return false;
        }

        File spjFile = new File(dataDir.getAbsolutePath() + File.separator + "spj");
        if (spjFile.isFile()) {
            return true;
        }

        //        spjFile = new File(sb.append(".c").toString());
        //        if (spjFile.isFile()) {
        //            String cmd = "gcc -o " + sb.toString() + " " + sb.append(".c").toString();
        //            try {
        //                Runtime.getRuntime().exec(cmd);
        //            } catch (IOException e) {
        //                log.error(e.getLocalizedMessage(), e);
        //                return false;
        //            }
        //            return true;
        //        }
        //
        //        spjFile = new File(sb.append(".cc").toString());
        //        if (spjFile.isFile()) {
        //            String cmd = "g++ -o " + sb.toString() + " " + sb.append(".cc").toString();
        //            try {
        //                Runtime.getRuntime().exec(cmd);
        //            } catch (IOException e) {
        //                log.error(e.getLocalizedMessage(), e);
        //                return false;
        //            }
        //            return true;
        //        }

        return false;
    }

    public boolean addProblem(ProblemModel problemModel) throws IOException {
        int ctime = OjConfig.timeStamp;
        problemModel.setCtime(ctime).setMtime(ctime);
        if (problemModel.getStatus() == null) {
            problemModel.setStatus(false);
        }

        problemModel.set("uid", userService.getCurrentUid());
        problemModel.save();

        File dataDir = new File(OjConfig.getString("dataPath") + File.separator + problemModel.getInt("pid"));
        if (dataDir.isDirectory()) {
            log.warn("Data directory already exists: " + dataDir.getPath());
            return false;
        }

        FileUtil.mkdirs(dataDir);
        Files.setPosixFilePermissions(dataDir.toPath(), JudgeService.FILE_PERMISSIONS);

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
        if (problemModel == null) {
            throw new ProblemException("Problem is not exist!");
        }

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
        if (problemModel == null) {
            return false;
        }

        problemModel.setSubmission(problemModel.getSubmission() + 1);
        problemModel.setStime(OjConfig.timeStamp);
        updateCache(problemModel);

        return problemModel.update();
    }

    public boolean incAccepted(Solution solutionModel) {
        Integer pid = solutionModel.getPid();
        Integer sid = solutionModel.getSid();
        Integer uid = solutionModel.getUid();
        ProblemModel problemModel = findProblem(pid);
        if (problemModel == null) {
            throw new ProblemException("Problem is not exist!");
        }

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

    public boolean revertAccepted(Solution solutionModel) {
        if (solutionModel.getResult() != ResultType.AC) {
            return false;
        }

        Integer pid = solutionModel.getPid();
        Integer sid = solutionModel.getSid();
        Integer uid = solutionModel.getUid();
        ProblemModel problemModel = findProblem(pid);
        if (problemModel == null) {
            throw new ProblemException("Problem is not exist!");
        }

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
