package com.troy.labgrader.ui;

import java.awt.event.*;

import javax.swing.JFrame;

import com.troy.labgrader.Main;

public class Window extends JFrame {
	private static final String TITLE = "Troy's Lab Viewer";

	private Pane pane;

	public Window() {
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
		pane = new Pane();
		add(pane);
		setJMenuBar(new Menu(pane));
	}

	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				pane.close();
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
