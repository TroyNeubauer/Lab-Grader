package com.troy.labgrader.ui;

import java.lang.reflect.*;
import java.util.*;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import static java.lang.reflect.Modifier.*;

public class FieldTabel<T> implements TableModel {

	private List<T> data;
	private Field[] fields;
	
	public FieldTabel(List<T> data, Class<T> type) {
		Field[] rawFields = type.getDeclaredFields();
		ArrayList<Field> temp = new ArrayList<Field>();
		for(Field f : rawFields) {
			if(!isTransient(f.getModifiers())) {
				temp.add(f);
			}
		}
		fields = new Field[temp.size()];
		temp.toArray(fields);
		System.out.println(Arrays.toString(fields));
	}
	
	@Override
	public int getRowCount() {

		return 0;
	}

	@Override
	public int getColumnCount() {

		return 0;
	}

	@Override
	public String getColumnName(int columnIndex) {

		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

}
