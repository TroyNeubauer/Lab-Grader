package com.troy.labgrader;

import java.util.*;

import com.troy.labgrader.email.Student;

public class StudentList implements Iterable<Student> {

	// Maps period numbers to a mapping between student name and student email
	private ArrayList<Student> students;

	public StudentList() {
		students = new ArrayList();
	}

	public StudentList(ArrayList<Student> students) {
		this.students = students;
	}

	public void addStudent(int periodNumber, String name, String email, int id) {
		students.add(new Student(name, id, periodNumber, email));
	}

	public int getPeriodWithName(String name) {
		for (Student s : students) {
			if (s.getName().equals(name))
				return s.getPeriod();
		}
		return -1;
	}

	public Student getWithEmail(String email) {
		for (Student s : students) {
			if (s.hasEmail(email))
				return s;
		}
		return null;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> data) {
		this.students = data;
	}

	public Student getStudentWithID(int id) {
		for (Student s : students) {
			if (s.getId() == id)
				return s;
		}
		return null;
	}

	@Override
	public Iterator<Student> iterator() {
		return students.iterator();
	}

	public void removeId(int id) {
		Iterator<Student> i = students.iterator();
		while (i.hasNext()) {
			if (i.next().getId() == id) {
				i.remove();
			}
		}
	}
}
