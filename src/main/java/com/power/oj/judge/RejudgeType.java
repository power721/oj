package com.power.oj.judge;

public enum RejudgeType {
    SOLUTION,
    PROBLEM,
    CONTEST_PROBLEM,
    CONTEST;

    public String getKey(Integer id) {
        return name() + id;
    }

    public String getKey(Integer cid, Integer pid) {
        return name() + cid + "-" + pid;
    }
}
