package com.power.oj.util;

import com.google.common.io.Files;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import jodd.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Some utils for file handling.
 *
 * @author power
 */
public class FileKit {

    private static final Logger LOGGER = Logger.getLogger(FileKit.class);
    private static String[] allowFiles =
        {".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp"};

    private static String[] imageFileType = {".gif", ".png", ".jpg", ".jpeg", ".bmp", ".tif", ".svg", ".gif"};

    /**
     * Get the files in a directory recursively.
     *
     * @param realPath the path of the directory
     * @param files    the list contains the files in realPath
     * @return the list of files
     */
    public static List<File> getFiles(String realPath, List<File> files) {
        File realFile = new File(realPath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            if (subfiles != null) {
                for (File file : subfiles) {
                    if (file.isDirectory()) {
                        getFiles(file.getAbsolutePath(), files);
                    } else {
                        if (!getFileType(file.getName()).equals("")) {
                            files.add(file);
                        }
                    }
                }
            }
        }
        return files;
    }

    /**
     * Get the file suffix from file name.
     *
     * @param fileName file name with suffix
     * @return the file suffix with '.'
     */
    public static String getFileType(String fileName) {
        String[] fileType =
            {".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp"};
        for (String t : fileType) {
            if (fileName.toLowerCase().endsWith(t)) {
                return t;
            }
        }
        return "";
    }

    /**
     * Get the image files in a directory recursively.
     *
     * @param realpath the path of the directory
     * @param files    the list contains the image files in realPath
     * @return the list of image files
     */
    public static List<File> getImageFiles(String realpath, List<File> files) {

        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            if (subfiles != null) {
                for (File file : subfiles) {
                    if (file.isDirectory()) {
                        getImageFiles(file.getAbsolutePath(), files);
                    } else {
                        if (!getImageType(file.getName()).equals("")) {
                            files.add(file);
                        }
                    }
                }
            }
        }
        return files;
    }

    /**
     * Get the image file suffix from file name.
     *
     * @param fileName file name with suffix
     * @return the image file suffix with '.'
     */
    public static String getImageType(String fileName) {
        for (String t : Arrays.asList(imageFileType)) {
            if (fileName.endsWith(t)) {
                return t;
            }
        }
        return "";
    }

    /**
     * Get the suffix from file name.
     *
     * @return string of file suffix
     */
    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(""));
    }

    /**
     * Generate new file name base on original one.
     *
     * @return the new file name
     */
    public static String getNewName(String fileName) {
        Random random = new Random();
        return "" + random.nextInt(10000) + System.currentTimeMillis() + getFileExt(fileName);
    }

    public static String[] getImageFileType() {
        return imageFileType;
    }

    public static void setImageFileType(String[] imageFileType) {
        FileKit.imageFileType = imageFileType;
    }

    public static String[] getAllowFiles() {
        return allowFiles;
    }

    public static String parsePath(String path) {
        return parsePath(path, false);
    }

    public static String parsePath(String path, boolean mkdirs) {
        path = path.trim();

        if (!path.endsWith("/"))
            path = path + "/";

        if (!path.startsWith("/"))
            path = PathKit.getWebRootPath() + "/" + path;

        File file = new File(path);
        path = file.getAbsolutePath();

        if (mkdirs && !file.exists()) {
            try {
                FileUtil.mkdirs(file);
            } catch (IOException e) {
                LOGGER.error("mkdirs failed", e);
            }
        }
        return path;
    }

    /**
     * Check the file type in allowed types.
     *
     * @param fileName file name with suffix
     * @return if file type is allowed
     */
    public boolean checkFileType(String fileName) {
        for (String ext : getAllowFiles()) {
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public static void moveFile(File src, File dest) throws IOException {
        // String srcName = URLEncoder.encode(src.getName(),"UTF-8");
        // String destName = URLEncoder.encode(dest.getName(),"UTF-8");
        //
        // File from = new File(src.getParentFile(), srcName);
        // File to = new File(dest.getParentFile(), destName);
        Files.move(src, dest);
    }
}
