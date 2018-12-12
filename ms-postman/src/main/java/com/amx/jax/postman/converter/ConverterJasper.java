package com.amx.jax.postman.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.converter.jasper.SimpleReportExporter;
import com.amx.jax.postman.converter.jasper.SimpleReportFiller;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.postman.service.TemplateUtils;
import com.amx.utils.FlatMap;
import com.codahale.metrics.annotation.Timed;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;

/**
 * The Class ConverterJasper.
 */
@Component
public class ConverterJasper implements FileConverter {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterJasper.class);

	/** The simple report filler. */
	@Autowired
	SimpleReportFiller simpleReportFiller;

	/** The simple exporter. */
	@Autowired
	SimpleReportExporter simpleExporter;

	/** The post man config. */
	@Autowired
	PostManConfig postManConfig;

	/** The message source. */
	@Autowired
	private MessageSource messageSource;

	/** The template utils. */
	@Autowired
	private TemplateUtils templateUtils;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.converter.FileConverter#toPDF(com.amx.jax.postman.model.
	 * File)
	 */
	@Override
	@Timed(name = "PDF_CREATION_JASPER", absolute = true)
	public File toPDF(File file) throws JRException {

		simpleReportFiller.setReportFileName("templates/jasper/" + file.getITemplate().getFileName() + ".jrxml");
		simpleReportFiller.compileReport();

		// ResourceBundle rb = ResourceBundle.getBundle("messages",
		// postManConfig.getLocal(file));

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("title", "Employee Report Example");
		parameters.put("model", new FlatMap(file.getModel()));
		parameters.put("TU", templateUtils);
		parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE,
				new MessageSourceResourceBundle(messageSource, postManConfig.getLocal(file)));

		simpleReportFiller.setParameters(parameters);
		simpleReportFiller.fillReport();
		// simpleReportFiller.getJasperPrint().getDefaultStyle().setFontSize(50f);
		// simpleReportFiller.getJasperPrint().getDefaultStyle().setPdfFontName("Arial");

		// simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());

		// simpleExporter.exportToPdf("employeeReport.pdf", "baeldung");
		// simpleExporter.exportToXlsx("employeeReport.xlsx", "Employee Data");
		// simpleExporter.exportToCsv("employeeReport.csv");
		// simpleExporter.exportToHtml("employeeReport.html");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			JasperExportManager.exportReportToPdfStream(simpleReportFiller.getJasperPrint(), outputStream);

			file.setBody(outputStream.toByteArray());
			file.setType(Type.PDF);

		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return file;
	}

	/**
	 * To PDF 2.
	 *
	 * @param file
	 *            the file
	 * @return the file
	 */
	public File toPDF2(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			String reportFileName = "jasper/htmlwrapper.jrxml";

			InputStream reportStream = getClass().getResourceAsStream("/".concat(reportFileName));
			// JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			// JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml",
			// ".jasper"));

			Map<String, Object> parameters = new HashMap<>();
			String htmlCode = file.getContent();
			parameters.put("htmlCode", htmlCode);
			parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			JasperReportsContext jrc = new SimpleJasperReportsContext();
			JasperExportManager jem = JasperExportManager.getInstance(jrc);

			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

			file.setBody(outputStream.toByteArray());
			file.setType(Type.PDF);

		} catch (JRException e) {
			LOGGER.error("Some Error", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		// jasperReportExporter.setJasperPrint(jasperReportFiller.getJasperPrint());

		// jasperReportExporter.exportToPdf(fileName, author);
		return file;
	}

}
