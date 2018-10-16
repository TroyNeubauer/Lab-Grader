package com.troy.labgrader.ui;

import java.text.*;
import java.util.*;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.impl.*;

public class MyJDatePicker extends JDatePickerImpl {
	private static final Properties DEFAULT_PROPS;
	private static final String DATE_FORMAT = "MM/dd/yyyy";
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

	static {
		DEFAULT_PROPS = new Properties();
		DEFAULT_PROPS.put("text.today", "Today");
		DEFAULT_PROPS.put("text.month", "Month");
		DEFAULT_PROPS.put("text.year", "Year");
	}

	public MyJDatePicker() {
		super(new JDatePanelImpl(new UtilDateModel(), DEFAULT_PROPS), new DateLabelFormatter());
		setDate(Calendar.getInstance());
		
	}

	public Date getDate(int totalMins) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, getModel().getYear());
		c.set(Calendar.MONTH, getModel().getMonth());
		c.set(Calendar.DAY_OF_MONTH, getModel().getDay());
		c.set(Calendar.HOUR_OF_DAY, totalMins / 60);
		c.set(Calendar.MINUTE, totalMins % 60);
		return c.getTime();
	}

	public void setDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		setDate(c);
	}
	
	public void setDate(Calendar c) {		
		getModel().setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		super.getJFormattedTextField().setText(dateFormatter.format(c.getTime()));
	}

	public static class DateLabelFormatter extends AbstractFormatter {

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}

			return "";
		}

	}

}
