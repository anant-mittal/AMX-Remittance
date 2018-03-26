package com.amx.jax.postman.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.utils.Constants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Component
public class ConverterIText5 implements FileConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterIText5.class);

	@Autowired
	private ApplicationContext context;

	public ConverterIText5() {
	}

	@Override
	public File toPDF(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			// LicenseKey.loadLicenseFile("/license/itextkey1520345049511_0.xml");
			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {

				Document document = new Document();

				PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document.open();
				
		        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		        fontImp.register("/fonts/all/arialuni.ttf");

				InputStream is = new ByteArrayInputStream(file.getContent().getBytes("UTF-8"));

//				 XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, is, null,Charset.forName("UTF-8"),
//						fontImp);

				 XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,
						 is);
					    
					    
				file.setBody(outputStream.toByteArray());
				document.close();
				
				file.setType(Type.PDF);
				
				LOGGER.info("Created");

			}
		} catch (IOException | DocumentException e) {
			LOGGER.error("Some Error", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					/* ignore */ 
					}
			}
		}
		return file;
	}

}
