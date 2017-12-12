package com.amx.jax.postman;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperExportManager;
//
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.tool.xml.XMLWorkerHelper;
//
//import com.itextpdf.text.Document;

public class File {

	private String content;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

//	public void donwload2(HttpServletResponse response)
//			throws DocumentException, IOException, com.itextpdf.text.DocumentException {
//		FileOutputStream os = null;
//		response.setHeader("Cache-Control", "cache, must-revalidate");
//		response.addHeader("Content-type", "application/pdf");
//		// response.addHeader("Content-Type", "application/force-download");
//		// response.setHeader("Content-Transfer-Encoding", "binary");
//		response.addHeader("Content-Disposition", "attachment; filename=" + getName());
//		try {
//			// final File outputFile = File.createTempFile(fileName, ".pdf");
//			OutputStream outputStream = response.getOutputStream();
//
//			Document document = new Document();
//			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
//			document.open();
//			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
//					new ByteArrayInputStream(content.getBytes("UTF-8")));
//			document.close();
//			System.out.println("PDF created successfully");
//		} finally {
//			if (os != null) {
//				try {
//					os.close();
//				} catch (IOException e) {
//					/* ignore */ }
//			}
//		}
//	}

	public void donwload(HttpServletResponse response) throws DocumentException, IOException {
		FileOutputStream os = null;
		response.setHeader("Cache-Control", "cache, must-revalidate");
		response.addHeader("Content-type", "application/pdf");
		// response.addHeader("Content-Type", "application/force-download");
		// response.setHeader("Content-Transfer-Encoding", "binary");
		response.addHeader("Content-Disposition", "attachment; filename=" + getName());
		try {
			// final File outputFile = File.createTempFile(fileName, ".pdf");
			OutputStream outputStream = response.getOutputStream();
			ITextRenderer renderer = new ITextRenderer();
			
			renderer.getFontResolver().addFont("/fonts/arial.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			
			renderer.setDocumentFromString(content);
			renderer.layout();
			renderer.createPDF(outputStream);
			renderer.finishPDF();
			outputStream.close();
			
			System.out.println("PDF created successfully");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
	}

}
