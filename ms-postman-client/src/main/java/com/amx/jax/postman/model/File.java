package com.amx.jax.postman.model;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class File {

	public enum Type {
		PDF, CSV, PNG
	}

	private String content;
	private String name;

	private Type type;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	private byte[] body;

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

	public void create(HttpServletResponse response, Boolean download) throws IOException {
		OutputStream outputStream = null;
		response.setHeader("Cache-Control", "cache, must-revalidate");
		if (this.type == Type.PDF) {
			response.addHeader("Content-type", "application/pdf");
		}
		if (download) {
			response.addHeader("Content-Disposition", "attachment; filename=" + getName());
		}
		try {
			// final File outputFile = File.createTempFile(fileName, ".pdf");
			if (body != null) {
				outputStream = response.getOutputStream();
				outputStream.write(this.body);
				System.out.println("PDF created successfully");
			}
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
	}
}
