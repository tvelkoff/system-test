package com.ionep.tasks;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.PackageNameFilter.includePackageNames;
import static org.junit.platform.launcher.EngineFilter.includeEngines;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.UriSelector;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

public class DiscoveryRequestCreator {

	public LauncherDiscoveryRequest toDiscoveryRequest() {
		LauncherDiscoveryRequestBuilder requestBuilder = request();
		requestBuilder.selectors(createExplicitDiscoverySelectors());
		addFilters(requestBuilder);
		requestBuilder.configurationParameters(createConfigurationParameters());
		return requestBuilder.build();
	}

	private List<DiscoverySelector> createExplicitDiscoverySelectors() {
		System.out.println("running createExplicitDiscoverySelectors");
		List<DiscoverySelector> selectors = new ArrayList<>();
//		List<String> selectedClasses = new ArrayList<>();
//		selectedClasses.add("com.ionep.tests.Sample_IT");
//		selectedClasses.stream().map(DiscoverySelectors::selectClass).forEach(selectors::add);
//		List<String> selectedPackages = new ArrayList<>();
//		selectedPackages.add("com.ionep.tests");
//		selectedPackages.stream().map(DiscoverySelectors::selectPackage).forEach(selectors::add);
		URI root = Paths.get("target", "classes").toUri();
		UriSelector rootSelector = DiscoverySelectors.selectUri(root);
		selectors.add(rootSelector);
//		List<String> classpathResources = new ArrayList<>();
//		classpathResources.add("target/classes");
//		classpathResources.stream().map(DiscoverySelectors::selectClasspathResource).forEach(selectors::add);
		selectors.stream().forEach(selector -> System.out.println("selector: " + selector.toString()));
//		System.out.println("selector(0): " + selectors.get(0).toString());
		return selectors;
	}

	private void addFilters(LauncherDiscoveryRequestBuilder requestBuilder) {
		requestBuilder.filters(includePackageNames("^com.ionep.tests$"));
		requestBuilder.filters(includeClassNamePatterns("^(.)*IT$"));
		requestBuilder.filters(includeEngines("system-test"));
	}
	
	private Map<String, String> createConfigurationParameters() {
		Map<String, String> parameters = new HashMap<>();
		return parameters;
	}

}
