package com.power.oj.cprogram.admin;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by w703710691d on 2017/6/29.
 */
public class AdminService {
    static List<Record> getContestListForSelect(Integer type) {
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
    static List<Record> getScoreList(Integer type, Integer cid, Integer week, Integer lecture, Integer tid) {
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
            if(week != null && lecture != null) {
                sql += "and score.week=? and score.lecture=? ";
                parase.add(week);
                parase.add(lecture);
            }
            else if(tid != -1 && tid != null){
                sql += "and cprogram_user_info.tid=? ";
                parase.add(tid);
            }
        }
        sql += "and ? <= score.ctime and score.ctime <= ? ";
        parase.add(CProgramService.getStartUnixTime());
        parase.add(CProgramService.getEndUnixTime());
        sql += "order by cprogram_user_info.stuid";
        return Db.find(sql, parase.toArray());
    }
    static List<Record> getAllWorkContest(Integer type, Integer week, Integer lecture) {
        Integer uid = UserService.me().getCurrentUid();
        Integer startTime = CProgramService.getStartUnixTime();
        Integer endTime = CProgramService.getEndUnixTime();
        Integer workType;
        if(type == ContestModel.TYPE_WORK) {
            type = ContestModel.TYPE_COURSE_EXAM;
        }
        if(type == ContestModel.TYPE_EXPERIMENT) {
            type = ContestModel.TYPE_EXPERIMENT_EXAM;
        }
        if(type == ContestModel.TYPE_EXPERIMENT_EXAM) {
            workType = ContestModel.TYPE_EXPERIMENT;
        }
        else {
            workType = ContestModel.TYPE_WORK;
        }
        List<Object> parase = new ArrayList<>();
        String sql = "select cid " +
                "from " +
                "contest " +
                "where " +
                "type=? " +
                "and ?<=startTime and startTime<=? ";
        parase.add(workType);
        parase.add(startTime);
        parase.add(endTime);
        if(workType == ContestModel.TYPE_WORK) {
            sql += "and uid=? ";
            parase.add(uid);
        }
        if(type == ContestModel.TYPE_COURSE_EXAM) {
            sql += "and lockBoardTime=? and unLockBoardTime=? ";
            parase.add(week);
            parase.add(lecture);
        }
        sql += "order by startTime";
        List<Record> contest = Db.find(sql, parase.toArray());
        return contest;
    }
    static List<Record> getAllScoreList(Integer type, Integer week, Integer lecture,
                                        Integer Rate, Integer workTime, Integer tid) {
        Integer uid = UserService.me().getCurrentUid();
        Integer startTime = CProgramService.getStartUnixTime();
        Integer endTime = CProgramService.getEndUnixTime();
        Integer workType;
        if(type == ContestModel.TYPE_WORK) {
            type = ContestModel.TYPE_COURSE_EXAM;
        }
        if(type == ContestModel.TYPE_EXPERIMENT) {
            type = ContestModel.TYPE_EXPERIMENT_EXAM;
        }
        if(type == ContestModel.TYPE_EXPERIMENT_EXAM) {
            workType = ContestModel.TYPE_EXPERIMENT;
        }
        else {
            workType = ContestModel.TYPE_WORK;
        }
        Map<Integer, Map<Integer,Integer> > userScore = new HashMap<>();
        Map<Integer, Integer> userExamScore = new HashMap<>();
        String sql;
        List<Object> parase = new ArrayList<>();
        sql = "select " +
                "score.uid, " +
                "score.cid, " +
                "score.score2, " +
                "contest.type " +
                "from score " +
                "inner join cprogram_user_info on score.uid=cprogram_user_info.uid " +
                "inner join contest on score.cid=contest.cid " +
                "where " +
                "? <= score.ctime " +
                "and score.ctime <= ? " +
                "and (contest.type=? or contest.type=?) ";
        parase.add(startTime);
        parase.add(endTime);
        parase.add(type);
        parase.add(workType);

        if(week != null && lecture != null) {
            sql += "and cprogram_user_info.class_week=? " +
                    "and cprogram_user_info.class_lecture=? ";
            parase.add(week);
            parase.add(lecture);
        }
        if(tid != null && tid != -1) {
            sql += "and cprogram_user_info.tid=? ";
            parase.add(tid);
        }
        List<Record> scoreTable = Db.find(sql, parase.toArray());

        parase.clear();
        sql = "select " +
                "user.name, " +
                "user.realName, " +
                "user.uid, " +
                "cprogram_user_info.stuid, " +
                "cprogram_user_info.class as Class " +
                "from user " +
                "inner join cprogram_user_info on user.uid=cprogram_user_info.uid " +
                "inner join score on user.uid=score.uid and ?<=score.ctime and score.ctime<=? " +
                "where 1=1 ";
        parase.add(startTime);
        parase.add(endTime);
        if(week != null && lecture != null) {
            sql += "and cprogram_user_info.class_week=? " +
                    "and cprogram_user_info.class_lecture=? ";
            parase.add(week);
            parase.add(lecture);
        }
        if(tid != null && tid != -1) {
            sql += "and cprogram_user_info.tid=? ";
            parase.add(tid);
        }
        sql += "group by user.uid order by cprogram_user_info.stuid ";
        List<Record> user = Db.find(sql, parase.toArray());
        for(Record score: scoreTable) {
            Integer UID = score.getInt("uid");
            Integer CID = score.getInt("cid");
            Integer SCORE = score.getInt("score2");
            if(score.get("type") == type) {
                userExamScore.put(UID, SCORE);
            }
            else {
                if(userScore.get(UID) == null) {
                    userScore.put(UID, new HashMap<>());
                }
                userScore.get(UID).put(CID, SCORE);
            }
        }
        for(Record u : user) {
            Integer UID = u.getInt("uid");
            u.set("scoreMap", userScore.get(UID));
            Integer examScore = userExamScore.get(UID);
            if(examScore == null) {
                examScore = 0;
            }
            u.set("examScore", examScore);
            Integer sum = 0;
            if(userScore.get(UID) != null)
                for(Integer i : userScore.get(UID).keySet()) {
                    sum += userScore.get(UID).get(i);
                }
            Integer finalScore = (int)Math.round(sum * Rate / 100.0 /workTime +  examScore * (100 - Rate) / 100.0);
            u.set("finalScore", finalScore);
        }
        return user;
    }
}
