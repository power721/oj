package com.power.oj.cprogram;

import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;
import com.sun.prism.impl.Disposer;
import jodd.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by w7037 on 2017/6/14.
 */
public final class CProgramService {
    static public boolean isTeacher(){
        return ShiroKit.hasPermission("teacher");
    }
    static public Integer GetUid() {
        return UserService.me().getCurrentUid();
    }
    static public Boolean HaveAccepted(int wid, int uid, int pid) {
        Record rd = Db.findFirst("select result from cprogram_solution where wid = ? and uid = ? and pid = ?",wid, uid, pid);
        if(rd == null) return null;
        if(rd.getInt("result") == 0) {
            return true;
        }
        rd = Db.findFirst("select result from cprogram_solution where wid = ? and uid = ? and pid = ? and result = 0", wid, uid, pid);
        return rd != null;
    }
    static public List<Record> GetProblemList(int wid) {
        List<Record> list = Db.find("select " +
                "cprogram_problem.id, " +
                "cprogram_problem.pid, " +
                "cprogram_problem.letter, " +
                "cprogram_problem.title, " +
                "cprogram_problem.accepted, " +
                "cprogram_problem.submission, " +
                "problem.timeLimit, " +
                "memoryLimit " +
                "from cprogram_problem inner join problem on " +
                "cprogram_problem.pid = problem.pid " +
                "where wid = ? " +
                "order by letter", wid);
        if(!CProgramService.isTeacher()) {
            Integer uid = GetUid();
            if(uid != null) {
                for(Record rd : list) {
                    Boolean status = HaveAccepted(wid, uid, rd.getInt("pid"));
                    rd.set("status", status);
                }
            }
        }
        return list;
    }
    static public Record GetWork(int wid) {
        Record rd = Db.findById("cprogram_work", wid);
        return rd;
    }
    static public boolean isWorkFinished(int wid) {
        Record rd = GetWork(wid);
        Date date = rd.get("endTime");
        if(rd != null && date.getTime() < OjConfig.timeStamp * 1000L) {
            return true;
        }
        return false;
    }
    static public  boolean isWorkPending(int wid) {
        Record rd = GetWork(wid);
        Date date = rd.get("startTime");
        if(rd != null && date.getTime() > OjConfig.timeStamp * 1000L) {
            return true;
        }
        return false;
    }
    static public int AddProblem(int wid, int pid, String title) {
        if(isWorkFinished(wid)) {
            return -5;
        }
        ProblemModel problemModel = ProblemService.me().findProblem(pid);
        if (problemModel == null) {
            return -4;
        }
        if (Db.queryInt("SELECT id FROM cprogram_problem WHERE wid=? AND pid=?", wid, pid) != null) {
            return -3;
        }
        int num;
        try {
            num = Db.queryLong("SELECT MAX(letter)+1 FROM cprogram_problem WHERE wid=?", wid).intValue();
        } catch (NullPointerException e) {
            num = 0;
        }
        if (num >= OjConstants.MAX_PROBLEMS_IN_CONTEST) {
            return -2;
        }
        Record rd = new Record();
        rd.set("pid", pid);
        rd.set("title", title);
        rd.set("wid", wid);
        rd.set("accepted", 0);
        rd.set("submission", 0);
        rd.set("letter", num);
        if(Db.save("cprogram_problem", rd)) {
            return num;
        }
        return -1;
    }
    static public int removeProblem(int wid, int pid) {
        if(!isWorkPending(wid)) {
            return -1;
        }
        Integer num = Db.queryInt("SELECT letter FROM cprogram_problem WHERE wid=? AND pid=?", wid, pid);
        int result = Db.update("DELETE FROM cprogram_problem WHERE wid=? AND pid=?", wid, pid);

        List<Record> problems = Db.find("SELECT * FROM cprogram_problem WHERE wid=? AND letter>? ORDER BY letter", wid, num);
        for (Record problem : problems) {
            problem.set("letter", num++);
            Db.update("cprogram_problem", problem);
        }
        return result;
    }
    static public int reorderProblem(Integer wid, String str) {
        if(!isWorkPending(wid)) {
            return -1;
        }
        int result = 0;
        int i = 0;
        String[] pids = str.split(",");
        for (String sPid : pids) {
            Integer pid = Integer.parseInt(sPid);
            result += Db.update("UPDATE cprogram_problem SET letter=? WHERE wid=? AND pid=?", i++, wid, pid);
        }
        return result;
    }
    static public Record GetProblem(Integer wid, Integer letter) {
        Record problem = Db.findFirst("select * from cprogram_problem where wid =? and letter = ?", wid, letter);
        if(problem == null) return null;
        Integer pid = problem.getInt("pid");
        Record rawProblem = Db.findById("problem", "pid", pid);
        problem.set("timeLimit", rawProblem.getInt("timeLimit"));
        problem.set("memoryLimit", rawProblem.getInt("memoryLimit"));
        problem.set("description", rawProblem.getStr("description"));
        problem.set("input", rawProblem.getStr("input"));
        problem.set("output", rawProblem.getStr("output"));

        problem.set("sampleInput", rawProblem.getStr("sampleInput"));
        int sampleInputRows = 1;
        if (StringUtil.isNotBlank(rawProblem.getStr("sampleInput"))) {
            sampleInputRows = StringUtil.count(rawProblem.getStr("sampleInput"), '\n') + 1;
        }
        problem.set("sample_input_rows", sampleInputRows);

        problem.set("sampleOutput", rawProblem.getStr("sampleOutput"));
        int sampleOutputRows = 1;
        if (StringUtil.isNotBlank(rawProblem.getStr("sampleOutput"))) {
            sampleOutputRows = StringUtil.count(rawProblem.getStr("sampleOutput"), '\n') + 1;
        }
        problem.set("sample_output_rows", sampleOutputRows);

        problem.set("hint", rawProblem.getStr("hint"));

        return problem;
    }
}
