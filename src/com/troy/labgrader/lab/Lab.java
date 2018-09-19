package com.troy.labgrader.lab;

import java.util.*;

public class Lab {
	private Date start, end;
	private String name;

	public Lab() {
		name = "";
	}

	public Lab(Date start, Date end, String name) {
		this.start = start;
		this.end = end;
		this.name = name;
	}

}
