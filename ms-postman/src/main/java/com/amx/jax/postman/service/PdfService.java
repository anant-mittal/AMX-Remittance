package com.amx.jax.postman.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.bootloaderjs.Constants;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class PdfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PdfService.class);

	@Autowired
	private ApplicationContext context;

	public PdfService() {
		LicenseKey.loadLicenseFile(getClass().getResourceAsStream("/license/itextkey1520345049511_0.xml"));
	}

	public File convert(File file) {
		return this.convertJasper(file);
	}

	public File convertJasper(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			String reportFileName = "jasper/htmlwrapper.jrxml";

			InputStream reportStream = getClass().getResourceAsStream("/".concat(reportFileName));
			//JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			// JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml",
			// ".jasper"));

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("htmlCode",
					"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n" + "<html>\n" + "<head>\n"
							+ " <title>Sample of html based report</title>\n" + "</head>\n" + "\n" + "<body>\n"
							+ "<h1>This is a sample of html based report</h1>\n" + "\n"
							+ "<p>Only minimal html features are supported</p>\n" + "\n"
							+ "<p>At least images are supported</p>\n" + "<br/><br/>\n" + "</body>\n" + "</html>");
			
			String htmlCode = file.getContent();
			parameters.put("htmlCode",htmlCode);

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
					/* ignore */ }
			}
		}

		// jasperReportExporter.setJasperPrint(jasperReportFiller.getJasperPrint());

		// jasperReportExporter.exportToPdf(fileName, author);

		return file;
	}

	public File convertIText7(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			// LicenseKey.loadLicenseFile("/license/itextkey1520345049511_0.xml");

			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {

				WriterProperties writerProperties = new WriterProperties();
				writerProperties.addXmpMetadata();
				PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);

				PdfDocument pdfDoc = new PdfDocument(pdfWriter);
				pdfDoc.getCatalog().setLang(new PdfString("en-US"));
				// Set the document to be tagged
				pdfDoc.setTagged();
				pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

				// Set meta tags
				PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
				pdfMetaData.setAuthor("Samuel Huylebroeck");
				pdfMetaData.addCreationDate();
				pdfMetaData.getProducer();
				pdfMetaData.setCreator("iText Software");
				pdfMetaData.setKeywords("example, accessibility");
				pdfMetaData.setSubject("PDF accessibility");
				// Title is derived from html

				// pdf conversion
				ConverterProperties props = new ConverterProperties();
				FontProvider fp = new FontProvider();
				fp.addStandardPdfFonts();
				fp.addDirectory("/fonts");// The noto-nashk font file (.ttf extension) is placed in the resources

				fp.addFont("/fonts/arial.ttf");
				fp.addFont("/fonts/arialbd.ttf");
				// fp.addFont("/fonts/all/arialbi.ttf");
				// fp.addFont("/fonts/all/ariali.ttf");
				fp.addFont("/fonts/arialuni.ttf");
				fp.addFont("/fonts/arabic/NotoNaskhArabic-Regular.ttf");

				fp.addFont("/fonts/all/times.ttf");
				fp.addFont("/fonts/all/timesbd.ttf");
				fp.addFont("/fonts/all/timesbi.ttf");
				fp.addFont("/fonts/all/timesi.ttf");
				fp.addSystemFonts();

				props.setFontProvider(fp);
				// props.setBaseUri("/fonts");
				// Setup custom tagworker factory for better tagging of headers
				// DefaultTagWorkerFactory tagWorkerFactory = new
				// AccessibilityTagWorkerFactory();
				// props.setTagWorkerFactory(tagWorkerFactory);

				// context.getResource("classpath:" + jsonFile).getInputStream()

				HtmlConverter.convertToPdf(file.getContent(), pdfDoc, props);
				pdfDoc.close();

				file.setBody(outputStream.toByteArray());
				file.setType(Type.PDF);
			}
		} catch (IOException e) {
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

	public File convertFS(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {
				ITextRenderer renderer = new ITextRenderer();
				renderer.getFontResolver().addFont("/fonts/all/arial.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/arialbd.ttf",
				// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
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
