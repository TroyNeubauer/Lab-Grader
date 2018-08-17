package com.troy.labgrader.lab;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.troy.labgrader.ui.*;

public class YearViewer extends JPanel {
	private Year year;
	private JTabbedPane pane = new JTabbedPane(JTabbedPane.LEFT);
	private JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private LabGraderFileViewer viewer;

	public YearViewer(Year year, LabGraderFileViewer viewer) {
		this.year = year;
		this.viewer = viewer;

		JButton newCourse = new JButton("New Course");
		newCourse.addActionListener((e) -> showNewCourseDialog());
		bottom.add(newCourse);

		JButton editName = new JButton("Edit Year Name");
		editName.addActionListener((e) -> {
			String s = (String) JOptionPane.showInputDialog(this, "Enter new name for the year \"" + year.getName() + "\".", "Edit Name", JOptionPane.INFORMATION_MESSAGE, null, null, null);
			System.out.println("entered: " + s);
		});
		bottom.add(editName);

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
