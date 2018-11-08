package com.troy.labgrader.lab;

public class LabResult {
	public LabRunStatus runStatus;
	public double plagiarism;
	public String fromEmail;
	public String stdErr;// Contains the error if it failed to compile
	public String stdOut;
	public int id;

	public LabResult(int id, String fromEmail, LabRunStatus runStatus, double plagerism, String stdErr, String stdOut) {
		this.fromEmail = fromEmail;
		this.runStatus = runStatus;
		this.plagiarism = plagerism;
		this.stdErr = stdErr;
		this.stdOut = stdOut;
		this.id = id;
	}

	public LabResult(int id, String fromEmail, LabRunStatus runStatus, String stdErr) {
		this.fromEmail = fromEmail;
		this.runStatus = runStatus;
		this.plagiarism = Double.NaN;
		this.stdErr = stdErr;
		this.stdOut = "<Compile Time Error>";
		this.id = id;
	}

	public LabResult(int id) {
		this.id = id;
		this.fromEmail = "<Unknown>";
		this.runStatus = LabRunStatus.NOT_SUBMITTED;
		this.plagiarism = Double.NaN;
		this.stdErr = "<Not Yet Run>";
		this.stdOut = "<Not Yet Run>";
	}

}
