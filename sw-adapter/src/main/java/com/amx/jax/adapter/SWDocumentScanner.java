package com.amx.jax.adapter;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.LoggerService;

@Component
public class SWDocumentScanner {

	private static final Logger LOGGER = LoggerService.getLogger(SWDocumentScanner.class);

	public static SWDocumentScanner CONTEXT = null;

	public Process process = null;

	public void scan() throws IOException {
		if (process != null) {
			process.destroyForcibly();
			LOGGER.info("Killed :  PlkScan.exe");
		}
		// Runtime.getRuntime().exec("cmd /c scan.bat", null, new
		// File("C:/temp"));

		process = Runtime.getRuntime().exec("cmd /c PlkScan.exe", null,
				new File("C:/ProgramData/Plustek/Software/PlkScan"));

		if (process != null && process.isAlive()) {
			LOGGER.info("Started :  PlkScan.exe");
		}
	}
}
