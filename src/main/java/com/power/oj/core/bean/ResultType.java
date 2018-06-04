package com.power.oj.core.bean;

import jodd.format.Printf;

public class ResultType {
    public static final int AC = 0;
    public static final int PE = 1;
    public static final int TLE = 2;
    public static final int MLE = 3;
    public static final int WA = 4;
    public static final int RE = 5;
    public static final int OLE = 6;
    public static final int CE = 7;
    public static final int RF = 8;
    public static final int SE = 9;
    public static final int VE = 10;
    public static final int WAIT = 11;
    public static final int RUN = 12;
    public static final int REJUDGE = 13;
    public static final int SIM = 14;
    public static final int NOT_AC = 999;

    private int id;
    private String name;
    private String longName;

    public ResultType() {

    }

    public ResultType(int id, String name, String longName) {
        this.id = id;
        this.name = name;
        this.longName = longName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String toJson() {
        return Printf.str("{id:\"%d\", name:\"%s\", longName:\"%s\"}", id, name, longName);
    }

    @Override
    public String toString() {
        return "ResultType{" + "id=" + id + ", name='" + name + '\'' + ", longName='" + longName + '\'' + '}';
    }
}
