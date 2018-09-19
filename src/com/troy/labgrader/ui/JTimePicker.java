package com.troy.labgrader.ui;

import java.util.HashMap;

import javax.swing.*;

public class JTimePicker extends JList<String> {
	private HashMap<String, Integer> times = new HashMap<String, Integer>();
	
	public JTimePicker(int minHour, int minMinute, int maxHour, int maxMinute, int minuteIncrement) {
		int currentMinute = minMinute;
		while(currentMinute < maxHour * 60 + maxMinute) {
			
		}
	}
}
