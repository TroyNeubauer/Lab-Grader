package com.troy.labgrader.ui;

import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.swing.event.*;

import com.troy.labgrader.lab.Lab;

public class LabEditor extends JPanel {

	private JTextField name;
	private JTextArea output;
	private JTimePicker openTime, closeTime;
	private MyJDatePicker openDate, closeDate;
	private Lab lab;

	public static Lab newLab() {
		JFrame frame = new JFrame("New Lab");
		AtomicBoolean done = new AtomicBoolean(false);
		LabEditor editor = new LabEditor(null, true, () -> done.set(true));
		frame.add(editor);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(650, 700);
		frame.setVisible(true);

		while (done.get() == false) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		frame.setVisible(false);
		frame.dispose();
		return editor.getLab();
	}
	
	public static void main(String[] args) {
		System.out.println(newLab());
	}
	
	public LabEditor(Lab lab) {
		this(lab, false, null);
	}

	private LabEditor(Lab origionalLab, boolean window, Runnable onCreate) {
		super();
		super.setLayout(null);
		if(origionalLab == null) {
			this.lab = new Lab();
		} else {
			this.lab = origionalLab;
		}
		
		JLabel lblLabName = new JLabel("Lab Name:");
		lblLabName.setBounds(242, 13, 92, 16);
		add(lblLabName);

		name = new JTextField();
		name.setBounds(220, 31, 116, 22);
		name.setColumns(10);
		name.getDocument().addDocumentListener(new DocumentListener() {

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
		});
		add(name);

		JLabel lblOpeningDate = new JLabel("Opening date");
		lblOpeningDate.setBounds(12, 76, 92, 16);
		add(lblOpeningDate);

		openTime = new JTimePicker();
		openTime.setToNow();
		openTime.setBounds(106, 76, 92, 16);
		openTime.addActionListener((e) -> update());
		add(openTime);

		openDate = new MyJDatePicker();
		openDate.setBounds(200, 70, 120, 28);
		openDate.addActionListener((e) -> update());
		add(openDate);

		JLabel lblClosingDate = new JLabel("Closing date");
		lblClosingDate.setBounds(12, 125, 92, 16);
		add(lblClosingDate);

		closeTime = new JTimePicker();
		closeTime.setBounds(106, 125, 92, 16);
		closeTime.addActionListener((e) -> update());
		add(closeTime);

		closeDate = new MyJDatePicker();
		closeDate.setBounds(200, 119, 120, 28);
		closeDate.addActionListener((e) -> update());
		add(closeDate);
		closeDate.addActionListener((e) -> {
			update();
		});

		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setBounds(260, 156, 46, 14);
		add(lblOutput);

		output = new JTextArea();
		output.getDocument().addDocumentListener(new DocumentListener() {

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
		});
		JScrollPane view = new JScrollPane(output);
		output.setColumns(40);
		output.setRows(20);
		view.setBounds(12, 181, 570, 417);
		add(view);

		if (window) {
			JButton btnAdd = new JButton("Add");
			btnAdd.setBounds(245, 609, 89, 23);
			add(btnAdd);
			btnAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onCreate.run();
				}
			});
		}
	}

	private void update() {
		lab.setOutput(output.getText());
		lab.setOpen(openDate.getDate(openTime.getSelection()));
		lab.setClose(closeDate.getDate(closeTime.getSelection()));
		lab.setName(name.getText());
	}

	public Lab getLab() {
		return lab;
	}

}
