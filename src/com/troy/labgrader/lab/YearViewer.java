package com.troy.labgrader.lab;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.troy.labgrader.ui.LabGraderFileViewer;

public class YearViewer extends JPanel {
	private Year year;
	private JTabbedPane pane = new JTabbedPane(JTabbedPane.LEFT);
	private JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JTextField name = new JTextField(30);
	private LabGraderFileViewer viewer;
	
	public YearViewer(Year year, LabGraderFileViewer viewer) {
		this.year = year;
		this.viewer = viewer;
		name.setText(year.getName());
		name.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				setYearName(name.getText());
				super.keyTyped(e);
			}
		});
		
		JButton newCourse = new JButton("New Course");
		newCourse.addActionListener((e) -> showNewCourseDialog());
		bottom.add(name);
		bottom.add(newCourse);

		pane.addTab("Students List", new StudentsListViewer(year.getStudents()));
		
		add(bottom, BorderLayout.SOUTH);
		add(pane, BorderLayout.NORTH);
	}

	protected void setYearName(String name) {
		year.setName(name);
		viewer.getPane().setTitleAt(viewer.getPane().getSelectedIndex(), name);
	}

	private void showNewCourseDialog() {
		
	}
	
	
}
