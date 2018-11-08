package com.troy.labgrader.lab;

import java.util.*;

import com.troy.labgrader.StudentList;
import com.troy.labgrader.email.Student;

public class Course {
	private List<Integer> peroids;
	private List<Lab> labs;
	private String name;

	public Course(String name) {
		this.name = name;
		this.labs = new ArrayList<Lab>();
		this.peroids = new ArrayList<Integer>();
	}

	public String getName() {
		return name;
	}

	public List<Integer> getPeroids() {
		return peroids;
	}

	public List<Lab> getLabs() {
		return labs;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Called to indicate that the periods that this class is taught during changed so we may need to re-shuffle switch which students can submit
	 * which lab
	 */
	public void periodsUpdated(Year year) {
		for (Lab lab : labs) {
			lab.periodsUpdated(this, year);
		}
	}

	public StudentList getStudentsInCourse(Year year) {
		ArrayList<Student> list = new ArrayList<Student>();
		for (Student student : year.getStudents()) {
			for (int peroid : peroids) {
				if (student.getPeriod() == peroid) {
					list.add(student);
					break;
				}
			}
		}
		return new StudentList(list);
	}

}
