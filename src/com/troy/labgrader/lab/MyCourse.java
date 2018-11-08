package com.troy.labgrader.lab;

import java.util.*;

import com.troy.labgrader.StudentList;

public class MyCourse {
	private Map<Integer, StudentList> classes;
	private List<Lab> labs;
	private String name;

	public MyCourse(String name) {
		this.name = name;
		this.labs = new ArrayList<Lab>();
		this.classes = new HashMap<Integer, StudentList>();
	}

	public String getName() {
		return name;
	}

	public Map<Integer, StudentList> getClasses() {
		return classes;
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

	public StudentList getStudentsInCourse() {
		
		return null;
	}

}
