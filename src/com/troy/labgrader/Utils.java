package com.troy.labgrader;

import java.awt.Component;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.troy.labgrader.email.Student;

public class Utils {

	public static final long INVALID_STRING = Long.MIN_VALUE;

	public static StudentList getStudentsListFromExcelFile(File file) {
		List<Student> students = new ArrayList<Student>();
		FileInputStream stream;
		XSSFWorkbook workbook = null;
		try {
			stream = new FileInputStream(file);
			workbook = new XSSFWorkbook(stream);
			Sheet sheet = workbook.getSheetAt(0);
			try {
				Row row1 = sheet.getRow(0);
				int nameIndex = findCol(row1, "name");
				int emailIndex = findCol(row1, "email");
				int periodIndex = findCol(row1, "period");
				if (nameIndex == -1 || emailIndex == -1 || periodIndex == -1)
					throw new RuntimeException("Missing header!");
				Iterator<Row> rows = sheet.iterator();
				while (rows.hasNext()) {
					Row row = rows.next();
					if (row == null)
						break;
					Cell nameCell = row.getCell(nameIndex);
					Cell emailCell = row.getCell(emailIndex);
					Cell periodCell = row.getCell(periodIndex);
					if (nameCell == null || emailCell == null || periodCell == null)
						continue;

					if (nameCell.getCellTypeEnum() != CellType.STRING || emailCell.getCellTypeEnum() != CellType.STRING || periodCell.getCellTypeEnum() != CellType.NUMERIC)
						continue;
					int period = (int) periodCell.getNumericCellValue();
					String name = nameCell.getStringCellValue();
					String email = emailCell.getStringCellValue();
					students.add(new Student(period, name, email));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new StudentList(students);
	}

	public static int findCol(Row row, String target) {
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i, MissingCellPolicy.RETURN_BLANK_AS_NULL);
			if (cell != null) {
				try {
					String s = cell.getStringCellValue();
					if (s.equalsIgnoreCase(target)) {
						return cell.getColumnIndex();
					}
				} catch (Exception e) {

				}
			}
		}
		return -1;
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
