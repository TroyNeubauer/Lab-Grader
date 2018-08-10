package com.troy.labgrader.ui;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Menu extends JMenuBar {
	
	public Menu(Pane pane) {
		JMenu file = new JMenu("File");
		ImageIcon openIcon = null, closeIcon = null;
		try {
			openIcon = new ImageIcon(ImageIO.read(Class.class.getResourceAsStream("/icons/open.png")));
			closeIcon = new ImageIcon(ImageIO.read(Class.class.getResourceAsStream("/icons/close.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JMenuItem open = new JMenuItem("Open", openIcon);
		open.addActionListener((ActionEvent e) -> {
			pane.showOpenDialog();
		});
		
		JMenuItem close = new JMenuItem("Close", closeIcon);
		close.addActionListener((ActionEvent e) -> {
			pane.closeSelectedFile();
		});
		file.add(open);
		file.add(close);
		
		add(file);
		
	}
}
