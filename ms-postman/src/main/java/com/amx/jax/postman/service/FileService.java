package com.amx.jax.postman.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;

@Component
public class FileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

	@Autowired
	private PdfService pdfService;

	@Autowired
	private TemplateService templateService;

	public File create(File file) {
		if (file.getTemplate() != null) {
			/**
			 * from template to content
			 */
			templateService.process(file);

			if (file.getName() == null) {
				if (file.getType() == Type.PDF) {
					file.setName(file.getTemplate() + ".pdf");
				} else {
					file.setName(file.getTemplate() + ".html");
				}
			}
		}

		if (file.getType() == File.Type.PDF) {
			/**
			 * From string to File type
			 */
			pdfService.convert(file);
			LOGGER.info("File converted to PDF");
		}
		return file;
	}

	public InputStream toInputStream(File file) {
		// convert bytes into InputStream
		InputStream is = new ByteArrayInputStream(file.getBody());
		// read it with BufferedReader
		// BufferedReader br = new BufferedReader(new InputStreamReader(is));
		//
		// String line;
		// while ((line = br.readLine()) != null) {
		// System.out.println(line);
		// }
		// br.close();

		return is;
	}

	public DataSource toDataSource(File file) throws MessagingException {
		DataSource dataSource = new ByteArrayDataSource(file.getBody(), "application/pdf");
		MimeBodyPart pdfBodyPart = new MimeBodyPart();
		pdfBodyPart.setDataHandler(new DataHandler(dataSource));
		pdfBodyPart.setFileName(file.getName());
		return dataSource;
	}
}
