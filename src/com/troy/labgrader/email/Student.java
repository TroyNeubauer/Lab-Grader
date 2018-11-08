package com.troy.labgrader.email;

public class Student {
	private String name;
	private int id;
	private int period;
	private String email1, email2, email3, email4, email5;

	public Student(String name, int id, int period, String email1) {
		this.name = name;
		this.id = id;
		this.period = period;
		this.email1 = email1;
	}

	public Student(String name, int id, int period, String... emails) {
		this.name = name;
		this.id = id;
		this.period = period;
		if (emails.length > 5) {
			throw new IllegalArgumentException("Too many emails! Only 5 at most");
		}
		if (emails.length >= 1)
			email1 = emails[0];
		if (emails.length >= 2)
			email2 = emails[1];
		if (emails.length >= 3)
			email3 = emails[2];
		if (emails.length >= 4)
			email4 = emails[3];
		if (emails.length >= 5)
			email5 = emails[4];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getEmail4() {
		return email4;
	}

	public void setEmail4(String email4) {
		this.email4 = email4;
	}

	public String getEmail5() {
		return email5;
	}

	public void setEmail5(String email5) {
		this.email5 = email5;
	}

	public boolean hasEmail(String email) {
		return email.equals(email1) || email.equals(email2) || email.equals(email3) || email.equals(email4) || email.equals(email5);
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", id=" + id + ", period=" + period + ", email1=" + email1 + ", email2=" + email2 + ", email3=" + email3 + ", email4=" + email4 + ", email5=" + email5 + "]";
	}
	
	

}
