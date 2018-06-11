package com.amx.jax.postman.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.exceptions.TemplateInputException;

import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.utils.ArgUtil;

import net.sf.jasperreports.engine.JRException;

@Component
public class FileService {

	private static final Logger LOGGER = LoggerService.getLogger(FileService.class);

	@Autowired
	private PdfService pdfService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	TemplateUtils templateUtils;

	@Autowired
	SlackService slackService;

	@Autowired
	AuditService auditService;

	@Timed
	public File create(File file) {
		if (file.getTemplate() != null) {
			/**
			 * from template to content
			 */
			try {
				templateService.process(file);
			} catch (TemplateInputException e) {
				LOGGER.error("Template Process Exception", e);
			}

			if (ArgUtil.isEmptyString(file.getTitle())) {
				file.setTitle(templateUtils.prop("template." + file.getTemplate() + ".title"));
			}

			if (file.getName() == null) {
				if (file.getType() == Type.PDF) {
					file.setName(file.getTemplate().getFileName() + ".pdf");
				} else {
					file.setName(file.getTemplate().getFileName() + ".html");
				}
			}
		}

		if (file.getType() == File.Type.PDF) {
			/**
			 * From string to File type
			 */
			PMGaugeEvent pmGaugeEvent = new PMGaugeEvent();
			try {
				pdfService.convert(file);
				auditService.gauge(pmGaugeEvent.fillDetail(PMGaugeEvent.Type.PDF_CREATED, file));
			} catch (JRException e) {
				auditService.excep(pmGaugeEvent.fillDetail(PMGaugeEvent.Type.PDF_ERROR, file), LOGGER, e);
			}

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
