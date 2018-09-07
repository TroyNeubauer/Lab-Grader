package com.troy.labgrader;

import java.awt.Component;
import java.io.*;
import java.util.List;

import javax.swing.JOptionPane;

import com.troy.labgrader.email.Student;

public class Utils {

	public static final long INVALID_STRING = Long.MIN_VALUE;
	
	public static void exportSdutentListToExcel(StudentList list, File file) {
		XSSFWorkbook workbook = null;
		FileInputStream stream;
		try {
			stream = new FileInputStream(file);
			workbook = new XSSFWorkbook(stream);
			Sheet sheet = workbook.getSheetAt(0);
			List<Student> students = list.getStudents();
			for(int i = 0; i < students.size() + 1; i++) {//One for each student + one header row
				sheet.createRow(i);
			}
		} catch (Exception e) {
			showError(e);
		}
	}

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
}
