package com.troy.labgrader.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import com.troy.labgrader.*;

public class StudentsListViewer extends JPanel {

	private static final int PERIOD_COL = 0, NAME_COL = 1, EMAIL_COL = 2, CLASS_COL = 3, DATA_ROW_START = 0;
	private static final String[] COL_NAMES = { "Period", "Name", "Email", "Course" };

	private JTable table = new JTable(new Model(50, 4));
	private int count = DATA_ROW_START;
	private StudentList list;

	public StudentsListViewer(StudentList list) {
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);
		this.list = list;
		JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JTextField peroid = new JTextField(5);
		JTextField name = new JTextField(30);
		JTextField email = new JTextField(40);
		JButton add = new JButton("Add");
		add.addActionListener((a) -> {
			int p = Utils.getInt(peroid.getText());
			String n = name.getText();
			String e = email.getText();
			if (p == Utils.INVALID_STRING) {
				JOptionPane.showMessageDialog(this, "You must specify a numerical peroid");
			} else if (n.isEmpty()) {
				JOptionPane.showMessageDialog(this, "You must specify a name peroid");
			} else if (e.isEmpty()) {
				JOptionPane.showMessageDialog(this, "You must specify an email");
			} else {
				addStudent(p, n, e);
				name.setText("");
				email.setText("");
			}
		});

		addPanel.add(new JLabel("Peroid:"));
		addPanel.add(peroid);
		addPanel.add(new JLabel("Name:"));
		addPanel.add(name);
		addPanel.add(new JLabel("Email:"));
		addPanel.add(email);
		addPanel.add(add);
		add(addPanel);

		add(new JScrollPane(table));
		JButton save = new JButton("Save");

		save.addActionListener((a) -> {
			save();
		});
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				save();
				System.out.println("Lost");
			}
		});
		if(list.getStudents().size() > 0) {
			setTabelFromList(list);
		}
	}

	private void setTabelFromList(StudentList list) {
		this.list = list;
		
	}

	private void save() {

	}

	public void addStudent(int period, String name, String email) {
		table.setValueAt(period, count, PERIOD_COL);
		table.setValueAt(name, count, NAME_COL);
		table.setValueAt(email, count, EMAIL_COL);
		count++;
		list.addStudent(period, name, email);
	}

	static class Model extends DefaultTableModel {
		public Model(int rowCount, int columnCount) {
			super(rowCount, columnCount);

		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == CLASS_COL)
				return false;
			return true;
		}

		@Override
		public String getColumnName(int index) {
			return COL_NAMES[index];
		}
	};

}
