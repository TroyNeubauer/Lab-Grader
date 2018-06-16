package com.troy.labgrader;

import java.io.File;

import com.troy.labgrader.email.*;

public class Main {

	public static void main(String[] args) throws Exception {
		EmailScanner scanner = new EmailScanner(new EmailListener() {
			@Override
			public void onEmail(Email email) {
				File file = new File("./test." + FileUtils.EXTENSION);
				FileUtils.saveEmail(file, email);
			}
		});
	}

}
