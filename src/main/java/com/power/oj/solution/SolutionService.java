package com.power.oj.solution;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestProblemModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.DataFile;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.core.service.SessionService;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.judge.JudgeResult;
import com.power.oj.judge.JudgeService;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import com.power.oj.user.UserService;
import com.power.oj.util.FileKit;
import jodd.util.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SolutionService {

    private static final Logger LOGGER = Logger.getLogger(SolutionService.class);
    private static final SolutionModel dao = SolutionModel.dao;
    private static final SolutionService me = new SolutionService();

    private static final JudgeService judgeService = JudgeService.me();
    private static final UserService userService = UserService.me();
    private static final ProblemService problemService = ProblemService.me();

    private SolutionService() {
    }

    public static SolutionService me() {
        return me;
    }

    public static void checkCompileError(Solution solution, String error) {
        if (error.contains(OjConfig.getString("dataPath")) || error.contains(OjConfig.getString("workPath"))) {
            String message =
                    "User id " + solution.getUid() + " from " + SessionService.me().getHost() + " try to hack system!";
            LOGGER.warn(message);
            solution.setSystemError(message + "\n" + error);
        } else {
            solution.setError(error);
        }
    }

    public Page<SolutionModel> getPage(int pageNumber, int pageSize, int result, int language, int pid,
                                       String userName) {
        String sql =
                "SELECT sid,s.uid,pid,cid,num,result,test,time,memory,s.language,codeLen,FROM_UNIXTIME(s.ctime, '%Y-%m-%d %H:%i:%s') AS ctime_t,u.name,u.nick";
        StringBuilder sb = new StringBuilder("FROM solution s INNER JOIN user u ON u.uid=s.uid WHERE s.status=1 ");

        List<Object> paras = new ArrayList<Object>();
        if (result > -1) {
            if (result == ResultType.NOT_AC) {
                sb.append(" AND result!=?");
                paras.add(ResultType.AC);
            } else {
                sb.append(" AND result=?");
                paras.add(result);
            }
        }
        if (language > 0) {
            sb.append(" AND s.language=?");
            paras.add(language);
        }
        if (pid > 0) {
            sb.append(" AND pid=?");
            paras.add(pid);
        }
        if (StringUtil.isNotBlank(userName)) {
            String[] names = userName.split(",", 5);
            if (names.length > 0) {
                sb.append(" AND (");
                for (String name : names) {
                    sb.append("name=? OR ");
                    paras.add(name);
                }
                sb.append("1!=1)");
            }
        }

        if (!userService.isAdmin()) {
            sb.append(" AND s.status=1");
        }
        sb.append(" ORDER BY sid DESC");
        Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

        for (SolutionModel solution : solutionList.getList()) {
            solution.put("languageName", (OjConfig.languageType.get(solution.getLanguage())).get("name"));

            ResultType resultType = OjConfig.resultType.get(solution.getResult());
            solution.put("resultName", resultType.getName());
            solution.put("resultLongName", resultType.getLongName());

            /*
             * if (solution.getNum() != null && solution.getNum() > -1) { solution.put("alpha", (char)
             * (solution.getNum() + 'A')); }
             */
        }

        return solutionList;
    }

    public SolutionModel findSolution(Integer sid) {
        return dao.findFirst("SELECT * FROM solution WHERE sid=? AND status=1 LIMIT 1", sid);
    }

    public SolutionModel getSolutionResult(Integer sid) {
        SolutionModel solutionModel =
                dao.findFirst("SELECT sid,time,memory,result,test FROM solution WHERE sid=? AND status=1 LIMIT 1", sid);
        solutionModel.set("result", OjConfig.resultType.get(solutionModel.getResult()));
        return solutionModel;
    }

    public ContestSolutionModel findContestSolution(Integer sid) {
        return ContestSolutionModel.dao
                .findFirst("SELECT * FROM contest_solution WHERE sid=? AND status=1 LIMIT 1", sid);
    }

    public ContestSolutionModel findContestSolution4Json(Integer sid) {
        return ContestSolutionModel.dao.findFirst(
                "SELECT cid,codeLen,s.language,time,memory,num,result,source,s.uid,u.name FROM contest_solution s INNER JOIN user u ON u.uid=s.uid WHERE sid=? AND s.status=1 LIMIT 1",
                sid);
    }

    public ContestSolutionModel getContestSolutionResult(Integer cid, Integer sid) {
        // TODO check permission
        ContestSolutionModel solutionModel = ContestSolutionModel.dao.findFirst(
                "SELECT cid,sid,time,memory,result,test,sim,sim_id FROM contest_solution WHERE cid=? AND sid=? AND status=1 LIMIT 1", cid,
                sid);
        solutionModel.set("result", OjConfig.resultType.get(solutionModel.getResult()));
        return solutionModel;
    }

    public Page<ContestSolutionModel> getPageForContest(int pageNumber, int pageSize, int result, int language, int cid,
                                                        int num, String userName) {
        String sql =
                "SELECT sid,s.uid,pid,cid,num,result,test,time,memory,s.language,codeLen,FROM_UNIXTIME(s.ctime, '%Y-%m-%d %H:%i:%s') AS ctime_t,u.name,u.nick, s.sim,s.sim_id";
        int ContestType = ContestService.me().getContest(cid).getType();
        StringBuilder sb = new StringBuilder("FROM contest_solution s INNER JOIN user u ON u.uid=s.uid ");
        if (ContestType == ContestModel.TYPE_ELSE) {
            sql += ",stuid, u.realName, classes ";
            sb.append(" LEFT JOIN cprogram_user_info cp ON u.uid=cp.uid");
        }
        sb.append(" WHERE cid=?");
        List<Object> paras = new ArrayList<Object>();
        paras.add(cid);

        if (result > -1) {
            if (result == ResultType.NOT_AC) {
                sb.append(" AND result!=?");
                paras.add(ResultType.AC);
            } else {
                sb.append(" AND result=?");
                paras.add(result);
            }
        }
        if (language > 0) {
            sb.append(" AND s.language=?");
            paras.add(language);
        }
        if (num > -1) {
            sb.append(" AND num=?");
            paras.add(num);
        }
        if (StringUtil.isNotBlank(userName)) {
            if (ContestType == ContestModel.TYPE_ELSE) {
                sb.append(" AND (stuid=? OR name=?)");
                paras.add(userName);
                paras.add(userName);
            } else {
                sb.append(" AND name=?");
                paras.add(userName);
            }
        }

        if (userService.isAdmin()) {
            sb.append(" AND s.status=1");
        }
        sb.append(" ORDER BY sid DESC");
        Page<ContestSolutionModel> solutionList =
                ContestSolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

        for (ContestSolutionModel solution : solutionList.getList()) {
            solution.put("languageName", OjConfig.languageName.get(solution.getLanguage()));

            ResultType resultType = OjConfig.resultType.get(solution.getResult());
            solution.put("resultName", resultType.getName());
            solution.put("resultLongName", resultType.getLongName());
            solution.put("alpha", (char) (solution.getNum() + 'A'));
        }

        return solutionList;
    }

    public Page<SolutionModel> getPage(int pageNumber, int pageSize, Integer result, Integer language, Integer uid) {
        String sql = "SELECT sid,s.uid,s.pid,cid,num,result,time,memory,s.language,codeLen,s.ctime,p.title";
        StringBuilder sb = new StringBuilder("FROM solution s LEFT JOIN problem p ON p.pid=s.pid WHERE 1=1");

        List<Object> paras = new ArrayList<Object>();
        if (result > -1) {
            sb.append(" AND s.result=?");
            paras.add(result);
        }
        if (language > 0) {
            sb.append(" AND s.language=?");
            paras.add(language);
        }

        sb.append(" AND s.uid=?");
        paras.add(uid);

        if (!userService.isAdmin()) {
            sb.append(" AND s.status=1");
        }

        sb.append(" ORDER BY sid DESC");
        Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

        for (SolutionModel solution : solutionList.getList()) {
            solution.put("languageName", OjConfig.languageName.get(solution.getLanguage()));

            ResultType resultType = OjConfig.resultType.get(solution.getResult());
            solution.put("resultName", resultType.getName());
            solution.put("resultLongName", resultType.getLongName());
        }

        return solutionList;
    }

    public Page<SolutionModel> getProblemStatusPage(int pageNumber, int pageSize, Integer language, Integer pid) {
        // TODO check user permission for view source code
        String sql = "SELECT sid,s.uid,u.name,pid,result,time,memory,s.language,codeLen,s.ctime,l.name AS language";
        StringBuilder sb = new StringBuilder(
                "FROM solution s INNER JOIN user u ON u.uid=s.uid INNER JOIN program_language l ON l.id=s.language WHERE result=?");

        List<Object> paras = new ArrayList<Object>();
        paras.add(ResultType.AC);

        if (language != null && language > 0) {
            sb.append(" AND s.language=?");
            paras.add(language);
        }

        sb.append(" AND pid=?");
        paras.add(pid);

        sb.append(" AND s.status=1 ORDER BY time,memory,codeLen,sid");
        Page<SolutionModel> solutionList = dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

        return solutionList;
    }

    public Page<ContestSolutionModel> getProblemStatusPageForContest(int pageNumber, int pageSize, int language,
                                                                     int cid, int num) {
        String sql = null;
        ContestModel contestModel = ContestService.me().getContest(cid);
        if (!userService.isAdmin() && contestModel.getType() > ContestModel.TYPE_PASSWORD
                && contestModel.getStartTime() <= OjConfig.timeStamp && contestModel.getEndTime() >= OjConfig.timeStamp) {
            sql = "SELECT sid,s.uid,u.name,pid,result,s.language,s.ctime,l.name AS language";
        } else {
            sql = "SELECT sid,s.uid,u.name,pid,result,time,memory,s.language,codeLen,s.ctime,l.name AS language";
        }
        StringBuilder sb = new StringBuilder(
                "FROM contest_solution s INNER JOIN user u ON u.uid=s.uid INNER JOIN program_language l ON l.id=s.language WHERE result=?");

        List<Object> paras = new ArrayList<Object>();
        paras.add(ResultType.AC);

        if (language > 0) {
            sb.append(" AND s.language=?");
            paras.add(language);
        }

        sb.append(" AND cid=?");
        paras.add(cid);

        sb.append(" AND num=?");
        paras.add(num);

        sb.append(" AND s.status=1 ORDER BY time,memory,codeLen,sid");
        Page<ContestSolutionModel> solutionList =
                ContestSolutionModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), paras.toArray());

        return solutionList;
    }

    public List<ContestSolutionModel> getProblemStatusForContest(Integer cid, Integer num) {
        List<ContestSolutionModel> resultList = ContestSolutionModel.dao.find(
                "SELECT result,COUNT(*) AS count FROM contest_solution WHERE cid=? AND num=? AND status=1 GROUP BY result",
                cid, num);

        for (ContestSolutionModel record : resultList) {
            ResultType resultType = OjConfig.resultType.get(record.getResult());
            record.put("longName", resultType.getLongName());
            record.put("name", resultType.getName());
        }

        return resultList;
    }

    public List<SolutionModel> getSolutionListForProblemRejudge(Integer pid) {
        List<SolutionModel> solutionList =
                dao.find("SELECT * FROM solution WHERE pid=? AND status=1 ORDER BY sid", pid);
        return solutionList;
    }

    public List<SolutionModel> getSolutionListForProblem(Integer pid) {
        List<SolutionModel> solutionList =
                dao.find("SELECT * FROM solution WHERE pid=? AND status=1 ORDER BY sid DESC", pid);
        return solutionList;
    }

    public List<SolutionModel> getWaitSolutionListForProblem(Integer pid) {
        List<SolutionModel> solutionList =
                dao.find("SELECT * FROM solution WHERE pid=? AND result=? AND status=1 ORDER BY sid DESC", pid,
                        ResultType.WAIT);
        return solutionList;
    }

    public List<ContestSolutionModel> getSolutionListForContest(Integer cid) {
        List<ContestSolutionModel> solutionList = ContestSolutionModel.dao
                .find("SELECT * FROM contest_solution WHERE cid=? AND status=1 ORDER BY sid ASC", cid);
        return solutionList;
    }

    public List<ContestSolutionModel> getSolutionListForContestProblem(Integer cid, Integer num) {
        List<ContestSolutionModel> solutionList = ContestSolutionModel.dao
                .find("SELECT * FROM contest_solution WHERE cid=? AND num=? AND status=1 ORDER BY sid", cid, num);
        return solutionList;
    }

    public int submitSolution(SolutionModel solutionModel, boolean style) {
        Integer uid = userService.getCurrentUid();
        Integer pid = solutionModel.getPid();
        ProblemModel problemModel = problemService.findProblem(pid);

        if (problemModel == null) {
            return -1;
        }

        solutionModel.setUid(uid);

        if (solutionModel.addSolution()) {
            judgeService.judge(solutionModel, style);
        } else {
            return -2;
        }

        return 0;
    }

    public boolean canAccessSolution(Integer sid) {
        boolean isAdmin = UserService.me().isAdmin();
        if (isAdmin) {
            return true;
        }

        SolutionModel solutionModel = SolutionService.me().findSolution(sid);
        Integer uid = solutionModel.getUid();
        Integer loginUid = UserService.me().getCurrentUid();
        if (uid.equals(loginUid)) {
            return true;
        }

        int result = solutionModel.getResult();
        return result == ResultType.AC && isUserShareCode(uid) && isUserShareCode(loginUid) && isUserSolvedProblem(
                loginUid, solutionModel.getPid());
    }

    public boolean canAccessSolution(SolutionModel solutionModel) {
        boolean isAdmin = UserService.me().isAdmin();
        if (isAdmin) {
            return true;
        }

        Integer uid = solutionModel.getUid();
        Integer loginUid = UserService.me().getCurrentUid();
        if (uid.equals(loginUid)) {
            return true;
        }

        int result = solutionModel.getResult();
        return result == ResultType.AC && isUserShareCode(uid) && isUserShareCode(loginUid) && isUserSolvedProblem(
                loginUid, solutionModel.getPid());
    }

    private boolean isUserShareCode(Integer uid) {
        return uid != null && UserService.me().getUserByUid(uid).getShareCode();
    }

    public boolean setResult(JudgeResult judgeResult) {
        int result = judgeResult.getResult();
        int sid = judgeResult.getSid();
        int cid = judgeResult.getCid();
        int time = judgeResult.getTime();
        int memory = judgeResult.getMemory();
        int test = judgeResult.getTest();
        String token = judgeResult.getToken();
        String error = judgeResult.getError();

        if (!judgeService.verifyToken(sid, token)) {
            LOGGER.error("verify token for " + (cid > 0 ? cid + "-" : "") + sid + " failed.(" + token + ")");
            return false;
        }

        if(cid != 0 && result == ResultType.AC) {
            if(SolutionService.checkSim(sid, cid)) {
                return true;
            }
        }

        Solution solution;
        if (cid > 0) {
            solution = findContestSolution(sid);
        } else {
            solution = findSolution(sid);
        }

        if (solution != null) {
            solution.setResult(result);
            solution.setTime(time);
            solution.setMemory(memory);
            solution.setTest(test);
            if (result == ResultType.CE) {
                checkCompileError(solution, error);
            } else if (result == ResultType.RE) {
                solution.setError(error);
            } else if (result == ResultType.SE || result == ResultType.RF) {
                solution.setSystemError(error);
            } else if (result == ResultType.WA || result == ResultType.PE) {
                solution.setWrong(error);
            }
            if (cid > 0 && ContestService.me().getContest(cid).getType() == ContestModel.TYPE_ELSE) {
                //CProgramService.updateScore(cid, sid, result);
                //TODO
            }
            boolean updateResult = solution.update();

            if (result == ResultType.AC) {
                if (cid == 0) {
                    userService.incAccepted(solution);
                    problemService.incAccepted(solution);
                }
            }

            if (cid > 0) {
                Integer originalResult = judgeService.removeOriginalResult(sid);
                if (originalResult != null) {
                    ContestService.me().updateBoard4Rejudge(solution, originalResult);
                } else {
                    ContestService.me().updateBoard(solution);
                }
            }
            judgeService.cleanRejudge(solution);
            return updateResult;
        }

        return false;
    }

    public boolean setSystemError(int sid, int cid, String error) {
        Solution solution;
        if (cid > 0) {
            solution = findContestSolution(sid);
        } else {
            solution = findSolution(sid);
        }

        if (solution != null) {
            solution.setResult(ResultType.SE);
            solution.setSystemError(error);

            return solution.update();
        }

        return false;
    }

    private boolean isUserSolvedProblem(Integer uid, Integer pid) {
        return ProblemService.me().isUserSolvedProblem(uid, pid);
    }

    public String getInput(int pid, int test) {
        List<DataFile> dataFiles = new ArrayList<>();
        File dataDir = new File(OjConfig.getString("dataPath") + File.separator + pid);
        if (!dataDir.isDirectory()) {
            return null;
        }
        File[] arrayOfFile = dataDir.listFiles();
        if (arrayOfFile == null) {
            return null;
        }
        Arrays.sort(arrayOfFile);
        boolean isSPJ = problemService.checkSpj(pid);
        int num = 0;
        for (File file : arrayOfFile) {
            if (file.getName().endsWith(".in")) {
                if (isSPJ) {
                    num++;
                } else {
                    String path = file.getAbsolutePath();
                    path = path.substring(0, path.length() - 2) + "out";
                    File outputFile = new File(path);
                    if (outputFile.isFile()) {
                        num++;
                    }
                }
            }
            if (num == test) {
                String data = new String();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if ((data + line).length() > 1024) {
                            data += line;
                            data = data.substring(0, 1024);
                            data += "......";
                            break;
                        } else
                            data += line + "\n";
                    }
                } catch (FileNotFoundException e) {
                    LOGGER.error("Can't read file " + file.getAbsolutePath());
                    return null;
                } catch (IOException e) {
                    LOGGER.error("Read file " + file.getAbsolutePath() + " error");
                    return null;
                }
                return data;
            }
        }
        return null;
    }

    synchronized static public boolean checkSim(Integer sid, Integer cid) {
        ContestSolutionModel solution = SolutionService.me().findContestSolution(sid);
        int pid = solution.getPid();
        ProblemModel problem = ProblemService.me().findProblemForContest(pid, cid);
        int maxSim = problem.getMaxSim();
        if (maxSim == 100) return false;
        String language = OjConfig.languageName.get(solution.getLanguage()).toLowerCase();
        if (language.contains("gcc") || language.contains("g++"))
            language = ".cpp";
        else if (language.contains("java"))
            language = ".java";
        else if (language.contains("pas"))
            language = ".pas";
        else
            return false;
        int num = Db.queryInt("select num from contest_problem where cid = ? and pid = ?", cid, pid);
        String workPath = judgeService.getWorkPath(cid) + "sim" + File.separator + (char) (num + 'A');
        String sourcePath = workPath + File.separator + "source";
        File sourcePathFile = new File(sourcePath);
        File judgeFile = new File(workPath + File.separator + sid + language);
        if (!sourcePathFile.exists()) {
            sourcePathFile.mkdirs();
        }
        if (sourcePathFile.listFiles().length == 0) {
            try {
                FileKit.moveFile(judgeFile, new File(sourcePath + File.separator + sid + language));
            } catch (IOException e) {
                LOGGER.error("Move judgeFile to source fail");
            }
        }
        if (new File(sourcePath + File.separator + sid + language).exists()) {
            return false;
        }
        try {
            judgeFile.createNewFile();
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(judgeFile)));
            writer.print(solution.getSource());
            writer.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't create source file to sim");
            return false;
        } catch (IOException e) {
            LOGGER.error("Can't create source file to sim");
            return false;
        }
        String cmd = "/usr/local/bin/";
        if (language.equals(".cpp"))
            cmd += "sim_c++.exe";
        else if (language.equals(".java"))
            cmd += "sim_java.exe";
        else
            cmd += "sim_pasc.exe ";
        cmd += " -peu -S ";
        cmd += judgeFile.getAbsolutePath();
        cmd += " \"|\" ";
        cmd += sourcePathFile.getAbsolutePath() + File.separator + "*" + language;
        cmd += " | grep \"%\" > " + workPath + File.separator + "result-" + sid + ".txt";
        LOGGER.debug("Will be exec: " + cmd);
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
            process.waitFor();
        } catch (IOException e) {
            LOGGER.error("Can't exec sim");
            return false;
        } catch (InterruptedException e) {
            LOGGER.error("Exec sim fail");
            return false;
        }
        String resultFileName = workPath + File.separator + "result-" + sid + ".txt";
        File result = new File(resultFileName);
        int nowMaxSim = 0, nowMaxSimID = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace(judgeFile.getAbsolutePath(), "");
                line = line.replace(" consists", "");
                line = line.replace(" for", "");
                line = line.replace(" material", "");
                line = line.replace(" of", "");
                line = line.trim();
                Integer pre;
                try {
                    pre = Integer.parseInt(line.substring(0, line.indexOf('%')).trim());
                } catch (Exception ex) {
                    continue;
                }
                if (pre > nowMaxSim) {
                    line = line.substring(line.indexOf('%') + 1);
                    line = line.replace(sourcePath + File.separator, "");
                    line = line.replace(language, "");
                    line = line.trim();
                    try {
                        int simid = Integer.parseInt(line);
                        ContestSolutionModel simSolution = ContestSolutionModel.dao.findById(simid);
                        if(simSolution.getUid().equals(solution.getUid())) {
                            continue;
                        }
                        nowMaxSimID = simid;
                        nowMaxSim = pre;
                    } catch (Exception ex) {
                        continue;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't open result file");
        } catch (IOException e) {
            LOGGER.error("Read result file error");
        }
        try {
            FileKit.moveFile(judgeFile, new File(sourcePath + File.separator + sid + language));
        } catch (IOException e) {
            LOGGER.error("Move judgeFile to source fail");
        }
        if (nowMaxSim > maxSim) {
            solution.setSim(nowMaxSim);
            solution.setSimID(nowMaxSimID);
            solution.setResult(ResultType.SIM);
            solution.update();
            return true;
        }
        return false;
    }
}