package com.power.oj.cprogram.admin.experiment;

import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.cprogram.CProgramConstants;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.cprogram.model.ScoreModel;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExperimentService {
    static public List<Map<String, Object>> getAllScore(Integer tid, Integer week, Integer lecture) {
        Map<Integer, Map<String, Object>> resultMap = new TreeMap<>();
        int startTime = CProgramService.getStartUnixTime();
        int endTime = CProgramService.getEndUnixTime();
        Object o = ScoreModel.dao;
        List<ScoreModel> scoreModelList = ScoreModel.dao.find(
                "SELECT uid,cid,score2 from score s WHERE cid in (SELECT c.cid FROM contest c INNER JOIN cprogram_info ci ON c.cid=ci.cid WHERE c.type=999 AND ci.type=? AND startTime BETWEEN ? AND ?)",
                "EXPERIMENT", startTime, endTime);
        List<UserModel> userModelList = UserModel.dao.find(
                "SELECT u.name,u.uid,u.realName,cu.stuid,cu.classes FROM `user` u INNER JOIN cprogram_user_info cu ON u.uid=cu.uid WHERE cu.ctime BETWEEN ? AND ? AND cu.tid=? AND cu.class_week=? AND cu.class_lecture=?",
                startTime, endTime, tid, week, lecture);
        for (UserModel user : userModelList) {
            Map<String, Object> res = new HashMap<>();
            for (Map.Entry<String, Object> entry : user._getAttrsEntrySet()) {
                res.put(entry.getKey(), entry.getValue());
            }
            res.put("score", new HashMap<>());
            resultMap.put(user.getUid(), res);
        }
        for (ScoreModel scoreModel : scoreModelList) {
            if (resultMap.get(scoreModel.getUid()) == null) {
                continue;
            }
            ((HashMap<Integer, Integer>) resultMap.get(scoreModel.getUid()).get("score")).put(scoreModel.getCid(), scoreModel.getScore2());
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> entry : resultMap.entrySet()) {
            result.add(entry.getValue());
        }
        result.sort((t1, t2) -> {
            String stuId1 = (String) t1.get("stuid");
            String stuId2 = (String) t2.get("stuid");
            return stuId1.compareTo(stuId2);
        });
        return result;
    }

    static public List<ContestModel> getExperimentWorkList() {
        int startTime = CProgramService.getStartUnixTime();
        int endTime = CProgramService.getEndUnixTime();
        return ContestModel.dao.find("SELECT c.cid FROM contest c INNER JOIN cprogram_info ci ON c.cid=ci.cid AND c.type=999 AND ci.type=? AND startTime BETWEEN ? AND ?",
                "EXPERIMENT", startTime, endTime);
    }

    static public File getExperimentXls(Integer tid, Integer week, Integer lecture) throws IOException, WriteException {
        File file;
        DateFormat fmtDateTime = new SimpleDateFormat("yyyyMMdd");
        String fileName = UserService.me().getUser(tid).getRealName();
        fileName += CProgramConstants.weeksMap.get(week);
        fileName += CProgramConstants.lectureMap.get(lecture);
        fileName += "实验";
        fileName += "成绩表" + fmtDateTime.format(new Date()) + ".xls";
        if (OjConfig.isLinux()) {
            file = new File(OjConfig.downloadPath, fileName + ".xls");
        } else {
            file = new File(fileName + ".xls");
        }
        List<ContestModel> contestList = getExperimentWorkList();

        WritableWorkbook book =
                Workbook.createWorkbook(file);
        WritableSheet sheet = book.createSheet("sheet1", 0);
        sheet.addCell(new Label(0, 0, fileName));
        sheet.mergeCells(0, 0, 5 + contestList.size(), 0);
        sheet.addCell(new Label(0, 1, "用户名"));
        sheet.addCell(new Label(1, 1, "姓名"));
        sheet.addCell(new Label(2, 1, "学号"));
        sheet.addCell(new Label(3, 1, "班级"));
        for (int i = 1; i <= contestList.size(); i++) {
            sheet.addCell(new Label(i + 3, 1, "实验" + i));
        }
        Integer idx = 2;
        List<Map<String, Object>> userList = getAllScore(tid, week, lecture);
        for (Map<String, Object> user : userList) {
            sheet.addCell(new Label(0, idx, (String) user.get("name")));
            sheet.addCell(new Label(1, idx, (String) user.get("realName")));
            sheet.addCell(new Label(2, idx, (String) user.get("stuid")));
            sheet.addCell(new Label(3, idx, (String) user.get("classes")));
            Integer row = 4;
            Map<Integer, Integer> scoreMap = (HashMap<Integer, Integer>) user.get("score");
            for (ContestModel c : contestList) {
                Integer sc = scoreMap.get(c.getCid());
                if (sc == null)
                    sc = 0;
                sheet.addCell(new Label(row++, idx, sc.toString()));
            }
            idx++;
        }
        book.write();
        book.close();
        return file;
    }

    static public List<ScoreModel> getScoreByUid(Integer uid) {
        int startTime = CProgramService.getStartUnixTime();
        int endTime = CProgramService.getEndUnixTime();
        List<ScoreModel> scoreModelList = ScoreModel.dao.find(
                "SELECT s.cid,s.score2,c.title,s.submited,s.accepted,s.uid from score s INNER JOIN contest c ON s.cid=c.cid WHERE s.cid in " +
                        "(SELECT c.cid FROM contest c INNER JOIN cprogram_info ci ON c.cid=ci.cid WHERE c.type=999 AND ci.type=? AND startTime BETWEEN ? AND ?) " +
                        "AND s.uid=? order by c.startTime",
                "EXPERIMENT", startTime, endTime,uid);
        return scoreModelList;
    }
}
