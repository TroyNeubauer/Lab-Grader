package com.troy.labgrader;

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

public class Utils {

	public static final long INVALID_STRING = Long.MIN_VALUE;

	public static void showError(Throwable t) {
		JOptionPane.showMessageDialog(null, MiscUtil.getStackTrace(t), "Error!", JOptionPane.ERROR_MESSAGE);
	}

	public static long getByte(String string) {
		try {
			return Byte.parseByte(string);
		} catch (NumberFormatException e) {
			return INVALID_STRING;
		}
	}

	public static long getShort(String string) {
		try {
			return Short.parseShort(string);
		} catch (NumberFormatException e) {
			return INVALID_STRING;
		}
	}

	/**
	 * Returns the integer representation of the number or {@link Long#MIN_VALUE} if the string does not represent a valid int
	 * 
	 * @param string The string to parse
	 * @return The string in an int form
	 */
	public static long getInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return INVALID_STRING;
		}
	}

	public static long getLong(String string) {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException e) {
			return INVALID_STRING;
		}
	}

	public static double getFloat(String string) {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException e) {
			return Double.MIN_VALUE;
		}
	}

	public static double getDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return Double.MIN_VALUE;
		}
	}

	public static String getUserString(Component master, String message, String title, int messageType) {
		return (String) JOptionPane.showInputDialog(master, message, title, messageType, null, null, null);
	}

	public static File setExtention(File file, String extension) {
		if (FilenameUtils.getExtension(file.getAbsolutePath()).equals(extension))
			return file;
		else
			return new File(file.getPath() + '.' + extension);
	}
}
