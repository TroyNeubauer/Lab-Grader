package com.troy.labgrader.lab;

import java.util.List;

import com.troy.labgrader.StudentList;

public class Year {
	public static final String YEARS_FILE = "years.dat";

	private List<Course> courses;
	private StudentList students = new StudentList();
	private String name;

	public Year(List<Course> courses, String name) {
		this.courses = courses;
		this.name = name;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public StudentList getStudents() {
		return students;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
