package com.troy.labgrader.ui;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import com.troy.labgrader.lab.*;

public class LabGraderFileViewer extends JPanel {
	private LabGraderFile file;
	private JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JComboBox<String> years = new JComboBox<String>();
	private ArrayList<YearViewer> yearsComp = new ArrayList<YearViewer>();

	public LabGraderFileViewer(LabGraderFile file) {
		super(new GridLayout());
		this.file = file;
		years.setEditable(false);
		for (Year year : file.getYears()) {
			addYear(year, false);
		}
		years.addActionListener((e) -> {
			Object com = years.getSelectedItem();
			if (com instanceof String) {
				Year year = null;
				for (Year yearI : file.getYears()) {
					if (yearI.getName().equals(com)) {
						year = yearI;
					}
				}
				if(year == null) {
					System.err.println("");
				}
				System.out.println("year string: " + com);
				System.out.println("year " + year);
			} else {
				System.err.println("object in years is not a string!!!");
			}
		});
		JButton newYear = new JButton("New Year");
		newYear.addActionListener((e) -> showNewYearDialog());

		top.add(years);
		top.add(newYear);

		add(top, BorderLayout.NORTH);

		if (file.getYears().isEmpty()) {
			showNewYearDialog();
		} else {
			years.setSelectedIndex(0);// Trigger the listener
		}
	}

	private void addYear(Year year, boolean addToFile) {
		years.addItem(year.getName());
		if (addToFile)
			file.getYears().add(year);
	}

	private void showNewYearDialog() {
		addYear(new Year(new ArrayList<>(), "Test year"), true);
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

}
