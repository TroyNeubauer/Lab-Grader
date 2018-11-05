package com.troy.labgrader.ui;

import java.awt.*;

import javax.swing.*;

import com.troy.labgrader.Utils;
import com.troy.labgrader.lab.*;

public class CourseViewer extends JPanel {

	private final Course course;
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
			int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete course \"" + course.getName() + "\"?" + '\n' + "You cannot undo this operation",
					"Delete \"" + course.getName() + "\"", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (result == 0)
				parent.removeCourse(course);
		});
		bottom.add(deleteCourse);
		JButton newLab = new JButton("New Lab");
		newLab.addActionListener((e) -> {
			new Thread(() -> {
				LabData data = LabDataEditor.newLab();
				if (data != null)
					addLab(new Lab(data, new LabResults()), true);
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
		if (toFile)
			course.getLabs().add(lab);
		pane.addTab(lab.getData().getName(), new LabViewer(lab, this));
	}

	protected void setCourseName(String name) {
		course.setName(name);
		parent.getPane().setTitleAt(parent.getPane().getSelectedIndex(), name);
	}

	public void setLabName(LabData lab, String text) {
		for (int i = 0; i < pane.getTabCount(); i++) {
			Component raw = pane.getComponent(i);
			if (raw instanceof LabViewer) {
				LabViewer editor = (LabViewer) raw;
				if (editor.getLab().equals(lab)) {
					pane.setTitleAt(i, text);
					break;
				}
			}
		}
	}
}
