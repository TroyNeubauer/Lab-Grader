package com.troy.labgrader;

import java.awt.Component;

import javax.swing.JOptionPane;

public class Utils {
	
	public static final int INVALID_STRING = Integer.MIN_VALUE;
	
	
	
	/**
	 * Returns the integer representation of the number or {@link Integer#MIN_VALUE} if the string does not represent a valud int
	 * 
	 * @param string The string to parse
	 * @return The string in an int form
	 */
	public static int getInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return INVALID_STRING;
		}
	}

	public static String getUserString(Component master, String message, String title, int messageType) {
		return (String) JOptionPane.showInputDialog(master, message, title, messageType, null, null, null);
	}
}
