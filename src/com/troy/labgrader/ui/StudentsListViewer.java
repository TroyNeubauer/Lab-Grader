package com.troy.labgrader.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.troy.labgrader.StudentList;

public class StudentsListViewer extends JPanel {

	private static final int PERIOD_COL = 0, NAME_COL = 1, EMAIL_COL = 2, CLASS_COL = 3, HEADER_ROW = 0, DATA_ROW_START = 1;

	private JTable table = new JTable(new Model(4, 15));

	public StudentsListViewer(StudentList list) {
		table.setValueAt("Period", PERIOD_COL, HEADER_ROW);
		table.setValueAt("Name", NAME_COL, HEADER_ROW);
		table.setValueAt("Email", EMAIL_COL, HEADER_ROW);
		table.setValueAt("Course", CLASS_COL, HEADER_ROW);
		addStudent(5, "Troy Neubauer", "troyneubauer@gmail.com");
	}

	public void addStudent(int period, String name, String email) {
		int row = DATA_ROW_START;
		while(row < table.getRowCount()) {
			if(period >= Integer.parseInt(table.getValueAt(row, PERIOD_COL).toString())) {
				
			}
		}
		table.getValueAt(row, column)
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
