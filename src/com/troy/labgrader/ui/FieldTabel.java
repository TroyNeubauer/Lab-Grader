package com.troy.labgrader.ui;

import static java.lang.reflect.Modifier.*;

import java.lang.reflect.Field;
import java.util.*;

import javax.swing.event.*;
import javax.swing.table.*;

import com.troy.labgrader.*;

public class FieldTabel<T> implements TableModel {

	private List<T> data;
	private Field[] fields;
	private Class<T> type;
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	public FieldTabel(List<T> data, Class<T> type) {
		super();
		this.data = Objects.requireNonNull(data);
		this.type = Objects.requireNonNull(type);
		Field[] rawFields = type.getDeclaredFields();
		ArrayList<Field> temp = new ArrayList<Field>();
		int i = 0;
		for (Field f : rawFields) {
			if (!isTransient(f.getModifiers())) {
				temp.add(f);
				f.setAccessible(true);
			}
		}
		fields = new Field[temp.size()];
		temp.toArray(fields);
		// System.out.println(Arrays.toString(fields));
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {

		return fields.length;
	}

	@Override
	public String getColumnName(int columnIndex) {

		return fields[columnIndex].getName();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> c = fields[columnIndex].getType();
		if (c.isPrimitive())
			return String.class;
		return c;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		boolean quit = rowIndex == data.size();
		if (quit)
			return null;
		// System.out.println("get [" + rowIndex + ", " + columnIndex + "]");
		try {
			T obj = data.get(rowIndex);
			return fields[columnIndex].get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void addRow() {
		try {
			data.add(type.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			// System.out.println("throring: " + MiscUtil.getStackTrace(e));
			try {
				data.add(MiscUtil.newInstanceUsingAConstructor(type));

			} catch (RuntimeException e2) {
				// System.out.println("throring again: " + MiscUtil.getStackTrace(e2));
				if (MiscUtil.isUnsafeSupported()) {
					try {
						data.add((T) MiscUtil.getUnsafe().allocateInstance(type));
					} catch (Exception e3) {
						throw new RuntimeException(e3);
					}
				}
				throw new RuntimeException(e2);
			}
		}
		fireEvent(new TableModelEvent(this, data.size() - 1, data.size(), 0, TableModelEvent.INSERT));

	}

	private void fireEvent(TableModelEvent e) {
		for (TableModelListener l : listeners) {
			l.tableChanged(e);
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// System.out.println("set [" + rowIndex + ", " + columnIndex + "]: " + aValue);
		try {
			T obj = data.get(rowIndex);
			Field f = fields[columnIndex];
			Class<?> c = f.getType();
			// System.out.println("Type " + c);
			if (c.isPrimitive()) {
				if (c == byte.class) {
					if (aValue instanceof String) {
						long value = Utils.getInt((String) aValue);
						if (value != Utils.INVALID_STRING) {
							f.setByte(obj, (byte) value);
						}
					}
					if (aValue instanceof Number) {
						f.setByte(obj, ((Number) aValue).byteValue());
					}
				} else if (c == short.class) {
					if (aValue instanceof String) {
						long value = Utils.getInt((String) aValue);
						if (value != Utils.INVALID_STRING) {
							f.setShort(obj, (short) value);
						}
					}
					if (aValue instanceof Number) {
						f.setShort(obj, ((Number) aValue).shortValue());
					}
				} else if (c == int.class) {
					if (aValue instanceof String) {
						long value = Utils.getInt((String) aValue);
						if (value != Utils.INVALID_STRING) {
							f.setInt(obj, (int) value);
						}
					}
					if (aValue instanceof Number) {
						f.setInt(obj, ((Number) aValue).intValue());
					}
				} else if (c == long.class) {
					if (aValue instanceof String) {
						long value = Utils.getLong((String) aValue);
						if (value != Utils.INVALID_STRING) {
							f.setLong(obj, (long) value);
						}
					}
					if (aValue instanceof Number) {
						f.setLong(obj, ((Number) aValue).longValue());
					}
				} else if (c == float.class) {
					if (aValue instanceof String) {
						double value = Utils.getFloat((String) aValue);
						if (value != Double.MIN_VALUE) {
							f.setFloat(obj, (float) value);
						}
					}
					if (aValue instanceof Number) {
						f.setFloat(obj, ((Number) aValue).floatValue());
					}
				} else if (c == double.class) {
					if (aValue instanceof String) {
						double value = Utils.getDouble((String) aValue);
						if (value != Double.MIN_VALUE) {
							f.setDouble(obj, (float) value);
						}
					}
					if (aValue instanceof Number) {
						f.setDouble(obj, ((Number) aValue).doubleValue());
					}
				} else if (c == char.class) {
					if (aValue instanceof String && ((String) aValue).length() > 0) {
						f.setChar(obj, ((String) aValue).charAt(0));
					}
					if (aValue instanceof Character) {
						f.setChar(obj, ((Character) aValue).charValue());
					}
				} else if (c == boolean.class) {
					if (aValue instanceof Boolean) {
						f.setBoolean(obj, ((Boolean) aValue).booleanValue());
					}
				}
			} else {
				f.set(obj, aValue);
			}
			fireEvent(new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public void add(T t) {
		data.add(t);
		fireEvent(new TableModelEvent(this, data.size() - 1, data.size(), 0, TableModelEvent.INSERT));
	}

}
