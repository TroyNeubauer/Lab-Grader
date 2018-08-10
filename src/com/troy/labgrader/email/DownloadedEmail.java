package com.troy.labgrader.email;

import java.io.File;
import java.util.*;

public class DownloadedEmail {
	public static final String BODY_NAME = "body.txt", INFO_NAME = "info.txt", ATTACHMENTS_FOLDER = "attachments";

	private File file;

	public DownloadedEmail(File file) {
		this.file = file;
	}

	public static boolean isDownloadedEmail(File file) {
		File attachments = new File(file, ATTACHMENTS_FOLDER + File.separatorChar);
		if (!attachments.exists() || attachments.isFile()) {
			return false;
		}

		File body = new File(file, BODY_NAME);
		if (!body.exists() || body.isDirectory()) {
			return false;
		}

		File info = new File(file, INFO_NAME);
		if (!info.exists() || info.isDirectory()) {
			return false;
		}

		return true;
	}

	public static DownloadedEmail get(File file) {
		if (!isDownloadedEmail(file))
			throw new IllegalArgumentException("Invalid downloaded email file: " + file);

		return new DownloadedEmail(file);
	}

	public static List<DownloadedEmail> getAll(File file) {
		return getAll(file, new ArrayList<DownloadedEmail>());
	}

	private static List<DownloadedEmail> getAll(File file, List<DownloadedEmail> list) {
		if (file.isDirectory()) {
			if (isDownloadedEmail(file)) {
				list.add(new DownloadedEmail(file));
			} else {
				for (File child : file.listFiles()) {
					getAll(child, list);
				}
			}
		}
		return list;
	}

}
