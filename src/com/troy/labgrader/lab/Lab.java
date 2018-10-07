package com.troy.labgrader.lab;

import java.util.*;

public class Lab {
	private Date open, close;
	private String name, output;

	public Lab() {

	}

	public Lab(Date open, Date close, String name, String output) {
		super();
		this.open = open;
		this.close = close;
		this.name = name;
		this.output = output;
	}

	public Date getOpen() {
		return open;
	}

	public void setOpen(Date open) {
		this.open = open;
	}

	public Date getClose() {
		return close;
	}

	public void setClose(Date close) {
		this.close = close;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "Lab [open=" + open + ", close=" + close + ", name=" + name + ", output=" + output + "]";
	}
	
	

}
