package com.ionep;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ionep.launcher.SystemTestLauncher;

@Component
public class SystemTestRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("running SystemTestLauncher");
		int exitCode = SystemTestLauncher.execute(System.out, System.err, args.getSourceArgs()).getExitCode();
		System.exit(exitCode);
	}

}
