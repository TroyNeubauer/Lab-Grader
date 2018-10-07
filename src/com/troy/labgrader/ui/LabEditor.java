package com.troy.labgrader.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

public class LabEditor {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LabEditor window = new LabEditor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LabEditor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 608, 682);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblLabName = new JLabel("Lab Name:");
		lblLabName.setBounds(242, 13, 92, 16);
		frame.getContentPane().add(lblLabName);
		
		textField = new JTextField();
		textField.setBounds(220, 31, 116, 22);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblOpeningDate = new JLabel("Opening date");
		lblOpeningDate.setBounds(12, 76, 92, 16);
		frame.getContentPane().add(lblOpeningDate);
		
		JLabel lblClosingDate = new JLabel("Closing date");
		lblClosingDate.setBounds(12, 125, 92, 16);
		frame.getContentPane().add(lblClosingDate);
	}
}
