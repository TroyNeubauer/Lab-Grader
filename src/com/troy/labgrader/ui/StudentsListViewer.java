package com.troy.labgrader.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.troy.labgrader.*;

public class StudentsListViewer extends JPanel {

	private static final int PERIOD_COL = 0, NAME_COL = 1, EMAIL_COL = 2, CLASS_COL = 3, HEADER_ROW = 0, DATA_ROW_START = 1;

	private JTable table = new JTable(new Model(15, 4));
	private int count;
	private StudentList list;

	public StudentsListViewer(StudentList list) {
		this.list = list;
		table.setValueAt("Period", HEADER_ROW, PERIOD_COL);
		table.setValueAt("Name", HEADER_ROW, NAME_COL);
		table.setValueAt("Email", HEADER_ROW, EMAIL_COL);
		table.setValueAt("Course", HEADER_ROW, CLASS_COL);
		count = list.getStudents().size() + DATA_ROW_START;
		addStudent(5, "Troy Neubauer", "troyneubauer@gmail.com");
		add(table);
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
			if (row == HEADER_ROW)
				return false;
			if (col == CLASS_COL)
				return false;
			return true;
		}
	};

}
