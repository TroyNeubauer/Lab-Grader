package com.troy.labgrader;

import javax.swing.UIManager;

import com.troy.labgrader.email.*;
import com.troy.labgrader.ui.Window;

public class Main {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		Window window = new Window();
				
		EmailScanner scanner = new EmailScanner(new EmailListener() {
			@Override
			public void onEmail(Email email) {
				FileUtils.saveEmail(email);
			}
		});
	}
}
