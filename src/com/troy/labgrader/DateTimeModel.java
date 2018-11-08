package com.troy.labgrader;

import java.beans.*;
import org.joda.time.LocalDate;
import java.util.*;

import javax.swing.event.*;

import org.jdatepicker.DateModel;

public class DateTimeModel implements DateModel<LocalDate> {

	private LocalDate date = LocalDate.now();
	private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
	private List<PropertyChangeListener> propertyChangeListeners = new ArrayList<PropertyChangeListener>();
	boolean selected = false;

	@Override
	public int getDay() {
		return date.getDayOfMonth();
	}

	@Override
	public void setDay(int day) {
		int oldDayValue = date.getDayOfMonth();
		LocalDate oldValue = getValue();
		addDay(day - getDay());
		fireChangeEvent();
		firePropertyChange("day", oldDayValue, getDay());
		firePropertyChange("value", oldValue, getValue());
	}

	@Override
	public int getMonth() {
		return date.getMonthOfYear() - 1;
	}

	@Override
	public void setMonth(int month) {
		int oldMonthValue = getMonth();
		LocalDate oldValue = getValue();
		this.date = new LocalDate(date.getYear(), month + 1, date.getDayOfMonth());
		fireChangeEvent();
		firePropertyChange("month", oldMonthValue, getMonth());
		firePropertyChange("value", oldValue, getValue());
	}

	@Override
	public int getYear() {
		return date.getYear();
	}

	@Override
	public void setYear(int year) {
		int oldYearValue = getYear();
		LocalDate oldValue = getValue();
		this.date = new LocalDate(year, date.getMonthOfYear(), date.getDayOfMonth());
		fireChangeEvent();
		firePropertyChange("year", oldYearValue, getYear());
		firePropertyChange("value", oldValue, getValue());
	}

	@Override
	public void setDate(int year, int month, int day) {
		int oldYearValue = getYear();
		int oldMonthValue = getMonth();
		int oldDayValue = getDay();
		LocalDate oldValue = getValue();
		this.date = new LocalDate(year, month, day);
		fireChangeEvent();
		firePropertyChange("year", oldYearValue, getYear());
		firePropertyChange("month", oldMonthValue, getMonth());
		firePropertyChange("day", oldDayValue, getDay());
		firePropertyChange("value", oldValue, getValue());
	}

	@Override
	public void addYear(int add) {
		int oldYearValue = getYear();
		LocalDate oldValue = getValue();
		this.date = date.plusYears(add);
		fireChangeEvent();
		firePropertyChange("year", oldYearValue, getYear());
		firePropertyChange("value", oldValue, getValue());
	}

	@Override
	public void addMonth(int add) {
		int oldMonthValue = getMonth();
		LocalDate oldValue = getValue();
		this.date = date.plusMonths(add);
		fireChangeEvent();
		firePropertyChange("month", oldMonthValue, getMonth());
		firePropertyChange("value", oldValue, getValue());
	}

	@Override
	public void addDay(int add) {
		int oldDayValue = date.getDayOfMonth();
		LocalDate oldValue = getValue();
		this.date = date.plusDays(add);
		fireChangeEvent();
		firePropertyChange("day", oldDayValue, getDay());
		firePropertyChange("value", oldValue, getValue());
	}

	@Override
	public void setValue(LocalDate value) {
		int oldYearValue = getYear();
		int oldMonthValue = getMonth();
		int oldDayValue = getDay();
		LocalDate oldValue = getValue();
		boolean oldSelectedValue = isSelected();

		if (value != null) {
			this.date = value;
			selected = true;
		} else {
			selected = false;
		}

		fireChangeEvent();
		firePropertyChange("year", oldYearValue, getYear());
		firePropertyChange("month", oldMonthValue, getMonth());
		firePropertyChange("day", oldDayValue, getDay());
		firePropertyChange("value", oldValue, getValue());
		firePropertyChange("selected", oldSelectedValue, this.selected);
	}

	@Override
	public LocalDate getValue() {
		return date;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.remove(listener);
	}

	protected synchronized void fireChangeEvent() {
		for (ChangeListener changeListener : listeners) {
			changeListener.stateChanged(new ChangeEvent(this));
		}
	}

	protected synchronized void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
			return;
		}

		for (PropertyChangeListener listener : propertyChangeListeners) {
			listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
		}
	}

	@Override
	public void addChangeListener(ChangeListener changeListener) {
		listeners.add(changeListener);
	}

	@Override
	public void removeChangeListener(ChangeListener changeListener) {
		listeners.remove(changeListener);
	}

	public void setToNow() {
		setValue(LocalDate.now());
	}

}
