package com.troy.labgrader.ui;

import java.awt.Component;
import java.io.*;

import javax.swing.*;

public class Pane extends JTabbedPane {

	JFileChooser chooser = new JFileChooser();

	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		if (!(comp instanceof LabGraderFileViewer))
			throw new IllegalArgumentException();
		super.addImpl(comp, constraints, index);
	}

	@Override
	public LabGraderFileViewer getComponentAt(int index) {
		return (LabGraderFileViewer) super.getComponentAt(index);
	}

	@Override
	public LabGraderFileViewer getSelectedComponent() {
		int index = getSelectedIndex();
		if (index == -1) {
			return null;
		}
		return getComponentAt(index);
	}

	public void openFile(File file) {
		try {
			int index = getTabCount();
			if (file == null)
				return;
			insertTab(file.getName(), null, new LabGraderFileViewer(file), null, index);
			setSelectedIndex(index);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error reading file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void showOpenDialog() {
		chooser.setDialogTitle("Open File");

		chooser.showOpenDialog(this);
		openFile(chooser.getSelectedFile());
	}

	public void closeSelectedFile() {
		LabGraderFileViewer viewer = getSelectedComponent();
		if (viewer != null) {
			removeTabAt(getSelectedIndex());
			try {
				viewer.close();
			} catch (RuntimeException e) {
				JOptionPane.showMessageDialog(this, "Error closing file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void close() {
		for(int i = 0; i < getTabCount(); i++) {
			closeSelectedFile();
		}
	}
}
