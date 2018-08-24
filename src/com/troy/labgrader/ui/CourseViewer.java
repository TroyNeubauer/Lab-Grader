package com.troy.labgrader.ui;

import java.awt.*;

import javax.swing.JPanel;

import com.troy.labgrader.lab.Course;

public class CourseViewer extends JPanel {

	private Course course;

	public CourseViewer(Course course) {
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.course = course;

	}
}
