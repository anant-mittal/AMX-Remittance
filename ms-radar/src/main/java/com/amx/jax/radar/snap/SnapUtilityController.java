package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.Language;
import com.amx.jax.postman.client.DocServiceClient;
import com.amx.jax.postman.model.DocResult;
import com.google.i18n.phonenumbers.NumberParseException;

@RestController
public class SnapUtilityController {

	@Autowired
	DocServiceClient documentService;

	@RequestMapping(value = "snap/api/utility/civil/check/id", method = RequestMethod.GET)
	public AmxApiResponse<DocResult, Object> charResponse(@RequestParam String identity)
			throws NumberParseException, IOException {
		return documentService.validate(identity);
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/snap/api/utility/civil/scan/card", method = { RequestMethod.POST })
	public Map<String, Object> uploadServiceProviderFile(@RequestParam MultipartFile file,
			@RequestParam Language lang) throws Exception {
		return documentService.scan(file, lang);
	}

}
