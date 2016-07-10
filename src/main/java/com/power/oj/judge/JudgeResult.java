package com.power.oj.judge;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JudgeResult {
    private int result;
    private int sid;
    private int cid;
    private int time = 0;
    private int memory = 0;
    private int test = 0;
    private String token;
    private String error = "";

    public JudgeResult() {
    }

    public JudgeResult(int sid, int cid, int result) {
        this.sid = sid;
        this.cid = cid;
        this.result = result;
    }

    public void readError(File file) {
        if (file != null) {
            try {
                error = FileUtils.readFileToString(file, "UTF-8");
                file.delete();
            } catch (IOException e) {

            }
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        JudgeResult that = (JudgeResult) o;

        if (result != that.result)
            return false;
        if (sid != that.sid)
            return false;
        if (cid != that.cid)
            return false;
        if (time != that.time)
            return false;
        if (memory != that.memory)
            return false;
        if (test != that.test)
            return false;
        if (token != null ? !token.equals(that.token) : that.token != null)
            return false;
        return error != null ? error.equals(that.error) : that.error == null;
    }

    @Override
    public int hashCode() {
        int result1 = result;
        result1 = 31 * result1 + sid;
        result1 = 31 * result1 + cid;
        result1 = 31 * result1 + time;
        result1 = 31 * result1 + memory;
        result1 = 31 * result1 + test;
        result1 = 31 * result1 + (token != null ? token.hashCode() : 0);
        result1 = 31 * result1 + (error != null ? error.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "JudgeResult{" +
            "result=" + result +
            ", sid=" + sid +
            ", cid=" + cid +
            ", time=" + time +
            ", memory=" + memory +
            ", test=" + test +
            ", token='" + token + '\'' +
            '}';
    }
}
