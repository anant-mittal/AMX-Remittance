package com.amx.jax.postman.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

import org.slf4j.Logger;

import com.amx.jax.dict.Language;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class File {

	private static Logger LOGGER = LoggerService.getLogger(File.class);

	public enum Type {
		PDF("application/pdf"), CSV("text/csv"), PNG("image/png");

		String contentType;

		Type(String contentType) {
			this.contentType = contentType;
		}
	}

	public enum PDFConverter {
		AMXFS, FS, FOP, ITEXT5, ITEXT7, JASPER
	}

	Language lang = null;

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	private String content;
	private String name;
	private Type type;
	private PDFConverter converter;

	public PDFConverter getConverter() {
		return converter;
	}

	public void setConverter(PDFConverter converter) {
		this.converter = converter;
	}

	private Templates template = null;
	private Map<String, Object> model = new HashMap<String, Object>();
	@Null
	@JsonIgnore
	private String object;

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	@JsonIgnore
	public void setObject(Object object) {
		this.model = JsonUtil.toMap(object);
	}

	public Templates getTemplate() {
		return template;
	}

	public void setTemplate(Templates template) {
		this.template = template;
	}

	private byte[] body;

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
				LOGGER.info("PDF created successfully :  Template {} {}", this.getTemplate(), this.getLang());
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
