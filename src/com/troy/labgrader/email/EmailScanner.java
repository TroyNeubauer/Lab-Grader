package com.troy.labgrader.email;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.mail.*;

import org.apache.logging.log4j.*;

public class EmailScanner implements Runnable {
	private static final Logger logger = LogManager.getLogger();

	private AtomicBoolean running = new AtomicBoolean(false);

	private final Thread thread;

	private Session session;
	private Store store;
	private Folder inbox, archive;
	private EmailListener listener;

	private static String host = "pop.gmail.com", port = "995", userName = "autolaulabs@gmail.com", password = "csisfun!!";

	public EmailScanner(EmailListener listener) {
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
		properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
		logger.trace("Creating email session...");
		session = Session.getDefaultInstance(properties);

		try {
			store = session.getStore("pop3");
			store.connect(userName, password);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.fatal("Unable to connect to the pop3 store!!!");
			logger.catching(Level.FATAL, e);
		}
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
		

	}

	private void scan() {
		try {
			logger.trace("About to get messages");
			Message[] messages = inbox.getMessages();
			logger.trace("Done getting messages");

			for (int i = 0; i < messages.length; i++) {
				logger.info("From: " + messages[i].getFrom());
				logger.info("Subject: " + messages[i].getSubject());
				logger.info("X-mailer: " + messages[i].getHeader("X-mailer"));
				listener.onEmail(Email.fromMessage(messages[i]));
			}
		} catch (Exception e) {
			logger.warn("Exception occurred!");
			logger.catching(Level.WARN, e);
		}
	}

	private void close() {
		try {
			inbox.close(true);
			archive.close(true);
			store.close();
		} catch (Exception e) {
			logger.catching(Level.WARN, e);
		}
	}

	public void stop() {
		running.set(false);
	}

}
