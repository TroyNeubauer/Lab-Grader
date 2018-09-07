package com.troy.labgrader.ui;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import com.troy.labgrader.Utils;
import com.troy.labgrader.lab.*;

public class LabGraderFileViewer extends JPanel {
	private LabGraderFile file;
	private JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	private JTabbedPane pane = new JTabbedPane();

	public LabGraderFileViewer(LabGraderFile file) {
		super(new BorderLayout());
		this.file = file;
		for(Year year : file.getYears()) {
			addYear(year, false);
		}
		JButton newYear = new JButton("New Year");
		newYear.addActionListener((e) -> showNewYearDialog());
		
		bottom.add(newYear);

		add(bottom, BorderLayout.SOUTH);
		add(pane, BorderLayout.NORTH);

		if (file.getYears().isEmpty()) {
			showNewYearDialog();
		}
	}

	private void addYear(Year year, boolean addToFile) {
		pane.add(year.getName(), new YearViewer(year, this));
		if (addToFile)
			file.getYears().add(year);
	}

	private void showNewYearDialog() {
		String name = Utils.getUserString(this, "Enter new name for the new year", "Set Name", JOptionPane.INFORMATION_MESSAGE);
		addYear(new Year(new ArrayList<Course>(), name), true);
	}

	public LabGraderFileViewer(File file) {
		this(new LabGraderFile(file));
	}

	public void save() {
		file.save();
	}

	public void close() {
		file.close();
	}

	public LabGraderFile getFile() {
		return file;
	}

	public JTabbedPane getPane() {
		return pane;
	}

	public YearViewer getOpenYear() {
		int index = pane.getSelectedIndex();
		if (index == -1) {
			return null;
		}
		return (YearViewer) pane.getComponentAt(index);
	}

}
