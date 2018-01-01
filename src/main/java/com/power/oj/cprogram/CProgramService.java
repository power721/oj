package com.power.oj.cprogram;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;

import java.io.*;
import java.util.*;


/**
 * Created by w7037 on 2017/6/14.
 */
public final class CProgramService {
    private static final Logger LOGGER = Logger.getLogger(CProgramMainController.class);
    static Page<ContestModel> getContestList(int pageNumber, int pageSize, int type) {
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
    static public ContestSolutionModel getSolution(Integer cid, Integer sid) {
        String sql = "select " +
                "contest_solution.*, " +
                "user.name " +
                "from " +
                "contest_solution inner join user on user.uid = contest_solution.uid " +
                "where cid = " + cid +
                " and sid = " + sid +
                " and contest_solution.status = 1";
        ContestSolutionModel solution = ContestSolutionModel.dao.findFirst(sql);
        if(solution == null)
            return null;
        solution.put("alpha", (char)(solution.getNum() + 'A'));
        return solution;
    }
    static public List<Record> getScoreList(Integer cid, Integer type) {
        List<Object> parase = new ArrayList<>();
        String sql = "select score.*, " +
                "cprogram_user_info.stuid, " +
                "cprogram_user_info.class as Class, " +
                "cprogram_user_info.tid, " +
                "user.name, " +
                "user.realName " +
                "from score " +
                "inner join user on score.uid = user.uid " +
                "inner join cprogram_user_info on score.uid = cprogram_user_info.uid " +
                "where cid = ? ";
        parase.add(cid);
        if(!isTeacher()) {
            sql += "and score.uid = ? ";
            parase.add(UserService.me().getCurrentUid());
        }
        else if(type == ContestModel.TYPE_EXPERIMENT && !ShiroKit.hasPermission("root")) {
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

    static public int getSolutionResult(Integer sid) {
        ContestSolutionModel solution = SolutionService.me().findContestSolution(sid);
        Integer result = Db.queryInt("select MIN(result) from contest_solution where cid = ? and pid = ? and uid = ?", solution.getCid(), solution.getPid(), solution.getUid());
        if(result == null) return 999;
        return  result;
    }

    static public void updateScore(Integer cid, Integer sid, Integer result) {
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
            score.set("ctime", OjConfig.timeStamp);
            score.set("week", getWeek(OjConfig.timeStamp));
            score.set("lecture", getLecture(OjConfig.timeStamp));
            score.set("accepted", 0);
            score.set("score1", 0);
            score.set("score2", 0);
            if(result == ResultType.AC) {
                score.set("accepted", 1);
                score.set("score1", preScore);
                score.set("score2", preScore);
            }
            Db.save("score","rid", score);
        }
        else {
            score.set("submited", score.getInt("submited") + 1);
            if(result== ResultType.AC && getSolutionResult(sid) != ResultType.AC) {
                score.set("accepted", score.getInt("accepted") + 1);
                Integer newScore = score.getInt("score1") + preScore;
                if(score.getInt("accepted").equals(totProblem)) {
                    newScore = 100;
                }
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
    static public String randomPassword() {
        String source = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String password = "";
        Random rand = new Random();
        for(int i = 0; i < 8; i++) {
            int index = rand.nextInt(source.length());
            password += source.charAt(index);
        }
        return password;
    }

    static public File addPassword(int cid, int number) {
        File file = new File(OjConfig.downloadPath , "password-" + cid + ".txt");
        try {
            file.createNewFile();
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
            List<Record> list = new ArrayList<>();
            for(int i = 0; i < number; i++) {
                String password = randomPassword();
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
            LOGGER.error("can't creat the password file");
        }
        return file;
    }

    static public boolean isRegister() {
        if(isTeacher()) return  true;
        Record record = Db.findById("cprogram_user_info","uid", UserService.me().getCurrentUid());
        return record != null;
    }

    static public List<Record> getTeacherList() {
        return Db.find("select user.realName, user.uid from user inner join user_role on user.uid = user_role.uid where user_role.rid = 4");
    }

    public static int getWeek(int unix_time) {
        Date date = new Date(unix_time * 1000L);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }
    public static int getLecture(int unix_time) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(new Date(unix_time * 1000L));
        endDate.setTime(new Date(unix_time * 1000L));
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        for(int i = 0; i < CProgramConstants.startTimeHour.length; i++) {
            startDate.set(Calendar.HOUR_OF_DAY, CProgramConstants.startTimeHour[i]);
            startDate.set(Calendar.MINUTE, CProgramConstants.startTimeMin[i]);
            endDate.set(Calendar.HOUR_OF_DAY, CProgramConstants.endTimeHour[i]);
            endDate.set(Calendar.MINUTE, CProgramConstants.endTimeMin[i]);
            long startTime = startDate.getTime().getTime();
            long endTime = endDate.getTime().getTime();
            if(startTime <= unix_time * 1000L && unix_time * 1000L <= endTime) {
                return i + 1;
            }
        }
        return 0;
    }
    public static int getStartUnixTime(int ctime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(ctime * 1000L));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(2 - 1 <= calendar.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) <= 7 - 1) {
            calendar.set(Calendar.MONTH, 2 - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        else {
            if(calendar.get(Calendar.MONTH) < 2 - 1) {
                int year = calendar.get(Calendar.YEAR);
                year--;
                calendar.set(Calendar.YEAR, year);
            }
            calendar.set(Calendar.MONTH, 8 - 1);
            calendar.set(Calendar.DAY_OF_MONTH,1);
        }
        return (int)(calendar.getTime().getTime() / 1000);
    }
    public static int getStartUnixTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(OjConfig.timeStamp * 1000L));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(2 - 1 <= calendar.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) <= 7 - 1) {
            calendar.set(Calendar.MONTH, 2 - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        else {
            if(calendar.get(Calendar.MONTH) < 2 - 1) {
                int year = calendar.get(Calendar.YEAR);
                year--;
                calendar.set(Calendar.YEAR, year);
            }
            calendar.set(Calendar.MONTH, 8 - 1);
            calendar.set(Calendar.DAY_OF_MONTH,1);
        }
        return (int)(calendar.getTime().getTime() / 1000);
    }
    public static int getEndUnixTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(OjConfig.timeStamp * 1000L));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(2 - 1 <= calendar.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) <= 7 - 1) {
            calendar.set(Calendar.MONTH, 7 - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 31 - 1);
        }
        else {
            if(calendar.get(Calendar.MONTH ) <= 12 - 1) {
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
            }
            calendar.set(Calendar.MONTH, 1 - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 31 - 1);
        }
        return (int)(calendar.getTime().getTime() / 1000);
    }
    public static Boolean needReSignUp() {
        if(isTeacher()) return false;
        if(isRegister()) {
            Integer ctime = Db.queryInt("select ctime from cprogram_user_info where uid=?", UserService.me().getCurrentUid());
            if(getStartUnixTime() != getStartUnixTime(ctime)) {
                return true;
            }
        }
        return false;
    }
    public static String getStuID() {
        Integer uid = UserService.me().getCurrentUid();
        String stdID = Db.queryStr("select stuid from cprogram_user_info where uid=?", uid);
        return stdID;
    }
}
