package com.troy.labgrader.ui;

import static java.lang.reflect.Modifier.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import javax.swing.event.*;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

	public static <T> List<T> importFromExcel(File file, Class<T> type) {
		return importFromExcel(file, 0, type);
	}

	public static <T> List<T> importFromExcel(File file, int sheetNumber, Class<T> type) {
		List<T> list = new ArrayList<T>();
		FileInputStream stream;
		XSSFWorkbook workbook = null;

		try {
			stream = new FileInputStream(file);
			workbook = new XSSFWorkbook(stream);
			Sheet sheet = workbook.getSheetAt(0);
			Field[] fields = MiscUtil.getAllNonTransientFields(type);
			Map<Field, Integer> columnIndices = new HashMap<Field, Integer>(fields.length);
			Row row1 = sheet.getRow(0);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				int row = findCol(row1, field.getName());
				if (row == -1)
					System.err.println("Warning: Field name: " + field.getName() + " is not listed in sheet! Will be assigned as null, false, or 0");
				columnIndices.put(field, row);
			}
			int maxRow = -1;
			for (int colInt = 0; colInt < fields.length; colInt++) {
				int rowInt = 1;
				while (true) {
					Row row = sheet.getRow(rowInt);
					Cell cell = null;
					if (row == null || (cell = row.getCell(colInt, MissingCellPolicy.CREATE_NULL_AS_BLANK)) == null) {
						if (rowInt > maxRow) {
							if (maxRow != -1) {
								System.err.println("Uneven rows!");
							}
							if(cell.getCellTypeEnum() != CellType.NUMERIC)
								System.err.println("Non numeric cell at: " + cell.getAddress().formatAsString());
							
							maxRow = rowInt;
							break;
						}
					}
				}
			}// We have either found a nice square area or reported the issue to the user
			
			if (maxRow == -1)// We didnt find any data in the list
				return list;
			for(int i = 1; i < maxRow; i++) {//Start at 1 because the data starts at row 1
				list.add(MiscUtil.newInstanceUsingAnyMeans(type));
			}
			for (Field field : fields) {
				int rowInt = 1;
				for (Row row : new MyIterable<Row>(sheet.iterator())) {
					if (rowInt > maxRow)
						break;
					int col = columnIndices.get(field);
					if (col == -1)
						continue;
					Cell cell = row.getCell(col, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					
					Class<?> fieldType = field.getType();
					if(fieldType.isPrimitive()) {
						if(fieldType == byte.class) {
							field.setByte(list.get(rowInt - 1), (byte) cell.getNumericCellValue());
						} else if(fieldType == short.class) {
							
						} else if(fieldType == int.class) {
							
						} else if(fieldType == long.class) {
							
						} else if(fieldType == float.class) {
							
						} else if(fieldType == double.class) {
							
						} else if(fieldType == char.class) {
							
						} else if(fieldType == boolean.class) {
							
						}
					} else {
						System.err.println("FieldTable cannot read non primitive data at this time!");
					}
					
					rowInt++;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return list;
	}

	public static int findCol(Row row, String target) {
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i, MissingCellPolicy.RETURN_BLANK_AS_NULL);
			if (cell != null) {
				try {
					String s = cell.getStringCellValue();
					if (s.equalsIgnoreCase(target)) {
						return cell.getColumnIndex();
					}
				} catch (Exception e) {
					// We dont care if getStringCellValue() throws an exception
				}
			}
		}
		return -1;
	}

	public static class MyIterable<T> implements Iterable<T> {

		private Iterator<T> iterator;

		public MyIterable(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		@Override
		public Iterator<T> iterator() {
			return iterator;
		}

	}

}
