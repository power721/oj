package com.power.oj.common;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FileKit
{

	private String[] allowFiles = { ".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".gif", ".png",
			".jpg", ".jpeg", ".bmp" };

	public static List<File> getFiles(String realpath, List<File> files)
	{

		File realFile = new File(realpath);
		if (realFile.isDirectory())
		{
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles)
			{
				if (file.isDirectory())
				{
					getFiles(file.getAbsolutePath(), files);
				} else
				{
					if (!getFileType(file.getName()).equals(""))
					{
						files.add(file);
					}
				}
			}
		}
		return files;
	}

	public static String getFileType(String fileName)
	{
		String[] fileType = { ".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg",
				".jpeg", ".bmp" };
		Iterator<String> type = Arrays.asList(fileType).iterator();
		while (type.hasNext())
		{
			String t = type.next();
			if (fileName.endsWith(t))
			{
				return t;
			}
		}
		return "";
	}

	public static List<File> getImageFiles(String realpath, List<File> files)
	{

		File realFile = new File(realpath);
		if (realFile.isDirectory())
		{
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles)
			{
				if (file.isDirectory())
				{
					getImageFiles(file.getAbsolutePath(), files);
				} else
				{
					if (!getImageType(file.getName()).equals(""))
					{
						files.add(file);
					}
				}
			}
		}
		return files;
	}

	public static String getImageType(String fileName)
	{
		String[] fileType = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
		Iterator<String> type = Arrays.asList(fileType).iterator();
		while (type.hasNext())
		{
			String t = type.next();
			if (fileName.endsWith(t))
			{
				return t;
			}
		}
		return "";
	}

	/**
	 * 文件类型判断
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean checkFileType(String fileName)
	{
		Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext())
		{
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	public static String getFileExt(String fileName)
	{
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public void setAllowFiles(String[] allowFiles)
	{
		this.allowFiles = allowFiles;
	}

	/**
	 * 依据原始文件名生成新文件名
	 * 
	 * @return
	 */
	public static String getNewName(String fileName)
	{
		Random random = new Random();
		return "" + random.nextInt(10000) + System.currentTimeMillis() + getFileExt(fileName);
	}

}
