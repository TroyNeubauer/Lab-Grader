package com.troy.labgrader;

import java.io.File;

import javax.swing.*;

import org.apache.commons.io.FilenameUtils;

import com.troy.labgrader.email.*;
import com.troy.labgrader.lab.LabGraderFile;
import com.troy.labgrader.ui.*;

public class Main {

	public static Window window;

	public static void main(String[] args) throws Exception {

		Object[] options = { "Open Existing File", "Create New File", "Quit" };

		int result = JOptionPane.showOptionDialog(null, "", "Choose an option", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
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
						if (!FilenameUtils.getExtension(file.getAbsolutePath()).equals(FileUtils.EXTENSION)) {
							file = new File(file.getPath() + '.' + FileUtils.EXTENSION);
						}
						window = new Window(new LabGraderFileViewer(LabGraderFile.createNew(file)));
					} else if (value == JOptionPane.NO_OPTION) {
						main(args);
						return;
					} else {
						System.exit(0);
					}
					return;
				}

			}
		} else if (result == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}

		EmailScanner scanner = new EmailScanner(new EmailListener() {

			@Override
			public void onEmail(Email email) {
				FileUtils.saveEmail(email);
			}
		});

	}
}
