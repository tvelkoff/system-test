package com.ionep.listeners;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import com.ionep.engine.SystemTestExecutionResult;

public class SystemTestListener implements TestExecutionListener {

	private TestPlan testPlan;
	
	private Map<String, SystemTestExecutionResult> testResults = new HashMap<>();
	
	private LocalDateTime started;
	
	private LocalDateTime finished;
	
	private BufferedWriter writer;

	@Override
	public void testPlanExecutionStarted(TestPlan testPlan) {
		System.out.println("running testPlanExecutionStarted");
		this.testPlan = testPlan;
		System.out.println("has tests? " + this.testPlan.containsTests());
		this.started = LocalDateTime.now();
		String dateString = DateTimeFormatter.BASIC_ISO_DATE.format(this.started);
		String timeString = DateTimeFormatter.ISO_LOCAL_TIME.format(this.started).replaceAll(":", "");
		try {
			this.writer = new BufferedWriter(new FileWriter(Paths.get("target/results." + dateString + "." + timeString + ".txt").toFile()));
			this.writer.write("Started : " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(this.started) + "\n");
		}
		catch (IOException ex) {
			
		}
	}

	@Override
	public void testPlanExecutionFinished(TestPlan testPlan) {
		System.out.println("running testPlanExecutionFinished");
		this.finished = LocalDateTime.now();
		try {
			this.writer.write("Finished: " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(this.finished) + "\n");
			this.writer.close();
		}
		catch (IOException ex) {
			
		}
	}

	@Override
	public void executionStarted(TestIdentifier testIdentifier) {
		System.out.println("running executionStarted");
		SystemTestExecutionResult testResult = new SystemTestExecutionResult();
		testResult.start(testIdentifier);
		this.testResults.put(testIdentifier.getUniqueId(), testResult);
	}

	@Override
	public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
		System.out.println("running executionFinished");
		if (this.testResults.containsKey(testIdentifier.getUniqueId())) {
			SystemTestExecutionResult testResult = this.testResults.get(testIdentifier.getUniqueId());
			testResult.finish(testIdentifier, testExecutionResult);
		}
	}

	public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
		if (this.testResults.containsKey(testIdentifier.getUniqueId())) {
			SystemTestExecutionResult testResult = this.testResults.get(testIdentifier.getUniqueId());
			testResult.setReportEntry(entry);
			try {
				this.writer.write("----------------------------------------------------------------\n");
				Map<String, String> attributes = testResult.getReportEntry().getKeyValuePairs();
				this.writer.write(formatClassName(attributes.get("TestClass")) + "." + attributes.get("TestMethod") + "\n");
				this.writer.write("NumberItemsReturned: " + attributes.get("NumberItemsReturned") + "\n");
				attributes.keySet().stream().filter(s -> s.startsWith("XPath:")).forEach(key -> 
				{
					try {
						this.writer.write(formatXpath(attributes.get(key)) + "\n");
					}
					catch (IOException ex) {
						
					}
				});
			}
			catch (IOException ex) {
				
			}
		}
	}
	
	protected static String formatClassName(String name) {
		int lastPeriod = name.lastIndexOf(".");
		return name.substring(lastPeriod + 1);
	}

	protected static String formatXpath(String xpath) {
		return xpath.substring("XPath assertion failure - ".length());
	}

}
