package com.troy.labgrader.lab;

public enum LabRunStatus {
	NOT_SUBMITTED(false, false, false, false), NOT_YET_RUN(true, false, false, false), COMPILE_TIME_ERROR(true, false, false, false), RUN_TIME_ERROR(true, true, false, false), RAN_WRONG_OUTPUT(true,
			true, true, false), NOMINAL_EXECUTION(true, true, true, true);

	public final boolean submitted, compiled, ran, correctOut;

	LabRunStatus(boolean submitted, boolean compiled, boolean ran, boolean correctOut) {
		this.submitted = submitted;
		this.compiled = compiled;
		this.ran = ran;
		this.correctOut = correctOut;
	}
}