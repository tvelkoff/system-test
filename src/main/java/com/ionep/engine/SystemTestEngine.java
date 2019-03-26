package com.ionep.engine;

import java.util.Optional;

import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.discovery.DiscoverySelectorResolver;
import org.junit.jupiter.engine.execution.JupiterEngineExecutionContext;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;

public class SystemTestEngine extends HierarchicalTestEngine<JupiterEngineExecutionContext> {

	public static final String ENGINE_ID = "system-test";

	@Override
	public String getId() {
		return ENGINE_ID;
	}

	@Override
	public Optional<String> getGroupId() {
		return Optional.of("com.ionep");
	}

	@Override
	public Optional<String> getArtifactId() {
		return Optional.of("system-test-engine");
	}

	@Override
	public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
		JupiterEngineDescriptor engineDescriptor = new JupiterEngineDescriptor(uniqueId);
		new DiscoverySelectorResolver().resolveSelectors(discoveryRequest, engineDescriptor);
		return engineDescriptor;
	}

	@Override
	protected JupiterEngineExecutionContext createExecutionContext(ExecutionRequest request) {
		return new JupiterEngineExecutionContext(request.getEngineExecutionListener(),
			request.getConfigurationParameters());
	}

}
