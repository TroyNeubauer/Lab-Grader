package com.troy.labgrader.ui;

import java.awt.Font;

import javax.swing.*;

import com.troy.labgrader.lab.Lab;

public class LabResultsViewer extends JPanel {

	private JTextArea area = new JTextArea(15, 25);
	private Lab lab;

	public LabResultsViewer(Lab lab) {
		area.setEditable(false);
		area.setBackground(this.getBackground());
		area.setFont(new Font("", Font.PLAIN, 13));

		add(area);
		update();
	}

	public void update() {
		area.setText("test1\ntest2\ntest3\n\n\ntest5");
	}
}
