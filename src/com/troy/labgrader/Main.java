package com.troy.labgrader;

import com.troy.labgrader.email.*;
import com.troy.labgrader.ui.Window;

public class Main {

	public static Window window;

	public static void main(String[] args) throws Exception {
		/*
		 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial",
		 * Font.BOLD, 30)); List<Student> list = new ArrayList(); list.add(new Student(5, "Troy Neubauer", "troyneubauer@gmail.com")); JFrame test = new
		 * JFrame(); test.setLayout(new BorderLayout()); FieldTabel tab = new FieldTabel<Student>(list, Student.class); JTable jtab = new JTable(tab);
		 * test.add(new JScrollPane(jtab), BorderLayout.CENTER); test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); test.setSize(1440, 810);
		 * test.setLocationRelativeTo(null); test.setVisible(true); JButton add = new JButton("add row"); add.addActionListener((e) -> { tab.addRow();
		 * }); test.add(add, BorderLayout.SOUTH);
		 */

		window = new Window();

		EmailScanner scanner = new EmailScanner(new EmailListener() {

			@Override
			public void onEmail(Email email) {
				FileUtils.saveEmail(email);
			}
		});

	}
}
