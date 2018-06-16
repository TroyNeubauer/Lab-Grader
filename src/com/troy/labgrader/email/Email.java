package com.troy.labgrader.email;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.*;

import de.schlichtherle.truezip.file.*;

public class Email {
	private static Logger logger = LogManager.getLogger();

	private Message message;
	private String body;
	private List<MimeBodyPart> attachments;

	private Email(Message message, String body, List<MimeBodyPart> attachments) {
		this.message = message;
		this.body = body;
		this.attachments = attachments;
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

	public TFile[] downloadAttachments(TFile parentDir) {
		TFile[] files = new TFile[attachments.size()];
		int i = 0;
		for (MimeBodyPart part : attachments) {
			TFileOutputStream out = null;
			try {
				TFile file = new TFile(parentDir, part.getFileName());
				files[i++] = file;
				out = new TFileOutputStream(file);
				IOUtils.copy(part.getInputStream(), out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				if (out != null)
					try {
						out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		}
		return files;
	}

	public static Email fromMessage(Message message) {
		String body;
		List<MimeBodyPart> attachments = new ArrayList<MimeBodyPart>();
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
		return new Email(message, body, attachments);
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

}
