package com.amx.jax.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.LoggerService;
import com.amx.utils.CloseUtil;
import com.amx.utils.IoUtils;

@RestController
public class SWAdapterScannerController {

	private static final Logger LOGGER = LoggerService.getLogger(SWAdapterScannerController.class);

	@Autowired
	SWDocumentScanner swDocumentScanner;

	@RequestMapping(value = "/pub/doc/scan", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> documentScan() throws Exception {
		// File scanFile = new
		// File("/Users/lalittanwar/Projects/amx-scanner/test/civilfornt_copy.jpg");
		// File directory = new File("/Users/lalittanwar/Projects/amx-scanner/test/");

		for (int i = 0; i < 5; i++) {
			File scanFile = new File("c:\\temp\\scanimage.jpg");
			File directory = new File("c:\\temp");

			boolean isScanDocument = false;

			try {

				if (!directory.exists()) {
					directory.mkdir();
				}

				if (isScanDocument == false) {
					if (scanFile.exists()) {
						LOGGER.info("Deleting : " + scanFile.getAbsolutePath());
						scanFile.delete();
					} else {
						LOGGER.info("Not Deleting : " + scanFile.getAbsolutePath());
					}
					swDocumentScanner.scan();

					isScanDocument = true;
					// TimeUnit.SECONDS.sleep(15);

					File f = new File(scanFile.getAbsolutePath());
					long startTime = System.currentTimeMillis();
					long end = startTime + 3000;// 10 seconds *1000=10000 // 15 sec *1000 =15000
					while (System.currentTimeMillis() < end) {
						if (f.exists()) {
							break;
						} else {
							// TimeUnit.SECONDS.sleep(1);
						}
					}
					long endTime = System.currentTimeMillis();
					LOGGER.info("ofile :" + f);
					LOGGER.info("Total time for Image reading:" + (endTime - startTime) / 1000L);
				}

				if (isScanDocument == true) {
					FileInputStream fin = null;
					try {
						if (scanFile.exists()) {
							LOGGER.debug("File Exists" + scanFile.getAbsolutePath());
							int len = 0;
							int lenDiff = 100;
							while (lenDiff > 0 || len == 0) {
								CloseUtil.close(fin);
								fin = new FileInputStream(scanFile.getAbsolutePath());
								byte[] out1 = IoUtils.toByteArray(fin);
								System.out.println(out1.length);
								lenDiff = out1.length - len;
								len = out1.length;
								if (lenDiff == 0 && len != 0) {
									return ResponseEntity.ok().contentType(MediaType.valueOf("image/jpeg")).body(out1);
								}
								TimeUnit.SECONDS.sleep(1);
							}
							LOGGER.error("File Exists But Dont know hwat happedn");
						} else {
							LOGGER.error("File NotExists" + scanFile.getAbsolutePath());
						}
					} catch (IOException e) {
						LOGGER.error("FileInputStream", e);
					} finally {
						try {
							if (fin != null) {
								fin.close();
							}
						} catch (IOException e) {
							LOGGER.debug("FileInputStream", e);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("ERROR", e);
			}
		}
		return ResponseEntity.noContent().build();
	}

}
