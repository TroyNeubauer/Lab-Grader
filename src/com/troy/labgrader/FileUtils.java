package com.troy.labgrader;

import java.io.*;
import java.util.*;

import javax.mail.MessagingException;
import javax.swing.filechooser.FileFilter;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.*;
import com.troy.labgrader.email.*;

public class FileUtils {

	public static final String EXTENSION = "troygrade", FILES_DIRECTORY = "files", APPDATA_FOLDER_NAME = "Troy's Lab Grader";
	public static final File APPDATA_STORAGE_FOLDER = new File(System.getenv("APPDATA"), APPDATA_FOLDER_NAME);
	public static final Kryo kryo = new Kryo();
	public static final String EXCEL_EXTENSION = "xlsx";

	public static class MyFileFilter extends FileFilter {

		@Override
		public String getDescription() {
			return EXTENSION;
		}

		@Override
		public boolean accept(File f) {
			if (f == null)
				return false;
			// System.out.println("is good file: " + MiscUtil.getExtension(f.getPath()).equals(FileUtils.EXTENSION) + " ext " +
			// MiscUtil.getExtension(f.getPath()));
			return f.isDirectory() || MiscUtil.getExtension(f.getPath()).equals(FileUtils.EXTENSION);
		}
	}
	
	public static class ExcelFileFilter extends FileFilter {

		@Override
		public String getDescription() {
			return EXCEL_EXTENSION;
		}

		@Override
		public boolean accept(File f) {
			if (f == null)
				return false;
			return f.isDirectory() || MiscUtil.getExtension(f.getPath()).equals(EXCEL_EXTENSION);
		}
	}

	static {
		kryo.setInstantiatorStrategy(new InstantiatorStrategy() {

			@Override
			public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
				if (MiscUtil.isClassDefualtJavaClass(type)) {
					System.out.println("using defualt instantitor: " + type);
					return new Kryo.DefaultInstantiatorStrategy().newInstantiatorOf(type);
				} else
					System.out.println("using my instantitor: " + type);
				return new ObjectInstantiator<T>() {

					@Override
					public T newInstance() {
						return MiscUtil.newInstanceUsingAnyMeans(type);

					}
				};
			}
		});
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
}
