package com.ionep.launcher;

import java.util.Optional;

import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class SystemTestLauncherExecutionResult {

	/**
	 * Exit code indicating successful execution
	 */
	private static final int SUCCESS = 0;

	/**
	 * Exit code indicating test failure(s)
	 */
	private static final int TEST_FAILED = 1;

	/**
	 * Exit code indicating any failure(s)
	 */
	private static final int FAILED = -1;

	public static int computeExitCode(TestExecutionSummary summary) {
		return summary.getTotalFailureCount() == 0 ? SUCCESS : TEST_FAILED;
	}

	static SystemTestLauncherExecutionResult success() {
		return new SystemTestLauncherExecutionResult(SUCCESS);
	}

	static SystemTestLauncherExecutionResult failed() {
		return new SystemTestLauncherExecutionResult(FAILED);
	}

	static SystemTestLauncherExecutionResult forSummary(TestExecutionSummary summary) {
		return new SystemTestLauncherExecutionResult(summary);
	}

	private final int exitCode;
	private final TestExecutionSummary testExecutionSummary;

	private SystemTestLauncherExecutionResult(int exitCode) {
		this.exitCode = exitCode;
		this.testExecutionSummary = null;
	}

	private SystemTestLauncherExecutionResult(TestExecutionSummary testExecutionSummary) {
		this.testExecutionSummary = testExecutionSummary;
		this.exitCode = computeExitCode(testExecutionSummary);
	}

	public int getExitCode() {
		return exitCode;
	}

	public Optional<TestExecutionSummary> getTestExecutionSummary() {
		return Optional.ofNullable(testExecutionSummary);
	}

}
