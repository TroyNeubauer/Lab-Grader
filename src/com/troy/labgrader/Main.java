package com.troy.labgrader;

import java.io.File;
import java.util.UUID;

import com.troy.labgrader.email.*;
import com.troy.labgrader.lab.LabGraderFile;
import com.troy.labgrader.ui.Window;

public class Main {

	public static void main(String[] args) throws Exception {
		Window window = new Window();
				
		EmailScanner scanner = new EmailScanner(new EmailListener() {
			@Override
			public void onEmail(Email email) {
				LabGraderFile file = new LabGraderFile(new File("./test." + FileUtils.EXTENSION));
				FileUtils.saveEmail(email);
			}
		});
	}
}
