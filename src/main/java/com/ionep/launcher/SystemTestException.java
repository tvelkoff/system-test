package com.ionep.launcher;

import lombok.Data;

@Data
public class SystemTestException {

	private Throwable throwable;
	
	public SystemTestException(Throwable throwable) {
		this.throwable = throwable;
	}

	public Throwable getThrowable() {
		return this.throwable;
	}
	
	public String getDescription() {
		return this.getThrowable().getLocalizedMessage();
	}
	
	public String getClassName() {
		return this.getThrowable().getClass().getCanonicalName();
	}

}
