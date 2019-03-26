package com.ionep.tasks;

import java.util.Optional;
import java.util.concurrent.Callable;

public class CustomContextClassLoaderExecutor {

	private final Optional<ClassLoader> customClassLoader;

	CustomContextClassLoaderExecutor(Optional<ClassLoader> customClassLoader) {
		this.customClassLoader = customClassLoader;
	}

	<T> T invoke(Callable<T> callable) throws Exception {
		if (customClassLoader.isPresent()) {
			// Only get/set context class loader when necessary to prevent problems with
			// security managers
			return replaceThreadContextClassLoaderAndInvoke(customClassLoader.get(), callable);
		}
		return callable.call();
	}

	private <T> T replaceThreadContextClassLoaderAndInvoke(ClassLoader customClassLoader, Callable<T> callable)
			throws Exception {
		ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(customClassLoader);
			return callable.call();
		}
		finally {
			Thread.currentThread().setContextClassLoader(originalClassLoader);
		}
	}

}
