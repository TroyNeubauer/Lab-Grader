package com.troy.labgrader.lab;

public class Lab {
	private LabData data;
	private LabResults results;

	public Lab(LabData data) {
		this.data = data;
	}

	public Lab(LabData data, LabResults results) {
		this.data = data;
		this.results = results;
	}

	public LabData getData() {
		return data;
	}

	public void setData(LabData data) {
		this.data = data;
	}

	public LabResults getResults() {
		return results;
	}

	public void setResults(LabResults results) {
		this.results = results;
	}

	public boolean hasResults() {
		return results != null;
	}

}
