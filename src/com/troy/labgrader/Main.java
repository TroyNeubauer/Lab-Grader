package com.troy.labgrader;

import java.awt.Font;
import java.util.*;

import javax.swing.*;

import com.troy.labgrader.email.Student;
import com.troy.labgrader.ui.*;

public class Main {
	
	public static Window window;

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial", Font.BOLD, 30));
		List<Student> list = new ArrayList();
		JFrame test = new JFrame();
		test.add(new JTable(new FieldTabel<>(list, Student.class)));
		
		test.setSize(1440, 810);
		test.setLocationRelativeTo(null);
		test.setVisible(true);
		
		/*window = new Window();

		EmailScanner scanner = new EmailScanner(new EmailListener() {
			@Override
			public void onEmail(Email email) {
				FileUtils.saveEmail(email);
			}
		});*/
	}
}
