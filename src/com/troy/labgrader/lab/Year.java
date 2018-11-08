package com.troy.labgrader.lab;

import java.util.List;

import com.troy.labgrader.StudentList;

public class Year {

	private List<Course> courses;
	private StudentList students = new StudentList();
	private String name;
	private int selectedTab = -1;//-1 for invalid, [0,students.size()) to indicate selected course index

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

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

}
