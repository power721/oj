package com.power.oj.cprogram;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by w7037 on 2017/6/14.
 */
public interface CProgramConstants {
    String teacherUser = "TeacherUser";
    int PageSize = 20;
    final Map<Integer, String> weeks = new HashMap<>();
    final Map<Integer, String> lecture = new HashMap<>();

    final int startTimeHour[] = {8,9,14,15,18,19};
    final int startTimeMin[] = {0,55,0,55,0,50};
    final int endTimeHour[] = {9,12,15,17,19,21};
    final int endTimeMin[] = {35,20,35,30,35,25};

    final int oneWeek = 7 * 24 * 3600 * 1000;
    final int twoHours = 2 * 3600 * 1000;
    public static void load() {
        weeks.put(1, "星期一");
        weeks.put(2, "星期二");
        weeks.put(3, "星期三");
        weeks.put(4, "星期四");
        weeks.put(5, "星期五");
        weeks.put(6, "星期六");
        weeks.put(0, "星期日");

        lecture.put(1, "第一讲");
        lecture.put(2, "第二讲");
        lecture.put(3, "第三讲");
        lecture.put(4, "第四讲");
        lecture.put(5, "第五讲");
        lecture.put(6, "第六讲");
        lecture.put(0, "其他时间");
    }

}
