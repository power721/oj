package com.power.oj.contest.model;

import com.jfinal.plugin.activerecord.Model;

public class ContestProblemModel extends Model<ContestProblemModel> {
    public static final ContestProblemModel dao = new ContestProblemModel();
    public static final String ID = "id";
    public static final String CID = "cid";
    public static final String PID = "pid";
    public static final String TITLE = "title";
    public static final String TIME_LIMIT = "timeLimit";
    public static final String MEMORY_LIMIT = "memoryLimit";
    public static final String NUM = "num";
    public static final String ACCEPTED = "accepted";
    public static final String SUBMISSION = "submission";
    public static final String FIRST_BLOOD_UID = "firstBloodUid";
    public static final String FIRST_BLOOD_TIME = "firstBloodTime";
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return getInt(ID);
    }

    public ContestProblemModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getCid() {
        return getInt(CID);
    }

    public ContestProblemModel setCid(Integer value) {
        return set(CID, value);
    }

    public Integer getPid() {
        return getInt(PID);
    }

    public ContestProblemModel setPid(Integer value) {
        return set(PID, value);
    }

    public String getTitle() {
        return getStr(TITLE);
    }

    public ContestProblemModel setTitle(String value) {
        return set(TITLE, value);
    }

    public Integer getTimeLimit()
    {
        return getInt(TIME_LIMIT);
    }

    public ContestProblemModel setTimeLimit(Integer value)
    {
        return set(TIME_LIMIT, value);
    }

    public Integer getMemoryLimit()
    {
        return getInt(MEMORY_LIMIT);
    }

    public ContestProblemModel setMemoryLimit(Integer value)
    {
        return set(MEMORY_LIMIT, value);
    }

    public Integer getNum() {
        return getInt(NUM);
    }

    public ContestProblemModel setNum(Integer value) {
        return set(NUM, value);
    }

    public Integer getAccepted() {
        return getInt(ACCEPTED);
    }

    public ContestProblemModel setAccepted(Integer value) {
        return set(ACCEPTED, value);
    }

    public Integer getSubmission() {
        return getInt(SUBMISSION);
    }

    public ContestProblemModel setSubmission(Integer value) {
        return set(SUBMISSION, value);
    }

    public Integer getFirstBloodUid() {
        return getInt(FIRST_BLOOD_UID);
    }

    public ContestProblemModel setFirstBloodUid(Integer value) {
        return set(FIRST_BLOOD_UID, value);
    }

    public Integer getFirstBloodTime() {
        return getInt(FIRST_BLOOD_TIME);
    }

    public ContestProblemModel setFirstBloodTime(Integer value) {
        return set(FIRST_BLOOD_TIME, value);
    }

}
