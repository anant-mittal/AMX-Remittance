package com.amx.jax.postman.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.utils.Constants;
import com.amx.utils.FileUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKeyException;

@Component
public class ConverterIText7 implements FileConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterIText7.class);

	@Autowired
	public ConverterIText7(@Value("${itext.key.path}") String keyFileSearch) {
		try {
			com.itextpdf.licensekey.LicenseKey
					.loadLicenseFile(FileUtil.getExternalResourceAsStream(keyFileSearch, ConverterIText7.class));
		} catch (LicenseKeyException | IOException e) {
			LOGGER.error("While Loading iText7 key ", e);
		}
	}

	@Override
	public File toPDF(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			// LicenseKey.loadLicenseFile("/license/itextkey1520345049511_0.xml");
			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {

				WriterProperties writerProperties = new WriterProperties();
				writerProperties.addXmpMetadata();
				PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);

				PdfDocument pdfDoc = new PdfDocument(pdfWriter);
				// pdfDoc.getCatalog().setLang(new com.itextpdf.kernel.pdf.PdfString("en-US"));
				// Set the document to be tagged
				pdfDoc.setTagged();
				pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

				// Set meta tags
				PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
				pdfMetaData.setAuthor("Almulla Exchange");
				pdfMetaData.addCreationDate();
				pdfMetaData.getProducer();
				pdfMetaData.setCreator("iText Software");
				pdfMetaData.setKeywords("transaction, receipt");
				pdfMetaData.setSubject("PDF accessibility");
				// Title is derived from html

				// pdf conversion
				ConverterProperties props = new ConverterProperties();
				FontProvider fp = new FontProvider();
				fp.addStandardPdfFonts();
				// fp.addDirectory("/fonts");// The noto-nashk font file (.ttf extension) is
				// placed in the resources

				fp.addFont("/fonts/arial.ttf");
				fp.addFont("/fonts/arialbd.ttf");
				// fp.addFont("/fonts/all/arialbi.ttf");
				// fp.addFont("/fonts/all/ariali.ttf");
				fp.addFont("/fonts/arialuni.ttf");
				fp.addFont("/fonts/unhinted/NotoNaskhArabic-Regular.ttf");

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
				file.setBody(outputStream.toByteArray());
				file.setType(Type.PDF);
				pdfDoc.close();

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

}
