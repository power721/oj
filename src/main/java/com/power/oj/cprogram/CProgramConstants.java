package com.power.oj.cprogram;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by w7037 on 2017/6/14.
 */
public interface CProgramConstants {
    String TeacherUser = "TeacherUser";
    int PageSize = 20;
    final Map<Integer, String> weeks = new HashMap<>();
    final Map<Integer, String> lecture = new HashMap<>();
    public static void load() {
        weeks.put(1, "星期一");
        weeks.put(2, "星期二");
        weeks.put(3, "星期三");
        weeks.put(4, "星期四");
        weeks.put(5, "星期五");
        weeks.put(6, "星期六");
        weeks.put(7, "星期日");

        lecture.put(1, "第一讲");
        lecture.put(2, "第二讲");
        lecture.put(3, "第三讲");
        lecture.put(4, "第四讲");
        lecture.put(5, "第五讲");
        lecture.put(6, "第六讲");
    }

}
