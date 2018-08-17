package com.troy.labgrader;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StudentList {

	// Maps period numbers to a mapping between student name and student email
	private HashMap<Integer, HashMap<String, String>> students;

	public StudentList() {
		students = new HashMap<Integer, HashMap<String, String>>();
	}

	public StudentList(HashMap<Integer, HashMap<String, String>> students) {
		this.students = students;
	}

	public static StudentList fromMap(HashMap<Integer, HashMap<String, String>> map) {
		return new StudentList(map);
	}

	public static StudentList fromExcelFile(File file) {
		HashMap<Integer, HashMap<String, String>> map = new HashMap<Integer, HashMap<String, String>>();
		FileInputStream stream;
		XSSFWorkbook workbook = null;
		try {
			stream = new FileInputStream(file);
			workbook = new XSSFWorkbook(stream);
			Sheet sheet = workbook.getSheetAt(0);
			try {
				Row row1 = sheet.getRow(0);
				System.out.println(row1.getCell(0).getStringCellValue());
				if (!row1.getCell(0).getStringCellValue().equals("period") || !row1.getCell(1).getStringCellValue().equals("name") || !row1.getCell(2).getStringCellValue().equals("email"))
					throw new RuntimeException("Missing header!");
				Iterator<Row> rows = sheet.iterator();
				while (rows.hasNext()) {
					Row row = rows.next();
					if (row.getLastCellNum() != 3)
						continue;
					if (row.getCell(0).getCellTypeEnum() != CellType.NUMERIC || row.getCell(1).getCellTypeEnum() != CellType.STRING || row.getCell(2).getCellTypeEnum() != CellType.STRING)
						continue;
					int period = (int) row.getCell(0).getNumericCellValue();
					String name = row.getCell(1).getStringCellValue();
					String email = row.getCell(2).getStringCellValue();

					HashMap<String, String> periodMap = map.get(period);
					if (periodMap == null) {
						periodMap = new HashMap<String, String>();
						map.put(period, periodMap);
					}
					periodMap.put(name, email);
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

		return new StudentList(map);
	}

	public void addStudent(int periodNumber, String name, String email) {
		HashMap<String, String> period = students.get(periodNumber);
		if (period == null) {
			period = new HashMap<String, String>();
			students.put(periodNumber, period);
		}
		period.put(name, email);
	}

	public int getPeriodWithName(String name) {
		for (Entry<Integer, HashMap<String, String>> entry : students.entrySet()) {
			HashMap<String, String> period = entry.getValue();
			if (period.containsKey(name))
				return entry.getKey();
		}
		return -1;
	}

	public int getPeriodWithEmail(String email) {
		for (Entry<Integer, HashMap<String, String>> entry : students.entrySet()) {
			HashMap<String, String> period = entry.getValue();
			if (period.containsValue(email))
				return entry.getKey();
		}
		return -1;
	}

	public HashMap<Integer, HashMap<String, String>> getStudents() {
		return students;
	}
}
