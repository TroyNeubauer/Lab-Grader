package com.troy.labgrader.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.swing.event.*;

import com.troy.labgrader.lab.*;

public class LabDataEditor extends JPanel {

	private JTextField name;
	private JTextArea output;
	private JTimePicker openTime, closeTime;
	private MyJDatePicker openDate, closeDate;
	private LabData lab;
	private CourseViewer parent;

	public static LabData newLab() {
		JFrame frame;
		LabDataEditor editor;
		final AtomicBoolean done = new AtomicBoolean(false), complete = new AtomicBoolean(true);
		System.out.println(Thread.currentThread());
		frame = new JFrame("New Lab");
		editor = new LabDataEditor(null, null, () -> done.set(true));
		frame.getContentPane().add(editor);
		frame.setSize(650, 700);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				complete.set(false);
				done.set(true);
			};
		});
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
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
		if (complete.get()) {
			editor.update();
			return editor.getLab();
		} else
			return null;
	}

	public static void main(String[] args) {
		System.out.println(newLab());
	}

	/**
	 * @wbp.parser.constructor
	 */
	public LabDataEditor(Lab lab, CourseViewer parent) {
		this(lab.getData(), parent, null);
	}

	private LabDataEditor(LabData origionalLab, CourseViewer parent, Runnable onCreate) {// If origional lab is null, we are in a window
		super();
		setMinimumSize(new Dimension(500, 600));

		this.parent = parent;
		setLayout(null);
		if (origionalLab == null) {
			this.lab = new LabData();
		} else {
			this.lab = origionalLab;
		}

		JLabel lblLabName = new JLabel("Lab Name:");
		lblLabName.setBounds(242, 13, 92, 16);
		add(lblLabName);

		name = new JTextField();
		if (origionalLab != null) {
			name.setText(origionalLab.getName());
		}
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
		if (origionalLab != null)
			openTime.setTime(origionalLab.getOpen().toLocalTime());
		else
			openTime.setToNow();
		openTime.setBounds(106, 76, 92, 16);
		openTime.addActionListener((e) -> update());
		add(openTime);

		openDate = new MyJDatePicker();
		if (origionalLab != null)
			openDate.setDate(origionalLab.getOpen().toLocalDate());
		else
			openDate.getModel().setToNow();
		openDate.setBounds(200, 70, 120, 28);
		openDate.addActionListener((e) -> update());
		add(openDate);

		JLabel lblClosingDate = new JLabel("Closing date");
		lblClosingDate.setBounds(12, 125, 92, 16);
		add(lblClosingDate);

		closeTime = new JTimePicker();
		if (origionalLab != null)
			closeTime.setTime(origionalLab.getClose().toLocalTime());
		closeTime.setBounds(106, 125, 92, 16);
		closeTime.addActionListener((e) -> update());
		add(closeTime);

		closeDate = new MyJDatePicker();
		if (origionalLab != null)
			closeDate.setDate(origionalLab.getClose().toLocalDate());
		else {
			closeDate.getModel().setToNow();
			closeDate.getModel().addDay(14);
		}
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
		output.setTabSize(4);
		if (origionalLab != null)
			output.setText(origionalLab.getOutput());
		output.setFont(new Font("", Font.PLAIN, 14));
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
		output.setColumns(25);
		output.setRows(22);
		view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		view.setBounds(12, 181, 470, 317);
		add(view);

		if (origionalLab == null) {
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
		if (parent != null) {
			parent.setLabName(lab, name.getText());
			parent.updateLabData();
		}
	}

	public LabData getLab() {
		return lab;
	}

}
