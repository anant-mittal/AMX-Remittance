package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import com.amx.jax.radar.service.CivilIdValidationService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class CivilIdTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	public static XmlMapper xmlMapper = new XmlMapper();

	public static void main(String[] args) throws URISyntaxException, IOException {
		CivilIdValidationService service = new CivilIdValidationService();

		service.validateCaptcha("285061506787");
		// service.validate2("280030801246");
		// service.validate2("278122103469");
		// service.validate2("931126916");
	}

}
