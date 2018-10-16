package com.troy.labgrader.ui;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JComboBox;

public class JTimePicker extends JComboBox {

	private HashMap<String, Integer> times = new HashMap<String, Integer>();

	public JTimePicker() {
		this(0, 0, 23, 59, 30);
	}

	public JTimePicker(int minHour, int minMinute, int maxHour, int maxMinute, int minuteIncrement) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
		for (int totalMin = minMinute + minHour * 60; totalMin <= maxHour * 60 + maxMinute; totalMin += minuteIncrement) {
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

	/**
	 * Returns the number of minutes from 12am or 00:00.
	 */
	public int getSelection() {
		int index = getSelectedIndex();
		if (index != -1)
			return times.get(getModel().getElementAt(index));
		else
			return -1;
	}

	public void setToNow() {
		Calendar c = Calendar.getInstance();
		setTo(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
	}

	public void setTo(int hours, int mins) {
		String bestStr = null;
		int best = -1;
		int nowMinutes = hours * 60 + mins;
		for (Entry<String, Integer> entry : times.entrySet()) {
			int lastDiff = nowMinutes - best, nowDif = nowMinutes - entry.getValue();
			if (best == -1 || (lastDiff < 0 && nowDif >= 0) || (nowDif < lastDiff && nowDif >= 0)) {
				best = entry.getValue();
				bestStr = entry.getKey();
			}
		}
		if (best != -1) {
			setSelectedItem(bestStr);
		}
	}

	public void setTime(Date close) {
		setTo(close.getHours(), close.getMinutes());
	}
}
