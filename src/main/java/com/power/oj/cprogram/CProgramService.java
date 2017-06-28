package com.power.oj.cprogram;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestProblemModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.solution.SolutionModel;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;
import com.sun.org.apache.bcel.internal.generic.Select;

import javax.mail.search.RecipientStringTerm;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                        "contest.startTime, " +
                        "contest.endTime, " +
                        "FROM_UNIXTIME(startTime, '%Y-%m-%d %H:%i:%s') AS startDateTime, "+
                        "FROM_UNIXTIME(endTime, '%Y-%m-%d %H:%i:%s') AS endDateTime ";
        if(type == ContestModel.TYPE_WORK) {
            sql += ",user.realName ";
        }
        if(type != ContestModel.TYPE_EXPERIMENT) {
            sql += ",contest.lockBoardTime " +
                    ",contest.unlockBoardTime ";
        }
        int startTime = getStartUnixTime();
        int endTime = getEndUnixTime();
        List<Object> parase = new ArrayList<>();
        String sqlExe = "FROM contest ";
        if(type == ContestModel.TYPE_WORK) {
            sqlExe += "INNER JOIN user ON contest.uid = user.uid ";
        }

        sqlExe += "WHERE " +
                "contest.type = ? " +
                "AND (? <= contest.startTime AND contest.startTime <= ?) ";
        parase.add(type);
        parase.add(startTime);
        parase.add(endTime);

        if(!isTeacher() && type == ContestModel.TYPE_WORK) {
            int uid = UserService.me().getCurrentUid();
            Record record = Db.findById("cprogram_user_info","uid", uid);
            sqlExe += "AND contest.uid = ? AND contest.lockBoardTime = ? AND contest.unlockBoardTime = ? ";
            parase.add(record.get("tid"));
            parase.add(record.get("class_week"));
            parase.add(record.get("class_lecture"));
        }
        sqlExe += "ORDER BY contest.startTime DESC ";
        Page<ContestModel> page = ContestModel.dao.paginate(pageNumber, pageSize, sql, sqlExe, parase.toArray());
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
    static public List<Record> GetScoreList(Integer cid, Integer type) {
        List<Object> parase = new ArrayList<>();
        String sql = "select score.*, " +
                "cprogram_user_info.stuid, " +
                "cprogram_user_info.class as Class, " +
                "user.name, " +
                "user.realName " +
                "from score " +
                "left join user on score.uid = user.uid " +
                "left join cprogram_user_info on score.uid = cprogram_user_info.uid " +
                "where cid = ? ";
        parase.add(cid);
        if(!isTeacher()) {
            sql += "and score.uid = ? ";
            parase.add(UserService.me().getCurrentUid());
        }
        else if(type != ContestModel.TYPE_WORK && !ShiroKit.hasPermission("root")) {
            sql += "and score.week = ? and score.lecture = ? ";
            int week = CProgramService.getWeek(OjConfig.timeStamp);
            int lecture = CProgramService.getLecture(OjConfig.timeStamp);
            if(lecture == 0) lecture = CProgramService.getLecture(OjConfig.timeStamp - 15 * 60);
            parase.add(week);
            parase.add(lecture);
        }
        sql += " ORDER BY cprogram_user_info.stuid ";
        return Db.find(sql, parase.toArray());
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
                score.set("week", getWeek(OjConfig.timeStamp));
                score.set("lecture", getLecture(OjConfig.timeStamp));
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
    static public String RandomPassword() {
        String source = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String password = "";
        for(int i = 0; i < 8; i++) {
            int index = (int)Math.floor(Math.random() * source.length());
            password += source.charAt(index);
        }
        return password;
    }

    static public File AddPassword(int cid, int number) {
        File file = new File(OjConfig.downloadPath , "password-" + cid + ".txt");
        try {
            file.createNewFile();
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
            List<Record> list = new ArrayList<>();
            for(int i = 0; i < number; i++) {
                String password = RandomPassword();
                Record record = new Record();
                record.set("cid", cid);
                record.set("password", password);
                list.add(record);
                writer.write(password+"\n\n");
            }
            Db.batchSave("cprogram_password", list, list.size());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    static public boolean isRegister() {
        if(isTeacher()) return  true;
        Record record = Db.findById("cprogram_user_info","uid", UserService.me().getCurrentUid());
        return record != null;
    }

    static public List<Record> GetTeacherList() {
        return Db.find("select user.realName, user.uid from user inner join user_role on user.uid = user_role.uid where user_role.rid = 4");
    }

    static int getWeek(int unix_time) {
        Date date = new Date(unix_time * 1000L);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }
    static int getLecture(int unix_time) {
        Date date = new Date(unix_time * 1000L);
        Date startDate = new Date(unix_time * 1000L);
        Date endDate = new Date(unix_time * 1000L);
        startDate.setSeconds(0);
        endDate.setSeconds(0);
        for(int i = 0; i < CProgramConstants.startTimeHour.length; i++) {
            startDate.setHours(CProgramConstants.startTimeHour[i]);
            startDate.setMinutes(CProgramConstants.startTimeMin[i]);
            endDate.setHours(CProgramConstants.endTimeHour[i]);
            endDate.setMinutes(CProgramConstants.endTimeMin[i]);
            long startTime = startDate.getTime();
            long endTime = endDate.getTime();
            if(startTime <= unix_time * 1000L && unix_time * 1000L <= endTime) {
                return i + 1;
            }
        }
        return 0;
    }
    static int getStartUnixTime() {
        Date date = new Date(OjConfig.timeStamp * 1000L);
        if(2 <= date.getMonth() && date.getMonth() <= 7) {
            date.setMonth(2);
            date.setDate(1);
        }
        else {
            date.setMonth(8);
            date.setDate(1);
        }
        return (int)(date.getTime() / 1000);
    }
    static int getEndUnixTime() {
        Date date = new Date(OjConfig.timeStamp * 1000L);
        if(2 <= date.getMonth() && date.getMonth() <= 7) {
            date.setMonth(7);
            date.setDate(31);
        }
        else {
            if(date.getMonth() <= 12) {
                date.setYear(date.getYear() + 1);
            }
            date.setMonth(1);
            date.setMinutes(31);
        }
        return (int)(date.getTime() / 1000);
    }
}
