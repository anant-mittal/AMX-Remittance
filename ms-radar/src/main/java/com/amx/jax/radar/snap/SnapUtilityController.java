package com.amx.jax.radar.snap;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.radar.service.CivilIdValidationService;
import com.amx.jax.radar.service.CivilIdValidationService.CivilIdValidationResponse;
import com.google.i18n.phonenumbers.NumberParseException;

@Controller
public class SnapUtilityController {

	@Autowired
	CivilIdValidationService civilIdValidationService;

	@ResponseBody
	@RequestMapping(value = "snap/api/utility/civil/check", method = RequestMethod.GET)
	public CivilIdValidationResponse charResponse(@RequestParam String identity)
			throws NumberParseException, IOException {
		return civilIdValidationService.validate(identity);
	}

}
