package com.troy.labgrader.email;

import javax.mail.Message;

public class Email {
	private Message message;

	public Email(Message message) {
		this.message = message;
	}

	public static Email fromMessage(Message message) {
		return new Email(message);
	}

}
