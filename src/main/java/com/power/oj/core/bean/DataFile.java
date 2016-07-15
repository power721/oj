package com.power.oj.core.bean;

import com.power.oj.core.OjConfig;

import java.io.File;

public class DataFile extends OJFile {

    private int pid;

    public DataFile(Integer pid, String name) {
        super(name);
        this.pid = pid;

        String path = OjConfig.getString("dataPath") + File.separator + pid + File.separator + name;
        file = new File(path);
    }

    public DataFile(Integer pid, File file) {
        super(file);
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

}
