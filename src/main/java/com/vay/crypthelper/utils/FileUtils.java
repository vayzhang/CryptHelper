package com.vay.crypthelper.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
	public static void main(String[] args) {
		String path = "D:\\myTemp\\TMP\\20170707\\source";

		List<String> names = getFileNameWithfilter(path, "pdf");
		for (int i = names.size() - 1; i >= 0; i--) {
			System.out.println(" == " + (String) names.get(i) + " == ");
		}

		List<String> dirs = getFileDirs(path);
		for (int i = dirs.size() - 1; i >= 0; i--)
			System.out.println(" @@ " + (String) dirs.get(i) + " @@ ");
	}

	public static File[] getFiles(String path) {
		File dirtory = new File(path);
		return dirtory.exists() ? dirtory.listFiles() : null;
	}

	public static void print(File[] files) {
		for (int i = files.length - 1; i >= 0; i--)
			System.out.println(" == " + files[i].getName() + " == ");
	}

	public static List<String> getFileNameWithfilter(String path, String pattern) {
		List<String> list = getFileList(path);
		List<String> rs = new ArrayList<String>();

		for (int i = list.size() - 1; i >= 0; i--) {
			String name = (String) list.get(i);
			if ((name != null) && (name.toLowerCase().endsWith(pattern.toLowerCase()))) {
				rs.add(name);
			}
		}
		return rs;
	}

	public static List<String> getFileList(String path) {
		List<String> fileNameList = new ArrayList<String>();

		int fileNum = 0;
		int folderNum = 0;
		File file = new File(path);
		if (file.exists()) {
			LinkedList<File> list = new LinkedList<File>();
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.isDirectory()) {
					list.add(file2);
					folderNum++;
				} else {
					fileNameList.add(file2.getAbsolutePath());
					fileNum++;
				}
			}

			while (!list.isEmpty()) {
				File temp_file = (File) list.removeFirst();
				files = temp_file.listFiles();
				for (File file2 : files) {
					if (file2.isDirectory()) {
						list.add(file2);
						folderNum++;
					} else {
						fileNameList.add(file2.getAbsolutePath());
						fileNum++;
					}

				}

			}

		}

		return fileNameList;
	}

	public static List<String> getFileDirs(String path) {
		List<String> dirsList = new ArrayList<String>();

		File file = new File(path);
		if ((file.exists() & file.isDirectory())) {
			LinkedList<File> list = new LinkedList<File>();

			File[] files = file.listFiles();

			for (File file2 : files) {
				if (file2.isDirectory()) {
					dirsList.add(file2.getAbsolutePath());
					list.add(file2);
				}

			}

			while (!list.isEmpty()) {
				File temp_file = (File) list.removeFirst();
				files = temp_file.listFiles();
				for (File file2 : files) {
					if (file2.isDirectory()) {
						list.add(file2);
						dirsList.add(file2.getAbsolutePath());
					}
				}
			}
		}

		return dirsList;
	}
}
