package com.troy.labgrader.ui;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.jdatepicker.impl.*;

import com.troy.labgrader.DateTimeModel;

public class MyJDatePicker extends JDatePickerImpl {
	private static final Properties DEFAULT_PROPS;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uu");

	static {
		DEFAULT_PROPS = new Properties();
		DEFAULT_PROPS.put("text.today", "Today");
		DEFAULT_PROPS.put("text.month", "Month");
		DEFAULT_PROPS.put("text.year", "Year");
	}

	public MyJDatePicker() {
		super(new JDatePanelImpl(new DateTimeModel(), DEFAULT_PROPS), null);// 11/26/2018
		getModel().addChangeListener((e) -> {
			super.getJFormattedTextField().setText(getModel().getValue().format(formatter));
		});
	}

	public LocalDate getDate() {
		return (LocalDate) getModel().getValue();
	}

	public LocalDateTime getDate(LocalTime time) {
		return time.atDate(getDate());
	}

	public void setDate(LocalDate dateTime) {
		getModel().setValue(dateTime);
	}

	@Override
	public DateTimeModel getModel() {
		return (DateTimeModel) super.getModel();
	}

}
