package com.power.oj.core.bean;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.util.FileKit;
import jodd.io.FileNameUtil;
import jodd.util.StringUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;

public class OJFile {

    public static String[] exts = {"in", "out", "txt", "c", "cc", "cpp", "pas", "java", "py"};
    private static final Logger LOGGER = Logger.getLogger(OJFile.class);

    private String name;
    private String user;
    private String group;
    private String perm;
    private String type;
    private String dir;
    private File file;

    public OJFile(String dir, String name) {
        file = new File(dir, name);
        this.name = file.getName();

        init();
    }

    public OJFile(String path) {
        file = new File(path);
        this.name = file.getName();

        init();
    }

    public OJFile(File file) {
        this.file = file;
        name = file.getName();

        init();
    }

    private void init() {
        if (file.exists()) {
            try {
                if (OjConfig.isLinux()) {
                    PosixFileAttributes attr =
                            Files.readAttributes(file.toPath(), PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                    user = attr.owner().getName();
                    group = attr.group().getName();
                    perm = PosixFilePermissions.toString(attr.permissions());
                    if (attr.isSymbolicLink()) {
                        type = "Link";
                    } else if (attr.isDirectory()) {
                        type = "Directory";
                    } else if (attr.isRegularFile()) {
                        type = "File";
                    } else {
                        type = "Other";
                    }
                } else {
                    if (file.isDirectory())
                        type = "Link";
                    else if (file.isFile()) {
                        type = "File";
                    } else {
                        type = "Other";
                    }
                    user = "";
                    group = "";
                    perm = "rwxrwxrwx";

                }
            } catch (IOException e) {
                LOGGER.warn("get file attributes failed!", e);
            }
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public String readString() throws IOException {
        String ext = FileNameUtil.getExtension(name).toLowerCase();
        if (StringUtil.equalsOne(ext, exts) == -1) {
            return null;
        }
        return FileUtils.readFileToString(file);
    }

    public String getContent() throws IOException {
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

    public long getModifyTime() {
        return file.lastModified();
    }

    public File getFile() {
        return file;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public boolean isFile() {
        return file.isFile();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isEditable() {
        String ext = FileNameUtil.getExtension(name).toLowerCase();
        return StringUtil.equalsOne(ext, exts) != -1;
    }

    public boolean isImage() {
        String ext = FileNameUtil.getExtension(name).toLowerCase();
        return StringUtil.equalsOne("." + ext, FileKit.getImageFileType()) != -1;
    }
}
