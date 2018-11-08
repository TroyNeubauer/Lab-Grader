package com.troy.labgrader.email;

public class EmailRejectedException extends RuntimeException {
	private String reason;

	public EmailRejectedException(String reason) {
		super(reason);// might be redundant but it makes it easier
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
