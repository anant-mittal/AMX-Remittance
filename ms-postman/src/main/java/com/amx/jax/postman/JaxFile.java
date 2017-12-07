package com.amx.jax.postman;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class JaxFile {

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

	public void donwload(HttpServletResponse response) throws DocumentException, IOException {
		FileOutputStream os = null;
		response.setHeader("Cache-Control", "cache, must-revalidate");
		response.addHeader("Content-disposition", "attachment; filename=" + getName());
		try {
			// final File outputFile = File.createTempFile(fileName, ".pdf");
			OutputStream outputStream = response.getOutputStream();
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(content);
			renderer.layout();
			renderer.createPDF(outputStream);
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
