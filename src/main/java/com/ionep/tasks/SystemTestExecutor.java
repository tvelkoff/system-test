package com.ionep.tasks;

import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.util.ClassLoaderUtils;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.PackageNameFilter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import com.ionep.listeners.SystemTestListener;

public class SystemTestExecutor {

	private final Supplier<Launcher> launcherSupplier = LauncherFactory::create;
	
	public TestExecutionSummary execute(PrintWriter out) throws Exception {
		return new CustomContextClassLoaderExecutor(createCustomClassLoader()).invoke(() -> executeTests(out));
	}

	private Optional<ClassLoader> createCustomClassLoader() {
		List<Path> additionalClasspathEntries = new ArrayList<>();
		if (!additionalClasspathEntries.isEmpty()) {
			URL[] urls = additionalClasspathEntries.stream().map(this::toURL).toArray(URL[]::new);
			ClassLoader parentClassLoader = ClassLoaderUtils.getDefaultClassLoader();
			ClassLoader customClassLoader = URLClassLoader.newInstance(urls, parentClassLoader);
			return Optional.of(customClassLoader);
		}
		return Optional.empty();
	}

	private URL toURL(Path path) {
		try {
			return path.toUri().toURL();
		}
		catch (Exception ex) {
			throw new JUnitException("Invalid classpath entry: " + path, ex);
		}
	}

	private TestExecutionSummary executeTests(PrintWriter out) {
		System.out.println("running SystemTestExecutor.executeTests");
		Launcher launcher = launcherSupplier.get();
		SummaryGeneratingListener summaryListener = registerListeners(out, launcher);

		LauncherDiscoveryRequest discoveryRequest = new DiscoveryRequestCreator().toDiscoveryRequest();
		discoveryRequest.getEngineFilters().forEach(filter -> System.out.println("engine filter: " + filter.toString()));
		discoveryRequest.getFiltersByType(ClassNameFilter.class).forEach(filter -> System.out.println("class name filter: " + filter));
		discoveryRequest.getFiltersByType(PackageNameFilter.class).forEach(filter -> System.out.println("package name filter: " + filter));
		launcher.execute(discoveryRequest);

		TestExecutionSummary summary = summaryListener.getSummary();
		if (summary.getTotalFailureCount() > 0) {
			printSummary(summary, out);
		}

		return summary;
	}

	private void printSummary(TestExecutionSummary summary, PrintWriter out) {
		summary.printFailuresTo(out);
		summary.printTo(out);
	}

	private SummaryGeneratingListener registerListeners(PrintWriter out, Launcher launcher) {
		// always register summary generating listener
		SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
		launcher.registerTestExecutionListeners(summaryListener);
		SystemTestListener systemTestListener = new SystemTestListener();
		launcher.registerTestExecutionListeners(systemTestListener);
		return summaryListener;
	}

}
