package com.troy.labgrader.ui;

import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import javax.swing.*;

import com.troy.labgrader.*;
import com.troy.labgrader.email.Student;

public class StudentListViewer extends JPanel {

	private StudentList list;
	private FieldTabel<Student> tabel;

	public StudentListViewer(StudentList list) {
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);
		this.list = list;
		this.tabel = new FieldTabel<Student>(list.getStudents(), Student.class);
		JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JTextField peroid = new JTextField(5);
		JTextField name = new JTextField(30);
		JTextField email = new JTextField(40);
		JTextField id = new JTextField(8);
		JButton add = new JButton("Add");
		add.addActionListener((a) -> {
			long p = Utils.getInt(peroid.getText());
			long sID = Utils.getInt(id.getText());
			String n = name.getText();
			String e = email.getText();
			if (p == Utils.INVALID_STRING) {
				JOptionPane.showMessageDialog(this, "You must specify a numerical peroid");
			} else if (n.isEmpty()) {
				JOptionPane.showMessageDialog(this, "You must specify a name peroid");
			} else if (e.isEmpty()) {
				JOptionPane.showMessageDialog(this, "You must specify an email");
			} else if (sID == Utils.INVALID_STRING) {

			} else {
				addStudent((int) p, n, e, (int) sID);
				peroid.setText("");
				name.setText("");
				email.setText("");
				id.setText("");
			}
		});

		addPanel.add(new JLabel("Peroid:"));
		addPanel.add(peroid);
		addPanel.add(new JLabel("Name:"));
		addPanel.add(name);
		addPanel.add(new JLabel("Email:"));
		addPanel.add(email);
		addPanel.add(new JLabel("Student ID:"));
		addPanel.add(id);

		addPanel.add(add);
		add(addPanel);
		add(new JScrollPane(new JTable(tabel)));

		JPanel bottomPanel = new JPanel();
		JButton export = new JButton("Export to Excel");
		JButton import1 = new JButton("Import from Excel");
		export.addActionListener((e) -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save Excel File");
			chooser.setFileFilter(new FileUtils.ExcelFileFilter());
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.showOpenDialog(null);
			File file = chooser.getSelectedFile();
			if (file == null)
				return;
			file = Utils.setExtention(file, FileUtils.EXCEL_EXTENSION);
			tabel.exportToExcel(file);
		});
		import1.addActionListener((e) -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open File");
			chooser.setFileFilter(new FileUtils.ExcelFileFilter());
			chooser.showOpenDialog(null);
			File file = chooser.getSelectedFile();
			if (file == null)
				return;
			List<Student> data = FieldTabel.importFromExcel(file, Student.class);
			this.tabel.setData(data);
			list.setStudents(data);
		});

		bottomPanel.add(export);
		bottomPanel.add(import1);

		add(bottomPanel);

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				save();
				System.out.println("Lost");
			}
		});
		if (list.getStudents().size() > 0) {
			setTabelFromList(list);
		}
	}

	private void setTabelFromList(StudentList list) {
		this.list = list;

	}

	private void save() {

	}

	public void addStudent(int period, String name, String email, int id) {
		tabel.add(new Student(period, name, email, id));
	}

}
