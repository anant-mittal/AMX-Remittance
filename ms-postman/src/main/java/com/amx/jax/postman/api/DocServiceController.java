package com.amx.jax.postman.api;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.dict.Language;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.client.DocServiceClient;
import com.amx.jax.postman.model.DocResult;
import com.amx.jax.postman.service.CivilIdValidationService;

/**
 * The Class GeoServiceController.
 */
@RestController
public class DocServiceController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocServiceController.class);

	/** The geo location service. */
	@Autowired
	DocServiceClient documentService;

	@Autowired
	CivilIdValidationService civilIdValidationService;

	@RequestMapping(value = PostManUrls.DOC_UPLOAD_URL, method = RequestMethod.POST)
	public String generateUrl()
			throws PostManException, IOException {
		return documentService.generateUrl();
	}

	@RequestMapping(value = PostManUrls.DOC_VALIDATE_ID, method = RequestMethod.POST)
	public DocResult validateId(@RequestParam String id)
			throws IOException {
		return civilIdValidationService.validateId(id);
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = PostManUrls.DOC_SCAN_ID, method = { RequestMethod.POST })
	public Map<String, Object> scanId(@RequestParam MultipartFile file,
			@RequestParam Language lang) throws Exception {
		return civilIdValidationService.scanId(file, lang);
	}
}
