package com.troy.labgrader.lab;

import java.util.*;

public class Course {
	private List<Integer> peroids;
	private List<Lab> labs;
	private String name;

	public Course() {

	}

	public Course(String name) {
		this.name = name;
		this.labs = new ArrayList<>();
		this.peroids = new ArrayList<>();
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

}
