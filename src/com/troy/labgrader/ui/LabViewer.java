package com.troy.labgrader.ui;

import java.awt.Dimension;

import javax.swing.JSplitPane;

import com.troy.labgrader.lab.*;

public class LabViewer extends JSplitPane {

	private Lab lab;
	private LabDataEditor dataEditor;
	private LabResultsViewer resultsViewer;
	private CourseViewer parent;

	public LabViewer(Lab lab, CourseViewer parent) {
		super(JSplitPane.HORIZONTAL_SPLIT, new LabDataEditor(lab, parent), new LabResultsViewer(lab, parent));
		this.lab = lab;
		this.dataEditor = (LabDataEditor) getLeftComponent();
		this.resultsViewer = (LabResultsViewer) getRightComponent();
		this.parent = parent;
		setDividerSize(10);
		setPreferredSize(new Dimension(1500, 800));
	}

	public Lab getLab() {
		return lab;
	}

	public void periodsUpdated(Year year) {
		resultsViewer.periodsUpdated(year, parent.getCourse());
	}

	public void updateLabData() {
		resultsViewer.updateLabData();
	}
}
