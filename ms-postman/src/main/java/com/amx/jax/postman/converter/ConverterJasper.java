package com.amx.jax.postman.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

	@Override
	public File toPDF(File file) {
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
