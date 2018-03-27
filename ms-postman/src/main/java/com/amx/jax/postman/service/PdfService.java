package com.amx.jax.postman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.converter.ConverterAmxFlyingSaucer;
import com.amx.jax.postman.converter.ConverterFOP;
import com.amx.jax.postman.converter.ConverterFlyingSaucer;
import com.amx.jax.postman.converter.ConverterIText5;
import com.amx.jax.postman.converter.ConverterIText7;
import com.amx.jax.postman.converter.ConverterJasper;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.ConverterType;

@Component
public class PdfService {

	@Autowired
	private ConverterFlyingSaucer converterFlyingSaucer;

	@Autowired
	private ConverterAmxFlyingSaucer converterAmxFlyingSaucer;

	@Autowired
	private ConverterJasper converterJasper;

	@Autowired
	private ConverterIText7 converterIText7;

	@Autowired
	private ConverterIText5 converterIText5;

	@Autowired
	private ConverterFOP converterFOP;

	public File convert(File file) {

		ConverterType conv = file.getConverter();
		if (conv == ConverterType.FOP) {
			return converterFOP.toPDF(file);
		} else if (conv == ConverterType.FS) {
			return converterFlyingSaucer.toPDF(file);
		} else if (conv == ConverterType.AMXFS) {
			return converterAmxFlyingSaucer.toPDF(file);
		} else if (conv == ConverterType.ITEXT5) {
			return converterIText5.toPDF(file);
		} else if (conv == ConverterType.JASPER) {
			return converterJasper.toPDF(file);
		}
		return converterIText7.toPDF(file);
	}

}
