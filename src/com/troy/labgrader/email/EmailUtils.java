package com.troy.labgrader.email;

import java.io.IOException;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;

public class EmailUtils {
	
	public static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + (result.isEmpty() ? "" : "\n") + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result = result + (result.isEmpty() ? "" : "\n") + org.jsoup.Jsoup.parse(html).text();
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}
	
	public static String getEmail(Address email) {
		String raw = email.toString();
		int open = raw.indexOf('<');
		if (open != -1) {
			int close = raw.indexOf('>');
			if (close == -1) {
				throw new RuntimeException("Email: \"" + raw + "\" doesn't have a closing >!");
			}
			return raw.substring(open + 1, close);
		}
		return raw.trim();
	}
}
