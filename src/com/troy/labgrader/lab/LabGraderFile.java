package com.troy.labgrader.lab;

import java.io.*;
import java.nio.channels.*;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.zip.*;

import com.troy.labgrader.*;

public class LabGraderFile implements AutoCloseable {

	private ArrayList<Year> years;

	private FileLock lock;
	private File file;

	public static final List<LabGraderFile> LIST = new ArrayList<LabGraderFile>();
	static {
		Runtime.getRuntime().removeShutdownHook(new Thread(() -> {
			for (LabGraderFile file : LIST) {
				file.closeImpl(false);
			}
		}));
	}

	public LabGraderFile(File file) {
		LIST.add(this);
		this.file = file;
		try {
			lock = FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE).lock();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<Year> getYears() {
		if (years == null) {
			years = FileUtils.read(file, ArrayList.class);
		}
		return years;
	}

	public File getFile() {
		return file;
	}

	public void save() {
		FileUtils.write(file, years);
	}

	public void close() {
		closeImpl(true);
	}

	private void closeImpl(boolean remove) {
		if (remove)
			LIST.remove(this);
		try {
			save();
		} catch (RuntimeException e) {
			throw e;
		} finally {// Make sure to clean up everything even if saving fails
			try {
				lock.release();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "LabGraderFile [file=" + file + "]";
	}

}
