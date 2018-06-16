package com.troy.labgrader;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import com.esotericsoftware.kryo.io.*;

import de.schlichtherle.truezip.file.*;

public class StudentList {

	public static final String STUDENTS_FILE = "students.dat";

	// Maps period numbers to a mapping between student name and student email
	private HashMap<Integer, HashMap<String, String>> students;

	public StudentList(HashMap<Integer, HashMap<String, String>> students) {
		this.students = students;
	}

	public static StudentList fromMap(HashMap<Integer, HashMap<String, String>> map) {
		return new StudentList(map);
	}

	public static StudentList fromTroyGradeFile(File file) {
		TFile studentsFile = new TFile(file, STUDENTS_FILE);
		try {
			Input in = new Input(new TFileInputStream(studentsFile));
			HashMap map = FileUtils.kryo.readObject(in, HashMap.class);
			in.close();
			return new StudentList(map);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void save(File file) {
		TFile studentsFile = new TFile(file, STUDENTS_FILE);
		try {
			Output out = new Output(new TFileOutputStream(studentsFile));
			assert students.getClass() == HashMap.class;
			FileUtils.kryo.writeObject(out, students);
			out.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
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
}
