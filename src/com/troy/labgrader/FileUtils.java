package com.troy.labgrader;

import java.io.*;

import javax.mail.*;
import javax.swing.filechooser.FileFilter;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.*;
import com.troy.labgrader.email.*;

public class FileUtils {

	public static final String EXTENSION = "troygrade", FILES_DIRECTORY = "files", APPDATA_FOLDER_NAME = "Troy's Lab Grader";
	public static final File APPDATA_STORAGE_FOLDER = new File(System.getenv("APPDATA"), APPDATA_FOLDER_NAME), NUMBER_OF_EMAILS_FILE = new File(APPDATA_STORAGE_FOLDER, "email count.dat");
	public static final Kryo kryo = new Kryo();
	public static final String EXCEL_EXTENSION = "xlsx";
	private static final Object EMAIL_COUNT_LOCK = new Object();

	static {
		kryo.setInstantiatorStrategy(new InstantiatorStrategy() {
			@Override
			public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
				if (MiscUtil.isClassDefualtJavaClass(type)) {
					return new Kryo.DefaultInstantiatorStrategy().newInstantiatorOf(type);
				} else {
					return new ObjectInstantiator<T>() {
						@Override
						public T newInstance() {
							return MiscUtil.newInstanceUsingAnyMeans(type);

						}
					};
				}
			}
		});
	}

	public static void deleteAllEmails() {
		synchronized (EMAIL_COUNT_LOCK) {
			for (File file : APPDATA_STORAGE_FOLDER.listFiles()) {
				if (file.isDirectory()) {
					try {
						org.apache.commons.io.FileUtils.deleteDirectory(file);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
			setNumberOfEmails(0);
		}
	}

	public static long getNumebrOfEmails() {
		DataInputStream stream = null;
		try {
			stream = new DataInputStream(new FileInputStream(NUMBER_OF_EMAILS_FILE));
			return stream.readLong();
		} catch (Exception e) {
			setNumberOfEmails(0);
			return 0;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static void setNumberOfEmails(long count) {
		DataOutputStream stream = null;
		try {
			NUMBER_OF_EMAILS_FILE.getParentFile().mkdirs();
			stream = new DataOutputStream(new FileOutputStream(NUMBER_OF_EMAILS_FILE));
			stream.writeLong(count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static long getAndIncrementNumberOfEmails() {
		synchronized (EMAIL_COUNT_LOCK) {
			long result = getNumebrOfEmails();
			setNumberOfEmails(result + 1);
			return result;
		}
	}

	public static class MyFileFilter extends FileFilter {
		@Override
		public String getDescription() {
			return EXTENSION;
		}

		@Override
		public boolean accept(File f) {
			if (f == null)
				return false;
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

	public static String removeBannedCharacters(String part) {
		switch (OSType.get()) {
		case Windows:// Not allowed characters = /\:*?"<>| format:off
			part = part.replace('/',  '.');
			part = part.replace('\\', '.');
			part = part.replace(':',  '.');
			part = part.replace('*',  '.');
			part = part.replace('?',  '.');
			part = part.replace('\"', '.');
			part = part.replace('<',  '.');
			part = part.replace('>',  '.');
			part = part.replace('|',  '.');
			break;
		case Mac:
			part = part.replace('/',  '.');
			part = part.replace('\\', '.');
			part = part.replace(':',  '.');
			break;
		case Linux:
			part = part.replace('/',  '.');
			part = part.replace('\\', '.');
			part = part.replace(':',  '.');
			break;
		case Other://format:on
			break;
		}
		return part;
	}

	public static String toEmailDirectory(String email) {
		return FileUtils.removeBannedCharacters(email);
	}

	public static DownloadedEmail saveEmail(Email email) {
		try {
			File file = new File(APPDATA_STORAGE_FOLDER, Long.toString(email.getId(), Character.MAX_RADIX));
			File[] attachments = email.downloadAttachments(new File(file, DownloadedEmail.ATTACHMENTS_FOLDER_NAME));

			Message message = email.getMessage();

			String[] from = new String[email.getMessage().getFrom().length];
			for (int i = 0; i < email.getMessage().getFrom().length; i++) {
				from[i] = email.getMessage().getFrom()[i].toString();
			}
			String subject = message.getSubject();
			long messageTime = email.getMessage().getReceivedDate().getTime() / 1000;

			return DownloadedEmail.save(new File(file, DownloadedEmail.INFO_FILE_NAME), from, messageTime, subject, attachments);
		} catch (MessagingException e) {
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
		kryo.writeClassAndObject(out, data);
		out.flush();
	}

	public static <T> T read(File file, Class<T> type) {
		try {
			Input in = new Input(new FileInputStream(file));
			Object obj = kryo.readClassAndObject(in);
			assert type.isAssignableFrom(type);
			in.close();
			return (T) obj;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
