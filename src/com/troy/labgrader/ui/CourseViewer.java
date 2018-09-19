package com.troy.labgrader.ui;

import java.awt.*;

import javax.swing.*;

import com.troy.labgrader.Utils;
import com.troy.labgrader.lab.*;

public class CourseViewer extends JPanel {

	private Course course;
	private YearViewer parent;
	private JTabbedPane pane;

	public CourseViewer(Course course, YearViewer parent) {
		super(new BorderLayout());
		this.course = course;
		this.parent = parent;
		this.pane = new JTabbedPane();

		JPanel bottom = new JPanel();

		JButton editName = new JButton("Edit Course Name");
		editName.addActionListener((e) -> {
			String s = Utils.getUserString(this, "Enter new name for the course \"" + course.getName() + "\".", "Edit Name", JOptionPane.INFORMATION_MESSAGE);
			setCourseName(s);
		});
		bottom.add(editName);

		JButton deleteCourse = new JButton("Delete Course");
		deleteCourse.addActionListener((e) -> {
			parent.removeCourse(course);
		});
		bottom.add(deleteCourse);
		JButton newLab = new JButton("New Lab");
		newLab.addActionListener((e) -> {
			showNewLabDialog();
		});
		bottom.add(newLab);
		add(bottom, BorderLayout.SOUTH);
		add(pane, BorderLayout.CENTER);

	}

	private static class LabEditor extends JPanel {

		private JTextField name = new JTextField();
		private JViewport output = new JViewport();
		private JTextArea outputArea = new JTextArea(30, 40);
		private Lab lab;

		public LabEditor() {
			this(new Lab());
		}
		
		public LabEditor(Lab lab) {
			output.setView(outputArea);
			
			GridBagConstraints c = new GridBagConstraints();
			
			add(new JLabel("Lab Name: "), c);
			
			c.gridx++;
			add(name, c);
			
			c.gridy++;
			c.gridx = 0;
			add(new JLabel("Desired Output: "), c);
			
			c.gridy++;
			

		}
	}

	private void showNewLabDialog() {

	}

	protected void setCourseName(String name) {
		course.setName(name);
		parent.getPane().setTitleAt(parent.getPane().getSelectedIndex(), name);
	}
}
