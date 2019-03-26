package com.ionep.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.runner.TestRunner;

public class Sample_IT {
	
	@Test
	@CitrusTest
	public void hello(@CitrusResource TestRunner runner, @CitrusResource TestContext context, TestInfo testInfo, TestReporter testReporter) throws Exception {
		System.out.println("Hello from JUnit!");
		runner.echo("Hello from Citrus!");
	}

}
