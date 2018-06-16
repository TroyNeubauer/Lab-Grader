package com.troy.labgrader;

import java.util.Locale;

public enum OSType {
	Windows, Mac, Linux, Other;

	// cached result of OS detection
	protected static final OSType OS = determineOS();

	/**
	 * detect the operating system from the os.name System property and cache the result
	 * 
	 * @returns - the operating system detected
	 */
	public static OSType get() {
		return OS;
	}

	private static OSType determineOS() {
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
			return OSType.Mac;
		} else if (OS.indexOf("win") >= 0) {
			return OSType.Windows;
		} else if (OS.indexOf("nux") >= 0) {
			return OSType.Linux;
		} else {
			return OSType.Other;
		}
	}
}
