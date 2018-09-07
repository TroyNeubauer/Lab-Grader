package com.troy.labgrader.ui;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.troy.labgrader.*;

public class Menu extends JMenuBar {

	@SuppressWarnings("restriction")
	public Menu(Window window) {
		JMenu file = new JMenu("File");
		ImageIcon openIcon = null, newIcon = null;
		try {
			openIcon = new ImageIcon(ImageIO.read(Class.class.getResourceAsStream("/icons/open.png")));
			// closeIcon = new ImageIcon(ImageIO.read(Class.class.getResourceAsStream("/icons/close.png")));
			// newIcon = new ImageIcon(ImageIO.read(Class.class.getResourceAsStream("/icons/new.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JMenuItem newItem = new JMenuItem("New", newIcon);
		newItem.addActionListener((ActionEvent e) -> {
			
			try {
				MiscUtil.runClass(Main.class, new ProcessBuilder(), new String[]{"1"});//1 is new
			} catch (Exception e1) {
				if (MiscUtil.isUnsafeSupported())
					MiscUtil.getUnsafe().throwException(e1);
				else
					throw new RuntimeException(e1);
			}
		});
		file.add(newItem);
		
		JMenuItem openItem = new JMenuItem("Open", openIcon);
		openItem.addActionListener((ActionEvent e) -> {
			
			try {
				MiscUtil.runClass(Main.class, new ProcessBuilder(), new String[]{"0"});//0 is open
			} catch (Exception e1) {
				if (MiscUtil.isUnsafeSupported())
					MiscUtil.getUnsafe().throwException(e1);
				else
					throw new RuntimeException(e1);
			}
		});
		file.add(openItem);

		add(file);

	}
}
