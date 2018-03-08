package com.amx.jax.postman.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.bootloaderjs.Constants;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

@Component
public class ConverterFlyingSaucer implements FileConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterFlyingSaucer.class);

	@Autowired
	private ApplicationContext context;

	public ConverterFlyingSaucer() {
		// com.itextpdf.licensekey.LicenseKey.loadLicenseFile(getClass().getResourceAsStream("/license/itextkey1520345049511_0.xml"));
	}

	@Override
	public File toPDF(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {
				ITextRenderer renderer = new ITextRenderer();
				renderer.getFontResolver().addFont("/fonts/all/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				renderer.getFontResolver().addFont("/fonts/all/arialbd.ttf", BaseFont.IDENTITY_H,
						BaseFont.NOT_EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/arialbi.ttf",
				// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/ariali.ttf",
				// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/arialuni.ttf",
				// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				/*
				 * renderer.getFontResolver().addFont("/fonts/all/times.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 * renderer.getFontResolver().addFont("/fonts/all/timesbd.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 * renderer.getFontResolver().addFont("/fonts/all/timesbi.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 * renderer.getFontResolver().addFont("/fonts/all/timesi.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 */
				renderer.getFontResolver().addFontDirectory("/fonts/all", BaseFont.NOT_EMBEDDED);

				// renderer.getFontResolver().addFont("/fonts/arabic/NotoNaskhArabic-Regular.ttf",
				// BaseFont.IDENTITY_H,
				// BaseFont.EMBEDDED);

				// renderer.getFontResolver().addFont("/fonts/all/times.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/timesbd.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/timesbi.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/timesi.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

				renderer.setDocumentFromString(file.getContent());
				renderer.layout();
				renderer.createPDF(outputStream);
				renderer.finishPDF();
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
