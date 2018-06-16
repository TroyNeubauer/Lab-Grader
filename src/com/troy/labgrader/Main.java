package com.troy.labgrader;

import com.troy.labgrader.email.*;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		EmailScanner scanner = new EmailScanner(new EmailListener() {
			
			@Override
			public void onEmail(Email email) {
				//email.downloadAttachments(new File("C:\\Users\\Troy Neubauer\\Desktop\\Email Test"));
			}
		});
	}

}
