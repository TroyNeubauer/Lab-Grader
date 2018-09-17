package com.troy.labgrader.email;

import java.io.File;
import java.util.*;

import com.troy.labgrader.FileUtils;

public class DownloadedEmail {
	public static final String INFO_FILE_NAME = "info.dat", ATTACHMENTS_FOLDER_NAME = "attachments";

	private String[] from;
	private String subject;
	private long epochTime;
	private File[] attachments;

	private DownloadedEmail(String[] from, long epochTime, File[] attachments, String subject) {
		this.from = from;
		this.epochTime = epochTime;
		this.attachments = attachments;
		this.subject = subject;
	}

	public static DownloadedEmail save(File file, String[] from, long epochTime, String subject, File[] attachments) {
		DownloadedEmail email = new DownloadedEmail(from, epochTime, attachments, subject);
		FileUtils.write(file, email);
		return email;
	}

	public static void save(File file, DownloadedEmail email) {
		FileUtils.write(file, email);
	}

	public static DownloadedEmail open(File file) {
		return FileUtils.read(file, DownloadedEmail.class);
	}

	public static boolean isDownloadedEmail(File file) {
		File attachments = new File(file, ATTACHMENTS_FOLDER_NAME + File.separatorChar);
		if (!attachments.exists() || attachments.isFile()) {
			return false;
		}

		File info = new File(file, INFO_FILE_NAME);
		if (!info.exists() || info.isDirectory()) {
			return false;
		}

		return true;
	}

	public static DownloadedEmail get(File file) {
		if (!isDownloadedEmail(file))
			throw new IllegalArgumentException("Invalid downloaded email file: " + file);

		return DownloadedEmail.open(file);
	}

	public static List<DownloadedEmail> getAll(File file) {
		return getAll(file, new ArrayList<DownloadedEmail>());
	}

	private static List<DownloadedEmail> getAll(File file, List<DownloadedEmail> list) {
		if (file.isDirectory()) {
			if (isDownloadedEmail(file)) {
				list.add(DownloadedEmail.open(file));
			} else {
				for (File child : file.listFiles()) {
					getAll(child, list);
				}
			}
		}
		return list;
	}
	
	public static List<Long> getAllAsLongs(File file) {
		return getAllAsLongs(file, new ArrayList<Long>());
	}
	
	private static List<Long> getAllAsLongs(File file, List<Long> list) {
		if (file.isDirectory()) {
			if (isDownloadedEmail(file)) {
				list.add(Long.valueOf(file.getName(), Character.MAX_RADIX));
			} else {
				for (File child : file.listFiles()) {
					getAllAsLongs(child, list);
				}
			}
		}
		return list;
	}
}
