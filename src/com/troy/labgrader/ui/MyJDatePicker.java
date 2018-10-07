package com.troy.labgrader.ui;

import java.text.*;
import java.util.*;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.impl.*;

public class MyJDatePicker extends JDatePickerImpl {
	private static final Properties DEFAULT_PROPS;

	static {
		DEFAULT_PROPS = new Properties();
		DEFAULT_PROPS.put("text.today", "Today");
		DEFAULT_PROPS.put("text.month", "Month");
		DEFAULT_PROPS.put("text.year", "Year");
	}

	public MyJDatePicker() {
		super(new JDatePanelImpl(new UtilDateModel(), DEFAULT_PROPS), new DateLabelFormatter());
	}

	public static class DateLabelFormatter extends AbstractFormatter {

	    private String datePattern = "MM/dd/yyyy";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

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
