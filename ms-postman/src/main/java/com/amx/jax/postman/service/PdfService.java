package com.amx.jax.postman.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.converter.ConverterFOP;
import com.amx.jax.postman.converter.ConverterFlyingSaucer;
import com.amx.jax.postman.converter.ConverterIText5;
import com.amx.jax.postman.converter.ConverterIText7;
import com.amx.jax.postman.converter.ConverterJasper;
import com.amx.jax.postman.model.File;

@Component
public class PdfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PdfService.class);

	@Autowired
	private ConverterFlyingSaucer converterFlyingSaucer;

	@Autowired
	private ConverterJasper converterJasper;

	@Autowired
	private ConverterIText7 converterIText7;

	@Autowired
	private ConverterIText5 converterIText5;

	@Autowired
	private ConverterFOP converterFOP;

	public File convert(File file) {
		return converterFOP.toPDF(file);
	}

}
