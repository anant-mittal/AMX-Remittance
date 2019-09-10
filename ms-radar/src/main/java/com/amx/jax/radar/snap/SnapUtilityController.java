package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.radar.service.CivilIdValidationService;
import com.amx.jax.radar.service.CivilIdValidationService.CivilIdValidationResponse;
import com.google.i18n.phonenumbers.NumberParseException;

@RestController
public class SnapUtilityController {

	@Autowired
	CivilIdValidationService civilIdValidationService;

	@RequestMapping(value = "snap/api/utility/civil/check/id", method = RequestMethod.GET)
	public CivilIdValidationResponse charResponse(@RequestParam String identity)
			throws NumberParseException, IOException {
		return civilIdValidationService.validate(identity);
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/snap/api/utility/civil/scan/card", method = { RequestMethod.POST })
	public Map<String, Object> uploadServiceProviderFile(@RequestParam MultipartFile file) throws Exception {

		return civilIdValidationService.scan(file);
	}

}
