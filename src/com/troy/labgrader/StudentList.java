package com.troy.labgrader;

import java.util.*;

import com.troy.labgrader.lab.Student;

public class StudentList implements Iterable<Student> {

	// Maps period numbers to a mapping between student name and student email
	private ArrayList<Student> students;

	public StudentList() {
		students = new ArrayList();
	}

	public StudentList(ArrayList<Student> students) {
		this.students = students;
	}

	public void addStudent(String name, int id) {
		students.add(new Student(name, id));
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
