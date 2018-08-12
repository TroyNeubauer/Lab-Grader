package com.troy.labgrader.lab;

import java.awt.*;

import javax.swing.*;

public class YearViewer extends JPanel {
	private Year year;
	private JTabbedPane pane = new JTabbedPane();
	private JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	public YearViewer(Year year) {
		this.year = year;
		
		JButton newCourse = new JButton("New Course");
		newCourse.addActionListener((e) -> showNewCourseDialog());

		bottom.add(newCourse);

		add(bottom, BorderLayout.SOUTH);
		add(pane, BorderLayout.NORTH);
	}

	private void showNewCourseDialog() {
		
	}
	
	
}
