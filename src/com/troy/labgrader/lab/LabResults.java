package com.troy.labgrader.lab;

import java.util.*;

import com.troy.labgrader.StudentList;

public class LabResults {
	private HashMap<Integer, LabResult> results = new HashMap<Integer, LabResult>();// Maps student ID to lab result

	public LabResult get(int id) {
		return results.get(id);
	}

	public void put(int id, LabResult result) {
		results.put(id, result);
	}

	public HashMap<Integer, LabResult> getMap() {
		return results;
	}

	public void setMap(HashMap<Integer, LabResult> hashMap) {
		results = hashMap;
	}

	public void periodsUpdated(MyCourse course, Year year) {
		StudentList remainingStudents = course.getStudentsInCourse();
		for(Integer entry : results.keySet()) {//Loop through every student's ID
			remainingStudents.removeId(entry);
		}
		//The only students left in remainingStudents are those who don't have a LabResult Yet
		for(Student student : remainingStudents) {
			put(student.getId(), new LabResult(student.getId()));
		}
	}

	@Override
	public String toString() {
		return "LabResults: " + results;
	}
	
	

}
