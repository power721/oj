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
}
