package com.troy.labgrader.email;

public class Student {
	private int period;
	private String name;
	private String email;
	private int id;

	public Student(int period, String name, String email, int id) {
		this.period = period;
		this.name = name;
		this.email = email;
		this.id = id;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Student [period=" + period + ", name=" + name + ", email=" + email + ", id=" + id + "]";
	}

	

}
