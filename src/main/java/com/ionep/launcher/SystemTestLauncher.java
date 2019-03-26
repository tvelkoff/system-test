package com.ionep.launcher;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.junit.platform.launcher.listeners.TestExecutionSummary;

import com.ionep.tasks.SystemTestExecutor;

public class SystemTestLauncher {

	private final PrintStream outStream;
	private final PrintStream errStream;
	private final Charset charset;

	public static SystemTestLauncherExecutionResult execute(PrintStream out, PrintStream err, String... args) {
		SystemTestLauncher systemTestLauncher = new SystemTestLauncher(out, err);
		return systemTestLauncher.execute(args);
	}

	SystemTestLauncher(PrintStream out, PrintStream err) {
		this(out, err, Charset.defaultCharset());
	}

	SystemTestLauncher(PrintStream out, PrintStream err, Charset charset) {
		this.outStream = out;
		this.errStream = err;
		this.charset = charset;
	}

	SystemTestLauncherExecutionResult execute(String... args) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outStream, charset)))) {
			return executeTests(out);
		}
		finally {
			outStream.flush();
			errStream.flush();
		}
	}

	private SystemTestLauncherExecutionResult executeTests(PrintWriter out) {
		try {
			System.out.println("running executeTests");
			TestExecutionSummary testExecutionSummary = new SystemTestExecutor().execute(out);
			return SystemTestLauncherExecutionResult.forSummary(testExecutionSummary);
		}
		catch (Exception exception) {
			exception.printStackTrace(errStream);
			errStream.println();
		}
		return SystemTestLauncherExecutionResult.failed();
	}

}
