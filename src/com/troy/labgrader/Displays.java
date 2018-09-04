package com.troy.labgrader;

public class Displays {

	private static boolean connected = false;

	public static DisplaysListener listener;

	public static final String CONNECTED = "CONNECTED";

	public static boolean isConnected() {
		return connected;
	}

	public static void setConnected(boolean connected) {
		if (Displays.connected != connected) {
			Displays.connected = connected;
			if (listener != null) {
				listener.onUpdate(CONNECTED, Boolean.valueOf(connected));
			}
		}
	}

	public static interface DisplaysListener {
		public void onUpdate(String variable, Object value);
	}

}
