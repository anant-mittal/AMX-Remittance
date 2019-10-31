package com.amx.jax.adapter;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.LoggerService;

@RestController
public class SWAdapterScannerController {

	public static final String PUB_DOC_SCAN = "/pub/doc/scan";

	private static final Logger LOGGER = LoggerService.getLogger(SWAdapterScannerController.class);

	@Autowired
	SWDocumentScanner swDocumentScanner;

	@RequestMapping(value = PUB_DOC_SCAN, produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> documentScan() throws Exception {
		byte[] out = swDocumentScanner.documentScan();
		if (out != null) {
			return ResponseEntity.ok().contentType(MediaType.valueOf("image/jpeg")).body(out);
		}
		return ResponseEntity.noContent().build();
	}

}
