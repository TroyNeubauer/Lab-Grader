package com.troy.labgrader.ui;

import java.awt.Dimension;

import javax.swing.JSplitPane;

import com.troy.labgrader.lab.*;

public class LabViewer extends JSplitPane {

	private Lab lab;

	public LabViewer(Lab lab, CourseViewer parent) {
		super(JSplitPane.HORIZONTAL_SPLIT, new LabDataEditor(lab, parent), new LabResultsViewer(lab));
		this.lab = lab;
		setDividerSize(10);
		setPreferredSize(new Dimension(1500, 800));
	}

	public Lab getLab() {
		return lab;
	}
}
