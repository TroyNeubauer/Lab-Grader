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
	private CourseViewer parent;


	public static Lab newLab() {
		JFrame frame;
		LabEditor editor;
		final AtomicBoolean done = new AtomicBoolean(false), complete = new AtomicBoolean(true);
		System.out.println(Thread.currentThread());
		frame = new JFrame("New Lab");
		editor = new LabEditor(null, true, null, () -> done.set(true));
		frame.add(editor);
		frame.setSize(650, 700);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				complete.set(false);
				done.set(true);
				System.out.println("closing");
			};
		});
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		System.out.println("waiting...");
		while (done.get() == false) {
			try {
				Thread.sleep(10);
				frame.repaint();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		frame.setVisible(false);
		frame.dispose();
		frame = null;
		if (complete.get())
			return editor.getLab();
		else
			return null;
	}

	public static void main(String[] args) {
		System.out.println(newLab());
	}

	public LabEditor(Lab lab, CourseViewer parent) {
		this(lab, false, parent, null);
	}

	private LabEditor(Lab origionalLab, boolean window, CourseViewer parent, Runnable onCreate) {
		super();
		this.parent = parent;
		super.setLayout(null);
		if (origionalLab == null) {
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
		
		if(origionalLab != null) {
			name.setText(origionalLab.getName());
			output.setText(origionalLab.getOutput());
			
			openDate.setDate(origionalLab.getOpen());
			closeDate.setDate(origionalLab.getClose());
			
			openTime.setTime(origionalLab.getOpen());
			closeTime.setTime(origionalLab.getClose());
		}
	}

	private void update() {
		lab.setOutput(output.getText());
		lab.setOpen(openDate.getDate(openTime.getSelection()));
		lab.setClose(closeDate.getDate(closeTime.getSelection()));
		lab.setName(name.getText());
		if (parent != null)
			parent.setLabName(lab, name.getText());
	}

	public Lab getLab() {
		return lab;
	}

}
