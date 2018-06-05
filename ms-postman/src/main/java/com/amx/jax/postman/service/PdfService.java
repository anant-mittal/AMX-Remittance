package com.amx.jax.postman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.converter.ConverterAmxFlyingSaucer;
import com.amx.jax.postman.converter.ConverterFlyingSaucer;
//import com.amx.jax.postman.converter.ConverterIText7;
//import com.amx.jax.postman.converter.ConverterFlyingSaucer;
//import com.amx.jax.postman.converter.ConverterIText5;
//import com.amx.jax.postman.converter.ConverterIText7;
import com.amx.jax.postman.converter.ConverterJasper;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.PDFConverter;

import net.sf.jasperreports.engine.JRException;

@Component
public class PdfService {

	@Autowired
	private ConverterFlyingSaucer converterFlyingSaucer;

	@Autowired
	private ConverterAmxFlyingSaucer converterAmxFlyingSaucer;

	@Autowired
	private ConverterJasper converterJasper;

	// @Autowired
	// private ConverterIText7 converterIText7;

	@Value("${default.pDFConverter}")
	private PDFConverter pDFConverter;

	// @Autowired
	// private ConverterIText5 converterIText5;

	// @Autowired
	// private ConverterFOP converterFOP;

	public File convert(File file) throws JRException {

		PDFConverter conv = file.getConverter();
		if (conv == null) {
			conv = file.getTemplate().getConverter();
			if (conv == null) {
				conv = pDFConverter;
			}
		}

		if (conv == PDFConverter.FS) {
			return converterFlyingSaucer.toPDF(file);
		} else if (conv == PDFConverter.AMXFS) {
			return converterAmxFlyingSaucer.toPDF(file);
			// } else if (conv == PDFConverter.ITEXT5) {
			// return converterIText5.toPDF(file);
		} else if (conv == PDFConverter.JASPER) {
			return converterJasper.toPDF(file);
			// } else if (conv == PDFConverter.ITEXT7) {
			// return converterIText7.toPDF(file);
			// } else if (conv == PDFConverter.FOP) {
			// return converterFOP.toPDF(file);
		} else {
			return converterFlyingSaucer.toPDF(file);
		}
		// return file;
	}

}
