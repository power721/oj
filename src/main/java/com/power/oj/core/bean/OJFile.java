package com.power.oj.core.bean;

import jodd.io.FileNameUtil;
import jodd.util.StringUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * file info for problem data files.
 * current implementation is for all files.
 *
 * @author power
 */
public class OJFile {
    private static String[] exts = {"in", "out", "txt", "c", "cc", "cpp", "pas", "java", "py"};
    protected String name;
    protected String user;
    protected String group;
    protected String perm;
    protected long createTime;
    protected File file;

    public OJFile(String name) {
        this.name = name;
    }

    public OJFile(File file) {
        this.file = file;

        if (file.exists()) {
            name = file.getName();
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public String readString() throws IOException {
        String ext = FileNameUtil.getExtension(name);
        if (StringUtil.equalsOne(ext, exts) == -1) {
            return null;
        }
        return FileUtils.readFileToString(file);
    }

    public void writeString(String data) throws IOException {
        FileUtils.writeStringToFile(file, data);
    }

    public void touch() throws IOException {
        FileUtils.touch(file);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public long getSize() {
        return file.length();
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getModifyTime() {
        return file.lastModified();
    }

    public File getFile() {
        return file;
    }

}
