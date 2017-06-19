package com.power.oj.cprogram;

import com.jfinal.plugin.activerecord.Page;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.shiro.ShiroKit;


/**
 * Created by w7037 on 2017/6/14.
 */
public final class CProgramService {
    static Page<ContestModel> GetContestList(int pageNumber, int pageSize, int type) {
        String sql =
                "SELECT " +
                        "contest.cid, " +
                        "contest.title, " +
                        "contest.description, " +
                        "startTime, " +
                        "endTime, " +
                        "FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS startDateTime, "+
                        "FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS endDateTime, " +
                        "user.nick";
        String sqlExe =
                "FROM " +
                        "contest " +
                        "INNER JOIN user ON " +
                        "contest.uid = user.uid " +
                        "WHERE " +
                        "contest.type = ? " +
                        "ORDER BY contest.startTime DESC";
        Page<ContestModel> page = ContestModel.dao.paginate(pageNumber, pageSize, sql, sqlExe, type);
        return page;
    }
    static public boolean isTeacher(){
        return ShiroKit.hasPermission("teacher");
    }
    static public ContestSolutionModel GetSolution(Integer cid, Integer sid) {
        String sql = "select " +
                "contest_solution.*, " +
                "user.name " +
                "from " +
                "contest_solution inner join user on user.uid = contest_solution.uid " +
                "where cid = " + cid +
                " and sid = " + sid +
                " and contest_solution.status = 1";
        ContestSolutionModel solution = ContestSolutionModel.dao.findFirst(sql);
        solution.put("alpha", (char)(solution.getNum() + 'A'));
        return solution;
    }
/*
    static public int submitSolution(Record solution) {
        Integer wid = solution.getInt("wid");
        Integer uid = UserService.me().getCurrentUid();
        Record problem = GetProblem(wid, solution.getInt("letter"));
        if(problem == null) return -1;
        solution.set("uid", uid);
        solution.set("pid", problem.get("pid"));
        solution.set("ctime", OjConfig.timeStamp);
        solution.set("mtime", OjConfig.timeStamp);
        solution.set("result", ResultType.WAIT);
        solution.set("time", 0);
        solution.set("memory", 0);
        int len = solution.getStr("source").length();
        if(len < 10 || len > 30000) return -2;
        solution.set("codeLen", len);
        if(Db.save("cprogram_solution", solution)) {
            Db.update("UPDATE cprogram_problem SET submission=submission+1 WHERE wid=? AND letter=?", wid, solution.getInt("letter"));
            ContestSolutionModel contestSolution =  new ContestSolutionModel();
            contestSolution.put(solution);
            JudgeService.me().judge(contestSolution);
        }
        else return -2;
        return  0;
    }
    */
}
