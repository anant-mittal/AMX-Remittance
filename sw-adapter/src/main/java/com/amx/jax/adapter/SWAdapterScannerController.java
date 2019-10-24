package com.amx.jax.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.IoUtils;

@Controller
public class SWAdapterScannerController {

	private static final Logger LOGGER = LoggerService.getLogger(SWAdapterScannerController.class);

	@ApiRequest(type = RequestType.POLL)
	@RequestMapping(value = "/pub/document/scan", method = RequestMethod.GET,
			produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> documentScan() throws Exception {
		File scanFile = new File("c:\\temp\\scanimage.jpg");
		File directory = new File("c:\\temp");
		boolean isScanDocument = false;

		try {

			if (!directory.exists()) {
				directory.mkdir();
			}

			if (isScanDocument == false) {
				if (scanFile.exists()) {
					scanFile.delete();
				}
				// Runtime.getRuntime().exec("cmd /c scan.bat", null, new
				// File("C:/temp"));

				Runtime.getRuntime().exec("cmd /c PlkScan.exe", null,
						new File("C:/ProgramData/Plustek/Software/PlkScan"));
				isScanDocument = true;
				// TimeUnit.SECONDS.sleep(15);
				LOGGER.info("Path : " + scanFile.getAbsolutePath());
				File f = new File(scanFile.getAbsolutePath());
				long startTime = System.currentTimeMillis();
				long end = startTime + 15000;// 10 seconds *1000=10000 // 15 sec *1000 =15000
				while (System.currentTimeMillis() < end) {
					if (f.exists()) {
						TimeUnit.SECONDS.sleep(2);
						break;
					}
				}
				long endTime = System.currentTimeMillis();
				LOGGER.info("ofile :" + f);
				LOGGER.info("Total time for Image reading:" + (endTime - startTime) / 1000L);
			}
			if (isScanDocument == true) {
				LOGGER.info("getAbsolutePath :" + scanFile.getAbsolutePath());
				FileInputStream fin = new FileInputStream(scanFile.getAbsolutePath());
				byte[] out1 = IoUtils.toByteArray(fin);
				return ResponseEntity.ok()
						.contentType(MediaType.valueOf("image/jpeg")).body(out1);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return ResponseEntity.noContent().build();
	}

}
