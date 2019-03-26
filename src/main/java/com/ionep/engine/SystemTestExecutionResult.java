package com.ionep.engine;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;

import com.ionep.launcher.SystemTestException;

import lombok.Data;

@Data
public class SystemTestExecutionResult {

	private TestExecutionResult testExecutionResult;
	
	private LocalDateTime started;
	
	private LocalDateTime finished;
	
	private TestIdentifier testIdentifier;
	
	private SystemTestException testException;
	
	private ReportEntry reportEntry;
	
	public void start(TestIdentifier testIdentifier) {
		this.testIdentifier = testIdentifier;
		this.started = LocalDateTime.now();
	}
	
	public void finish(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
		if (this.testIdentifier.getUniqueId().equals(testIdentifier.getUniqueId())) {
			this.testExecutionResult = testExecutionResult;
			this.finished = LocalDateTime.now();
			Optional<Throwable> exception = testExecutionResult.getThrowable();
			this.testException = exception.isPresent() ? new SystemTestException(exception.get()) : null;
		}
	}
	
	public void setReportEntry(ReportEntry reportEntry) {
		this.reportEntry = reportEntry;
	}
	
	public ReportEntry getReportEntry() {
		return this.reportEntry;
	}
	
	public LocalDateTime getStarted() {
		return this.started;
	}
	
	public LocalDateTime getFinished() {
		return this.finished;
	}
	
	public Duration getDuration() {
		return Duration.between(getStarted(), getFinished());
	}
	
	public SystemTestException getTestException() {
		return this.testException;
	}
	
	public Status getStatus() {
		return this.testExecutionResult.getStatus();
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.testIdentifier.getUniqueId() + ": ");
//		buf.append(this.testExecutionResult.getStatus() + ", ");
		buf.append(this.reportEntry.toString());
		return buf.toString();
	}

}
