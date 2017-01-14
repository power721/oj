package com.power.oj.problem;

import com.jfinal.plugin.activerecord.Model;

import java.util.Map;

public class ProblemModel extends Model<ProblemModel> {
    public static final ProblemModel dao = new ProblemModel();
    public static final String TABLE_NAME = "problem";
    public static final String PID = "pid";
    public static final String UID = "uid";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String INPUT = "input";
    public static final String OUTPUT = "output";
    public static final String SAMPLE_INPUT = "sampleInput";
    public static final String SAMPLE_OUTPUT = "sampleOutput";
    public static final String HINT = "hint";
    public static final String SOURCE = "source";
    public static final String SAMPLE_PROGRAM = "sampleProgram";
    public static final String TIME_LIMIT = "timeLimit";
    public static final String MEMORY_LIMIT = "memoryLimit";
    public static final String ATIME = "atime";
    public static final String CTIME = "ctime";
    public static final String MTIME = "mtime";
    public static final String STIME = "stime";
    public static final String ACCEPTED = "accepted";
    public static final String SOLVED = "solved";
    public static final String SUBMISSION = "submission";
    public static final String SUBMIT_USER = "submitUser";
    public static final String ERROR = "error";
    public static final String RATIO = "ratio";
    public static final String DIFFICULTY = "difficulty";
    public static final String VIEW = "view";
    public static final String STATUS = "status";
    /**
     *
     */
    private static final long serialVersionUID = 1943890587086216047L;

    public ProblemModel() {
    }

    public ProblemModel(ProblemModel original) {
        for (Map.Entry<String, Object> entry : original.getAttrs().entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public ProblemModel merge(ProblemModel newProblemModel) {
        for (Map.Entry<String, Object> entry : newProblemModel.getAttrs().entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Integer getPid() {
        return getInt(PID);
    }

    public ProblemModel setPid(Integer value) {
        return set(PID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public ProblemModel setUid(Integer value) {
        return set(UID, value);
    }

    public String getTitle() {
        return getStr(TITLE);
    }

    public ProblemModel setTitle(String value) {
        return set(TITLE, value);
    }

    public String getDescription() {
        return getStr(DESCRIPTION);
    }

    public ProblemModel setDescription(String value) {
        return set(DESCRIPTION, value);
    }

    public String getInput() {
        return getStr(INPUT);
    }

    public ProblemModel setInput(String value) {
        return set(INPUT, value);
    }

    public String getOutput() {
        return getStr(OUTPUT);
    }

    public ProblemModel setOutput(String value) {
        return set(OUTPUT, value);
    }

    public String getSampleInput() {
        return getStr(SAMPLE_INPUT);
    }

    public ProblemModel setSampleInput(String value) {
        return set(SAMPLE_INPUT, value);
    }

    public String getSampleOutput() {
        return getStr(SAMPLE_OUTPUT);
    }

    public ProblemModel setSampleOutput(String value) {
        return set(SAMPLE_OUTPUT, value);
    }

    public String getHint() {
        return getStr(HINT);
    }

    public ProblemModel setHint(String value) {
        return set(HINT, value);
    }

    public String getSource() {
        return getStr(SOURCE);
    }

    public ProblemModel setSource(String value) {
        return set(SOURCE, value);
    }

    public String getSampleProgram() {
        return getStr(SAMPLE_PROGRAM);
    }

    public ProblemModel setSampleProgram(String value) {
        return set(SAMPLE_PROGRAM, value);
    }

    public Integer getTimeLimit() {
        return getInt(TIME_LIMIT);
    }

    public ProblemModel setTimeLimit(Integer value) {
        return set(TIME_LIMIT, value);
    }

    public Integer getMemoryLimit() {
        return getInt(MEMORY_LIMIT);
    }

    public ProblemModel setMemoryLimit(Integer value) {
        return set(MEMORY_LIMIT, value);
    }

    public Integer getAtime() {
        return getInt(ATIME);
    }

    public ProblemModel setAtime(Integer value) {
        return set(ATIME, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public ProblemModel setCtime(Integer value) {
        return set(CTIME, value);
    }

    public Integer getMtime() {
        return getInt(MTIME);
    }

    public ProblemModel setMtime(Integer value) {
        return set(MTIME, value);
    }

    public Integer getStime() {
        return getInt(STIME);
    }

    public ProblemModel setStime(Integer value) {
        return set(STIME, value);
    }

    public Integer getAccepted() {
        return getInt(ACCEPTED);
    }

    public ProblemModel setAccepted(Integer value) {
        return set(ACCEPTED, value);
    }

    public Integer getSolved() {
        return getInt(SOLVED);
    }

    public ProblemModel setSolved(Integer value) {
        return set(SOLVED, value);
    }

    public Integer getSubmission() {
        return getInt(SUBMISSION);
    }

    public ProblemModel setSubmission(Integer value) {
        return set(SUBMISSION, value);
    }

    public Integer getSubmitUser() {
        return getInt(SUBMIT_USER);
    }

    public ProblemModel setSubmitUser(Integer value) {
        return set(SUBMIT_USER, value);
    }

    public Integer getError() {
        return getInt(ERROR);
    }

    public ProblemModel setError(Integer value) {
        return set(ERROR, value);
    }

    public Integer getRatio() {
        return getInt(RATIO);
    }

    public ProblemModel setRatio(Integer value) {
        return set(RATIO, value);
    }

    public Integer getDifficulty() {
        return getInt(DIFFICULTY);
    }

    public ProblemModel setDifficulty(Integer value) {
        return set(DIFFICULTY, value);
    }

    public Integer getView() {
        return getInt(VIEW);
    }

    public ProblemModel setView(Integer value) {
        return set(VIEW, value);
    }

    public Boolean getStatus() {
        return getBoolean(STATUS);
    }

    public ProblemModel setStatus(Boolean value) {
        return set(STATUS, value);
    }

}
