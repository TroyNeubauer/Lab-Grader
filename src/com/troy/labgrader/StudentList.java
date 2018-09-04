package com.troy.labgrader;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.troy.labgrader.email.Student;

public class StudentList {

	// Maps period numbers to a mapping between student name and student email
	private List<Student> students;

	public StudentList() {
		students = new ArrayList();
	}

	public StudentList(List<Student> students) {
		this.students = students;
	}

	public void addStudent(int periodNumber, String name, String email, int id) {
		students.add(new Student(periodNumber, name, email, id));
	}

	public int getPeriodWithName(String name) {
		for (Student s : students) {
			if (s.getName().equals(name))
				return s.getPeriod();
		}
		return -1;
	}

	public int getPeriodWithEmail(String email) {
		for (Student s : students) {
			if (s.getEmail().equals(email))
				return s.getPeriod();
		}
		return -1;
	}

	public List<Student> getStudents() {
		return students;
	}
}
