package com.troy.labgrader;

public class Utils {
	/**
	 * Returns the integer representation of the number or {@link Integer#MIN_VALUE} if the string does not represent a valud int
	 * @param string The string to parse
	 * @return The string in an int form
	 */
	public static int getInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return Integer.MIN_VALUE;
		}
	}
}
