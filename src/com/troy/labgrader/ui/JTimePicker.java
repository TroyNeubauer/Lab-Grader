package com.troy.labgrader.ui;

import java.text.SimpleDateFormat;
import org.joda.time.LocalTime;
import java.util.*;

import javax.swing.JComboBox;

public class JTimePicker extends JComboBox<String> {

	private HashMap<String, LocalTime> times = new HashMap<String, LocalTime>();

	public JTimePicker() {
		this(0, 0, 23, 59, 30);
	}

	public JTimePicker(int minHour, int minMinute, int maxHour, int maxMinute, int minuteIncrement) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
		for (int totalMin = minMinute + minHour * 60; totalMin < maxHour * 60 + maxMinute; totalMin += minuteIncrement) {
			String time = format.format(toCalendar(totalMin).getTime());
			times.put(time, new LocalTime(totalMin / 60, totalMin % 60));
			addItem(time);
		}
	}

	private Calendar toCalendar(int minutes) {
		Calendar c = Calendar.getInstance();
		c.set(0, 0, 0, minutes / 60, minutes % 60);
		return c;
	}

	/**
	 * Returns the number of minutes from 12am or 00:00.
	 */
	public LocalTime getSelection() {
		int index = getSelectedIndex();
		if (index != -1)
			return times.get(getModel().getElementAt(index));
		else
			return null;
	}

	public void setToNow() {
		LocalTime date = LocalTime.now();
		setTo(date.getHourOfDay(), date.getMinuteOfHour());
	}

	public void setTo(int hours, int mins) {
		LocalTime toHit = new LocalTime(hours, mins);
		boolean found = false;
		for (int i = 0; i < getItemCount() - 1; i++) {
			LocalTime currentTime = times.get(getItemAt(i));
			LocalTime nextTime = times.get(getItemAt(i + 1));
			if ((currentTime.isBefore(toHit) || currentTime.equals(toHit)) && nextTime.isAfter(toHit)) {
				setSelectedIndex(i);
				found = true;
				break;
			}
		}
		if (!found && getItemCount() > 0) {
			setSelectedIndex(0);
		}

	}

	public void setTime(LocalTime time) {
		setTo(time.getHourOfDay(), time.getMinuteOfHour());
	}
}
