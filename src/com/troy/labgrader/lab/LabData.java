package com.troy.labgrader.lab;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

public class LabData {

	private String name, output;
	private LocalDateTime open, close;

	public LabData() {
		this.name = "<blank>";
		this.output = "<blank>";
		this.open = LocalDateTime.now();
		this.close = LocalDateTime.now();
	}

	public LabData(LocalDateTime open, LocalDateTime close, String name, String output) {
		super();
		this.open = open;
		this.close = close;
		this.name = name;
		this.output = output;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((close == null) ? 0 : close.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LabData other = (LabData) obj;
		if (close == null) {
			if (other.close != null)
				return false;
		} else if (!close.equals(other.close))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (output == null) {
			if (other.output != null)
				return false;
		} else if (!output.equals(other.output))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Lab [name=" + name + ", output=" + output + ", open=" + open.toString(DateTimeFormat.fullDateTime()) + ", close=" + close.toString(DateTimeFormat.fullDateTime()) + "]";
	}

	public LocalDateTime getOpen() {
		return open;
	}

	public void setOpen(LocalDateTime open) {
		this.open = open;
	}

	public LocalDateTime getClose() {
		return close;
	}

	public void setClose(LocalDateTime close) {
		this.close = close;
	}

	public void periodsUpdated(Course course, Year year) {
		
	}

}
