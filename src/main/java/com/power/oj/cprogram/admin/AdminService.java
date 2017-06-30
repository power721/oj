package com.power.oj.cprogram.admin;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w703710691d on 2017/6/29.
 */
public class AdminService {
    static List<Record> GetContestListForSelect(Integer type) {
        List<Object> parase = new ArrayList<>();
        String sql = "select cid, title, lockBoardTime AS week, unLockBoardTime AS lecture from contest where type = ? ";
        parase.add(type);
        if(type == ContestModel.TYPE_WORK && !ShiroKit.hasPermission("root")) {
            sql += "and uid = ? ";
            parase.add(UserService.me().getCurrentUid());
        }
        sql += "and startTime >= ? and startTime <= ? ";
        parase.add(CProgramService.getStartUnixTime());
        parase.add(CProgramService.getEndUnixTime());
        return Db.find(sql, parase.toArray());
    }
    static List<Record> GetScoreList(Integer type, Integer cid, Integer week, Integer lecture) {
        List<Object> parase = new ArrayList<>();
        String sql = "select " +
                "score.uid, " +
                "score.score1, " +
                "score.score2," +
                "score.accepted, " +
                "score.submited, " +
                "user.name, " +
                "user.realName, " +
                "cprogram_user_info.stuid, " +
                "cprogram_user_info.class as Class " +
                "from " +
                "score " +
                "inner join user on score.uid=user.uid " +
                "inner join cprogram_user_info on score.uid=cprogram_user_info.uid " +
                "where cid=? ";
        parase.add(cid);
        if(type == ContestModel.TYPE_EXPERIMENT) {
            sql += "and score.week=? and score.lectrue=? ";
            parase.add(week);
            parase.add(lecture);
        }
        sql += "and ? <= score.ctime and score.ctime <= ? ";
        parase.add(CProgramService.getStartUnixTime());
        parase.add(CProgramService.getEndUnixTime());
        sql += "order by cprogram_user_info.stuid";
        return Db.find(sql, parase.toArray());
    }
}
