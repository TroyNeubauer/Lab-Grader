package com.troy.labgrader.ui;

import java.util.Properties;

import org.jdatepicker.impl.*;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;

import com.troy.labgrader.DateTimeModel;

public class MyJDatePicker extends JDatePickerImpl {
	private static final Properties DEFAULT_PROPS;

	static {
		DEFAULT_PROPS = new Properties();
		DEFAULT_PROPS.put("text.today", "Today");
		DEFAULT_PROPS.put("text.month", "Month");
		DEFAULT_PROPS.put("text.year", "Year");
	}

	public MyJDatePicker() {
		super(new JDatePanelImpl(new DateTimeModel(), DEFAULT_PROPS), null);// 11/26/2018
		getModel().addChangeListener((e) -> {
			super.getJFormattedTextField().setText(getModel().getValue().toString(DateTimeFormat.mediumDate()));
		});
	}

	public LocalDate getDate() {
		return (LocalDate) getModel().getValue();
	}

	public LocalDateTime getDate(LocalTime time) {
		return getDate().toDateTime(time).toLocalDateTime();

	}

	public void setDate(LocalDate dateTime) {
		getModel().setValue(dateTime);
	}

	@Override
	public DateTimeModel getModel() {
		return (DateTimeModel) super.getModel();
	}

}
