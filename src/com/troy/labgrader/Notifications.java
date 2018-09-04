package com.troy.labgrader;

import java.awt.*;

import javax.swing.*;

public class Notifications {
	public static void showNotification(String title, String body) {
		String message = "You got a new notification message. Isn't it awesome to have such a notification message.";
		String header = "This is header of notification message";
		JFrame frame = new JFrame();
		frame.setSize(300, 125);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel headingLabel = new JLabel(header);
		//headingLabel.setIcon(headingIcon); // --- use image icon you want to be as heading image.
		headingLabel.setOpaque(false);
		frame.add(headingLabel, constraints);
		constraints.gridx++;
		constraints.weightx = 0f;
		constraints.weighty = 0f;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.NORTH;
		JButton cloesButton = new JButton("X");
		cloesButton.setMargin(new Insets(1, 4, 1, 4));
		cloesButton.setFocusable(false);
		frame.add(cloesButton, constraints);
		cloesButton.addActionListener((e) -> {
			frame.dispose();
		});
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel messageLabel = new JLabel("<HtMl>" + message);
		frame.add(messageLabel, constraints);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setFocusable(false);
		
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
		frame.pack();
		frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());
		
		frame.setVisible(true);
		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				throw new RuntimeException(e1);
			}
			frame.dispose();
		});
		thread.start();
	}
}
