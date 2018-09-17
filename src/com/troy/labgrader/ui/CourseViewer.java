package com.troy.labgrader.ui;

import java.awt.*;

import javax.swing.*;

import com.troy.labgrader.Utils;
import com.troy.labgrader.lab.Course;

public class CourseViewer extends JPanel {

	private Course course;
	private YearViewer parent;

	public CourseViewer(Course course, YearViewer parent) {
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.course = course;
		this.parent = parent;

		JButton editName = new JButton("Edit Course Name");
		editName.addActionListener((e) -> {
			String s = Utils.getUserString(this, "Enter new name for the course \"" + course.getName() + "\".", "Edit Name", JOptionPane.INFORMATION_MESSAGE);
			setCourseName(s);
		});
		add(editName, c);
		
		JButton deleteCourse = new JButton("Delete Course");
		deleteCourse.addActionListener((e) -> {
			parent.removeCourse(course);
		});
		add(deleteCourse, c);

	}

	protected void setCourseName(String name) {
		course.setName(name);
		parent.getPane().setTitleAt(parent.getPane().getSelectedIndex(), name);
	}
}
