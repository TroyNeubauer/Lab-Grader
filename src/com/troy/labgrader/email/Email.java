package com.troy.labgrader.email;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.*;
import org.joda.time.LocalDateTime;

import com.troy.labgrader.FileUtils;

public class Email {
	private static Logger logger = LogManager.getLogger();

	private String from, subject;
	private Message message;
	private String body;
	private List<MimeBodyPart> attachments;
	private LocalDateTime timeRecieved;
	private long id;

	private Email(Message message, String body, List<MimeBodyPart> attachments, LocalDateTime timeRecieved) {
		this.message = message;
		this.body = body;
		this.attachments = attachments;
		this.id = FileUtils.getAndIncrementNumberOfEmails();
		this.timeRecieved = timeRecieved;
		try {
			this.from = EmailUtils.getEmail(message.getFrom()[0]);
			this.subject = message.getSubject();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getText() {
		return body;
	}

	public File[] downloadAttachments(File parentDir) {
		File[] files = new File[attachments.size()];
		int i = 0;
		parentDir.mkdirs();
		for (MimeBodyPart part : attachments) {
			FileOutputStream out;
			try {
				File file = new File(parentDir, part.getFileName());
				files[i++] = file;
				out = new FileOutputStream(file);
				IOUtils.copy(part.getInputStream(), out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return files;
	}

	public static Email fromMessage(Message message) {
		String body;
		List<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();
		Date recievedDate;
		try {
			recievedDate = message.getReceivedDate();
		} catch (MessagingException e1) {
			throw new RuntimeException(e1);
		}
		try {
			String contentType = message.getContentType();

			if (contentType.contains("multipart")) {
				// content may contain attachments
				MimeMultipart multiPart = (MimeMultipart) message.getContent();
				int numberOfParts = multiPart.getCount();
				for (int partCount = 0; partCount < numberOfParts; partCount++) {
					MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
					if ((part.getDisposition() != null) && part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
						attachments.add(part);
					}
				}
				body = EmailUtils.getTextFromMimeMultipart(multiPart);

			} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
				Object content = message.getContent();
				if (content != null) {
					body = content.toString();
				} else {
					body = "null";
				}
			} else {
				logger.warn("unknown contnent type: " + contentType);
				body = "";
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new Email(message, body, attachments, new LocalDateTime(recievedDate));
	}

	public void reply(String body, boolean delete) {
		try {
			Message reply = message.reply(true);
			reply.setText(body);
			Transport.send(reply);
			if (delete)
				reply.setFlag(Flags.Flag.DELETED, true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public Message getMessage() {
		return message;
	}

	public String getSubject() {
		return subject;
	}

	public String getFrom() {
		return from;
	}

	public String getBody() {
		return body;
	}

	public long getId() {
		return id;
	}

	public void makeDefault() {
		body = "Test body!!!\n#period=5\n\n#first_name=TroY\n#last_name=Neubauer\ncan you figure it out?";
		from = "troyneubauer@gmail.com";
		subject = "lab";
		timeRecieved = LocalDateTime.now();
	}
	
	public LocalDateTime getTimeRecieved() {
		return timeRecieved;
	}
}
