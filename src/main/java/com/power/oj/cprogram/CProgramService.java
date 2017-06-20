package com.power.oj.cprogram;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestProblemModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.solution.SolutionModel;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;

import javax.mail.search.RecipientStringTerm;
import java.util.List;


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
    static public List<Record> GetScoreList(Integer cid) {
        String sql = "select score.*, " +
                "user.name, user.realName " +
                "from " +
                "score inner join user on score.uid = user.uid " +
                "where cid = " + cid + " ";
        if(!isTeacher()) {
            sql += "and score.uid = " + UserService.me().getCurrentUid();
        }
        return Db.find(sql);
    }

    static public int GetSolutionResult(Integer sid) {
        ContestSolutionModel solution = SolutionService.me().findContestSolution(sid);
        Integer result = Db.queryInt("select MIN(result) from contest_solution where cid = ? and pid = ? and uid = ?", solution.getCid(), solution.getPid(), solution.getUid());
        if(result == null) return 999;
        return  result;
    }

    static public void UpdateScore(Integer cid, Integer sid, Integer result) {
        ContestSolutionModel solution = SolutionService.me().findContestSolution(sid);
        Integer uid = solution.getUid();
        Record score = Db.findFirst("select * from score where cid =? and uid = ?", cid, uid);
        Integer totProblem = ContestService.me().getContestProblems(cid, null).size();
        Integer preScore = (int)Math.round(100.0 / totProblem);
        if(score == null) {
            score = new Record();
            score.set("cid", cid);
            score.set("uid", uid);
            score.set("submited", 1);
            if(result == ResultType.AC) {
                score.set("accepted", 1);
                score.set("score1", preScore);
                score.set("score2", preScore);
            }
            Db.save("score","rid", score);
        }
        else {
            score.set("submited", score.getInt("submited") + 1);
            if(result== ResultType.AC && GetSolutionResult(sid) != ResultType.AC) {
                score.set("accepted", score.getInt("accepted") + 1);
                Integer newScore = score.getInt("score1") + preScore;
                if(newScore > 100) newScore = 100;
                score.set("score1", newScore);
                score.set("score2", newScore);
            }
            Db.update("score", "rid", score);
        }
    }
    static public void updateFinalScore(int cid, int uid ,int score) {
        Record rd = Db.findFirst("select * from score where cid =? and uid = ?", cid, uid);
        rd.set("score2", score);
        Db.update("score", "rid", rd);
    }
}
