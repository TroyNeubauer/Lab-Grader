package com.troy.labgrader;

import java.io.*;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;

import javax.mail.MessagingException;

import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;

import com.esotericsoftware.kryo.Kryo;
import com.troy.labgrader.email.*;

import de.schlichtherle.truezip.file.*;
import de.schlichtherle.truezip.fs.FsSyncException;
import de.schlichtherle.truezip.fs.archive.zip.ZipDriver;
import de.schlichtherle.truezip.socket.sl.IOPoolLocator;

public class FileUtils {

	public static final String EXTENSION = "troygrade";
	public static final Kryo kryo = new Kryo();

	static {
		TConfig.get().setArchiveDetector(new TArchiveDetector(EXTENSION, new ZipDriver(IOPoolLocator.SINGLETON)));
	}

	public static String removeBannedCharacters(String part) {
		switch (OSType.get()) {
		case Windows://Not allowed characters = /\:*?"<>|
			part = part.replace('/', '.');
			part = part.replace('\\', '.');
			part = part.replace(':', '.');
			part = part.replace('*', '.');
			part = part.replace('?', '.');
			part = part.replace('\"', '.');
			part = part.replace('<', '.');
			part = part.replace('>', '.');
			part = part.replace('|', '.');
			break;
		case Mac:
			part = part.replace('/', '.');
			part = part.replace('\\', '.');
			part = part.replace(':', '.');
			break;
		case Linux:
			part = part.replace('/', '.');
			part = part.replace('\\', '.');
			part = part.replace(':', '.');
			break;
		case Other:
			break;
		}
		return part;
	}

	public static void saveEmail(File saveFile, Email email) {
		try {
			String from = EmailUtils.getEmail(email.getMessage().getFrom()[0]);
			int i = 1;
			TFile file;
			do {
				file = new TFile(saveFile, "files" + File.separatorChar + FileUtils.removeBannedCharacters(from) + File.separatorChar + Integer.toString(i));
				i++;
			} while (file.exists());
			email.downloadAttachments(new TFile(file, "attachments"));

			StringBuilder info = new StringBuilder();
			info.append("from:");
			info.append(Arrays.toString(email.getMessage().getFrom()));
			info.append('\n');
			Date date = email.getMessage().getSentDate();
			if (date == null)
				date = email.getMessage().getReceivedDate();
			if (date != null) {
				info.append("date:");
				info.append(date.getTime() / 1000);
				info.append('\n');
			}
			info.append("subject:");
			info.append(email.getMessage().getSubject());
			info.append('\n');

			writeString(new TFile(file, "info.txt"), info.toString());
			writeString(new TFile(file, "body.txt"), email.getText());
			TVFS.umount();// Save the archive
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (FsSyncException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void writeString(TFile file, String string) throws IOException {
		file.createNewFile();
		TFileOutputStream stream = new TFileOutputStream(file);
		stream.write(string.getBytes());
		stream.close();
	}
}
