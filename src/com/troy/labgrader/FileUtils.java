package com.troy.labgrader;

import java.io.*;
import java.util.*;

import javax.mail.MessagingException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.*;
import com.troy.labgrader.email.*;
import com.troy.labgrader.lab.Year;

public class FileUtils {

	public static final String EXTENSION = "zip", FILES_DIRECTORY = "files", APPDATA_FOLDER_NAME = "Troy's Lab Grader";
	public static final File APPDATA_STORAGE_FOLDER = new File(System.getenv("APPDATA"), APPDATA_FOLDER_NAME);
	public static final Kryo kryo = new Kryo();

	static {
		// TConfig.get().setArchiveDetector(new TArchiveDetector(EXTENSION, new ZipDriver(IOPoolLocator.SINGLETON)));
	}

	public static String removeBannedCharacters(String part) {
		switch (OSType.get()) {
		case Windows:// Not allowed characters = /\:*?"<>|
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

	public static String toEmailDirectory(String email) {
		return FileUtils.removeBannedCharacters(email);
	}

	public static DownloadedEmail saveEmail(Email email) {
		try {
			String from = EmailUtils.getEmail(email.getMessage().getFrom()[0]);
			UUID uuid = UUID.randomUUID();
			File file = new File(APPDATA_STORAGE_FOLDER, toEmailDirectory(from) + File.separatorChar + uuid.toString().replaceAll("-", ""));

			email.downloadAttachments(new File(file, DownloadedEmail.ATTACHMENTS_FOLDER));

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

			writeString(file, DownloadedEmail.INFO_NAME, info.toString());
			writeString(file, DownloadedEmail.BODY_NAME, email.getText());
			return new DownloadedEmail(file);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	private static void writeString(File parent, String fileName, String string) {
		try {
			FileWriter writer = new FileWriter(new File(parent, fileName));
			writer.write(string);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void write(File file, Object data) {
		try {
			FileOutputStream stream = new FileOutputStream(file);
			write(stream, data);
			stream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static void write(OutputStream stream, Object data) {
		Output out = new Output(stream);
		kryo.writeObject(out, data);
		out.flush();
	}

	public static <T> T read(File file, Class<T> type) {
		try {
			Input in = new Input(new FileInputStream(file));
			T obj = kryo.readObject(in, type);
			in.close();
			return obj;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * public static void write(ZipOutputStream stream, String inside, byte[] data) throws IOException { ZipEntry e = new ZipEntry(inside);
	 * stream.putNextEntry(e);
	 * 
	 * stream.write(data, 0, data.length); stream.closeEntry(); }
	 */

	public static <T> void save(File file, T obj, Class<T> type) {
		assert obj.getClass() == type.getClass();
		try {
			Output out = new Output(new FileOutputStream(file));

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * public static void delete(ZipFile file, String[] entryNames) { try { File tempFile = new File(file.getName() + "-" +
	 * UUID.randomUUID().toString().replaceAll("-", "") + ".temp"); ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(tempFile));
	 * Enumeration<? extends ZipEntry> e = file.entries(); while (e.hasMoreElements()) { ZipEntry entry = e.nextElement(); boolean copy = true; for
	 * (String compareName : entryNames) { if (entry.getName().equals(compareName)) { copy = false; break; } } if (copy) {
	 * stream.putNextEntry(entry); IOUtils.copy(file.getInputStream(entry), stream); stream.closeEntry(); } } stream.close(); } catch (IOException
	 * e1) { e1.printStackTrace(); } }
	 */
	/*
	 * public static <T> T read(ZipFile file, String entryName, Class<T> type) { ZipEntry entry = file.getEntry(entryName); if (entry == null) throw
	 * new IllegalArgumentException("Could not find entry: " + entryName); try { Input in = new Input(file.getInputStream(entry)); return
	 * kryo.readObject(in, type); } catch (IOException e) { throw new RuntimeException(e); }
	 * 
	 * }
	 */
}
