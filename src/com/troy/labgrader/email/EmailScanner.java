package com.troy.labgrader.email;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.mail.*;

import org.apache.logging.log4j.*;

public class EmailScanner implements Runnable {
	private static final Logger logger = LogManager.getLogger();

	private AtomicBoolean running = new AtomicBoolean(true);

	private final Thread thread;

	private Session session;
	private Store store;
	private Folder inbox, archive;
	private EmailListener listener;

	private static String host = "pop.gmail.com", port = "995", userName = "autolaulabs@gmail.com", password = "csisfun!!";

	public EmailScanner(EmailListener listener) {
		this.listener = listener;
		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);

		thread.start();
	}

	@Override
	public void run() {
		logger.debug("Starting scanner thread");
		init();
		while (running.get()) {
			scan();
		}
		close();
		logger.debug("Ending scanner thread");
	}

	private void init() {
		Properties properties = new Properties();

		// server setting
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);

		// SSL setting
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.socketFactory.port", port);

		// properties.setProperty("mail.smtp.ssl.enable", "true");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "587");
		logger.trace("Creating email session...");
		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
		
		try {
			store = session.getStore("pop3");
			store.connect(userName, password);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.fatal("Unable to connect to the pop3 store!!!");
			logger.catching(Level.FATAL, e);
		}
	}

	private void scan() {
		try {
			if (inbox != null)
				inbox.close(true);
			try {
				inbox = store.getFolder("Inbox");
				if (!inbox.exists())
					logger.warn("Inbox folder doesn't exist!");
				inbox.open(Folder.READ_WRITE);
			} catch (MessagingException e) {
				e.printStackTrace();
				logger.fatal("Unable to get the inbox folder!");
				logger.catching(Level.FATAL, e);
			}

			Message[] messages = inbox.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				Address[] from = message.getFrom();
				if(from.length == 1) {
					if(from[0].toString().equals(userName))
						continue;
				}
				logger.info("Message #" + (i + 1));
				logger.info("From: " + Arrays.toString(message.getFrom()));
				logger.info("Subject: " + message.getSubject());
				Email email = Email.fromMessage(message);
				email.reply("LAB SUBMISSION FAILURE\nLab failed to compile!\n\nPlease re-submit a working lab", true);
				listener.onEmail(email);
				message.setFlag(Flags.Flag.DELETED, true);
			}
		} catch (Exception e) {
			logger.warn("Exception occurred!");
			logger.catching(Level.WARN, e);
		}
	}

	private void close() {
		try {
			inbox.close(true);
			// archive.close(true);
			store.close();
		} catch (Exception e) {
			logger.catching(Level.WARN, e);
		}
	}

	public void stop() {
		running.set(false);
	}

}
