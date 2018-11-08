package com.troy.labgrader.ui;

import java.awt.*;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.tools.ToolProvider;

import com.troy.labgrader.*;
import com.troy.labgrader.lab.*;

public class CourseViewer extends JPanel {

	private final MyCourse course;
	private YearViewer parent;
	private JTabbedPane pane;
	private JLabel evaluatedPeriods = new JLabel("");

	public CourseViewer(MyCourse course, YearViewer parent) {
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

		JTextField periods = new JTextField(null, null, 20);
		bottom.add(new JLabel("Periods that this course is taught during eg. \"1,2,4,7\""));
		bottom.add(periods);
		periods.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			private void update() {
				String[] parts = periods.getText().split(",");
				ArrayList<Integer> origional = new ArrayList<Integer>(course.getPeroids());
				course.getPeroids().clear();
				String otherText = "Periods are: ";
				for (String part : parts) {
					if (!part.isEmpty()) {
						part = part.trim();
						try {
							int value = Integer.parseInt(part);
							course.getPeroids().add(value);
							otherText += value;
							otherText += ", ";
						} catch (Exception ignore) {
						}
					}
				}
				if (origional.equals(course.getPeroids())) {
					System.out.println("skipping");
					return;
				}
				course.periodsUpdated(parent.getYear());
				for (int i = 0; i < pane.getTabCount(); i++) {
					LabViewer viewer = (LabViewer) pane.getComponent(i);
					if (viewer == null)
						continue;
					viewer.periodsUpdated(parent.getYear());
				}
				evaluatedPeriods.setText(otherText);
			}
		});
		String periodsText = "";
		for (int peroid : course.getClasses().keySet()) {
			periodsText += peroid;
			periodsText += ',';
		}
		bottom.add(evaluatedPeriods);
		periods.setText(periodsText);

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
		SwingUtilities.invokeLater(() -> pane.addTab(lab.getData().getName(), new LabViewer(lab, this)));
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
				if (editor.getLab().getData().equals(lab)) {
					pane.setTitleAt(i, text);
					break;
				}
			}
		}
	}

	public YearViewer getYearViewer() {
		return parent;
	}

	public StudentList getStudentsInCourse() {
		return course.getStudentsInCourse(parent.getYear());
	}

	public MyCourse getCourse() {
		return course;
	}

	public void updateLabData() {
		for (int i = 0; i < pane.getTabCount(); i++) {
			Component raw = pane.getComponent(i);
			if (raw instanceof LabViewer) {
				LabViewer editor = (LabViewer) raw;
				editor.updateLabData();
			}
		}
	}
}
