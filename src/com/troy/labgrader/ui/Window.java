package com.troy.labgrader.ui;

import java.awt.event.*;

import javax.swing.JFrame;

import com.troy.labgrader.*;
import com.troy.labgrader.Displays.DisplaysListener;

public class Window extends JFrame implements DisplaysListener {
	private static final String TITLE = "Troy's Lab Viewer";

	@Override
	public void onUpdate(String variable, Object value) {
		if (variable.equals(Displays.CONNECTED)) {
			((Boolean) value).booleanValue();

		}
	}

	private LabGraderFileViewer file;

	public Window(LabGraderFileViewer file) {
		super(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add();
		addListeners();

		setSize(1440, 810);
		setLocationRelativeTo(null);
		setVisible(true);
		// pane.showOpenDialog();
	}

	private void add() {
		setJMenuBar(new Menu(this));
	}

	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//pane.close();
			}
		});
	}

	public static LabGraderFileViewer getOpenFile() {
		return Main.window.pane.getSelectedComponent();
	}

	public static YearViewer getOpenYear() {
		LabGraderFileViewer view = Main.window.pane.getSelectedComponent();
		if (view == null)
			return null;
		return view.getOpenYear();
	}

}
