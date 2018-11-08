package com.troy.labgrader;

import java.io.File;
import java.util.*;

import javax.swing.*;

import com.troy.labgrader.email.Email;
import com.troy.labgrader.lab.*;
import com.troy.labgrader.ui.*;

public class Main {

	public static Window window;

	public static void main(String[] args) throws InstantiationException {

		LabGraderFile f = new LabGraderFile(new File("C:\\Users\\Troy Neubauer\\Documents\\test.troygrade"));
		List<Lab> labs = new ArrayList<Lab>();
		for (Year year : f.getYears()) {
			for (Course course : year.getCourses()) {
				for (Lab l : course.getLabs()) {
					labs.add(l);
				}
			}
		}
		Email email = (Email) MiscUtil.getUnsafe().allocateInstance(Email.class);
		email.makeDefault();
		EmailDecoder.narrowDownLab(f.getYears().get(0), labs, f.getYears().get(0).getStudents(), email);
		System.exit(0);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			throw new RuntimeException(e1);
		}

		try {
			int result;
			if (args.length == 1) {
				result = MiscUtil.getIntOrDefaultValue(args[0], "Invalid argument. Must be integer", Integer.MIN_VALUE);
				if (result == Integer.MIN_VALUE)
					System.exit(0);
			} else {
				Object[] options = { "Open Existing File", "Create New File", "Quit" };
				result = JOptionPane.showOptionDialog(null, "", "Choose an option", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
			}
			if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
				if (result == JOptionPane.YES_OPTION) {// open
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("Open File");
					chooser.setFileFilter(new FileUtils.MyFileFilter());
					chooser.showOpenDialog(null);
					File file = chooser.getSelectedFile();
					if (file == null)
						System.exit(0);
					window = new Window(new LabGraderFileViewer(file));
				} else {// create new
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("Save New File");
					chooser.setFileFilter(new FileUtils.MyFileFilter());
					chooser.showOpenDialog(null);
					File file = chooser.getSelectedFile();
					if (file == null)
						System.exit(0);
					if (file.exists() && file.isFile()) {
						int value = JOptionPane.showOptionDialog(null, "Do you want to override this file?", file.getName() + " already exists!", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
								null, null, null);
						if (value == JOptionPane.CANCEL_OPTION)
							System.exit(0);
						else if (value == JOptionPane.YES_OPTION) {
							file = Utils.setExtention(file, FileUtils.EXTENSION);
							window = new Window(new LabGraderFileViewer(LabGraderFile.createNew(file)));
						} else if (value == JOptionPane.NO_OPTION) {
							MiscUtil.runClass(Main.class);
							return;
						} else {
							System.exit(0);
						}
						return;
					}
				}
			} else {// Includes cancel and all other cases
				System.exit(0);
			}
			/*
			 * EmailScanner scanner = new EmailScanner(new EmailListener() {
			 * 
			 * @Override public void onEmail(Email email) { try { FileUtils.saveEmail(email); } catch (Exception e) { Utils.showError(e,
			 * "Unable to save email!"); } } });
			 */
		} catch (Exception e) {
			Utils.showError(e);
		}

	}

	private static void letters(char c) {
	}
}
