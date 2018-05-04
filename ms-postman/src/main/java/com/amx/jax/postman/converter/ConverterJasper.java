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

@Component
public class ConverterJasper implements FileConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterJasper.class);

	@Autowired
	SimpleReportFiller simpleReportFiller;

	@Autowired
	SimpleReportExporter simpleExporter;

	@Autowired
	PostManConfig postManConfig;

	@Autowired
	private MessageSource messageSource;

	@Override
	public File toPDF(File file) {

		simpleReportFiller.setReportFileName("jasper/" + file.getTemplate().getFileName() + ".jrxml");
		simpleReportFiller.compileReport();

		// ResourceBundle rb = ResourceBundle.getBundle("messages",
		// postManConfig.getLocal(file));

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("title", "Employee Report Example");
		parameters.put("minSalary", 15000.0);
		parameters.put("model", file.getModel());
		parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE,
				new MessageSourceResourceBundle(messageSource, postManConfig.getLocal(file)));

		LOGGER.info("===== {} {}",postManConfig.getLocal(file),file.getLang());
		simpleReportFiller.setParameters(parameters);
		simpleReportFiller.fillReport();
		//simpleReportFiller.getJasperPrint().getDefaultStyle().setFontSize(50f);
		//simpleReportFiller.getJasperPrint().getDefaultStyle().setPdfFontName("Arial");

		//simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());

		// simpleExporter.exportToPdf("employeeReport.pdf", "baeldung");
		// simpleExporter.exportToXlsx("employeeReport.xlsx", "Employee Data");
		// simpleExporter.exportToCsv("employeeReport.csv");
		// simpleExporter.exportToHtml("employeeReport.html");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			JasperExportManager.exportReportToPdfStream(simpleReportFiller.getJasperPrint(), outputStream);

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
		return file;
	}

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
