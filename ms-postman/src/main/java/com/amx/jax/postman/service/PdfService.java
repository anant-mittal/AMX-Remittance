package com.amx.jax.postman.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.bootloaderjs.Constants;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

@Component
public class PdfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PdfService.class);

	public File convert(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {
				ITextRenderer renderer = new ITextRenderer();
				renderer.getFontResolver().addFont("/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				//renderer.getFontResolver().addFont("/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				renderer.getFontResolver().addFont("/fonts/arialbold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

				renderer.setDocumentFromString(file.getContent());
				renderer.layout();
				renderer.createPDF(outputStream);
				renderer.finishPDF();

				System.out.println("PDF created successfully");
				file.setBody(outputStream.toByteArray());
				file.setType(Type.PDF);
			}
		} catch (DocumentException | IOException e) {
			LOGGER.error("Some Error", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
		return file;
	}
}
