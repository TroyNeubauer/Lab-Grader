package com.troy.labgrader.ui;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

public class JTimePicker extends JComboBox {

	private HashMap<String, Integer> times = new HashMap<String, Integer>();

	public JTimePicker() {
		this(1, 0, 23, 59, 30);
	}

	public JTimePicker(int minHour, int minMinute, int maxHour, int maxMinute, int minuteIncrement) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");

		for (int totalMin = minMinute; totalMin <= maxHour * 60 + maxMinute; totalMin += minuteIncrement) {
			String time = format.format(toCalendar(totalMin).getTime());
			times.put(time, totalMin);
			addItem(time);
		}
	}

	private Calendar toCalendar(int minutes) {
		Calendar c = Calendar.getInstance();
		c.set(0, 0, 0, minutes / 60, minutes % 60);
		return c;
	}

	public int getSelection() {
		int index = getSelectedIndex();
		if (index != -1)
			return times.get(getModel().getElementAt(index));
		else
			return -1;
	}
}
