package com.power.oj.cprogram;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by w7037 on 2017/6/14.
 */
public interface CProgramConstants {
    int PageSize = 20;
    final int startTimeHour[] = {8, 10, 14, 16, 19};
    final int startTimeMin[] = {0, 0, 0, 0, 0};
    final int endTimeHour[] = {9, 11, 15, 17, 20};
    final int endTimeMin[] = {40, 40, 40, 40, 40};

    final int oneWeek = 7 * 24 * 3600 * 1000;
    final int twoHours = 2 * 3600 * 1000;
    String teacherPermissionString = "teacher";

    String teacherUserString = "TeacherUser";
    String weeksMapString = "weeksMap";
    String lectureMapString = "lecturesMap";
    String contestTypeMapString = "contestTypeMap";

    final Map<Integer, String> weeksMap = new HashMap<>();
    final Map<Integer, String> lectureMap = new HashMap<>();
    final Map<String, String> contestTypeMap = new HashMap<>();

    public static void load() {
        weeksMap.put(1, "星期一");
        weeksMap.put(2, "星期二");
        weeksMap.put(3, "星期三");
        weeksMap.put(4, "星期四");
        weeksMap.put(5, "星期五");
        weeksMap.put(6, "星期六");
        weeksMap.put(0, "星期日");

        lectureMap.put(1, "第一讲");
        lectureMap.put(2, "第二讲");
        lectureMap.put(3, "第三讲");
        lectureMap.put(4, "第四讲");
        lectureMap.put(5, "第五讲");
        lectureMap.put(6, "第六讲");
        lectureMap.put(0, "其他时间");

        contestTypeMap.put("HOMEWORK", "作业");
        contestTypeMap.put("EXPERIMENT", "实验");
        contestTypeMap.put("EXPERIMENT_EXAM", "实验考试");
        contestTypeMap.put("COURSE_EXAM", "课程考试");
    }

}
