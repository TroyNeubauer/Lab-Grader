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
			new Thread(() -> {
				Lab lab = LabEditor.newLab();
				if (lab != null)
					addLab(lab, true);
			}).start();
		});
		bottom.add(newLab);
		add(bottom, BorderLayout.SOUTH);
		add(pane, BorderLayout.CENTER);

		for (Lab lab : course.getLabs()) {
			addLab(lab, false);
		}
	}

	private void addLab(Lab lab, boolean toFile) {
		System.out.println("adding lab " + lab);
		if (toFile)
			course.getLabs().add(lab);
		System.out.println("name " + lab.getName());
		pane.addTab(lab.getName(), new LabEditor(lab, this));
	}

	protected void setCourseName(String name) {
		course.setName(name);
		parent.getPane().setTitleAt(parent.getPane().getSelectedIndex(), name);
	}

	public void setLabName(Lab lab, String text) {
		System.out.println("about to set title");
		for (int i = 0; i < pane.getTabCount(); i++) {
			Component raw = pane.getComponent(i);
			if (raw instanceof LabEditor) {
				LabEditor editor = (LabEditor) raw;
				if (editor.getLab().equals(lab)) {
					pane.setTitleAt(i, text);
					System.out.println("set title " + text);
					break;
				}
			}
		}
	}
}
