package com.amx.jax.postman.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.amx.jax.dict.Language;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.model.ITemplates.ITemplate;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class File {

	private static Logger LOGGER = LoggerService.getLogger(File.class);

	public enum Type {
		PDF("application/pdf"), CSV("text/csv"), PNG("image/png"), JSON("application/json"), HTML("text/html"), TEXT(
				"text/plain");

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
	private String title;
	private Type type;
	private PDFConverter converter;

	public PDFConverter getConverter() {
		return converter;
	}

	public void setConverter(PDFConverter converter) {
		this.converter = converter;
	}

	private String template = null;
	private Map<String, Object> model = new HashMap<String, Object>();

	public File() {
	}

	@SuppressWarnings("unchecked")
	public File(ITemplate template, Object data, Type fileType) {
		this.setITemplate(template);
		this.setType(fileType);
		this.setModel(JsonUtil.fromJson(JsonUtil.toJson(data), Map.class));
	}

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

	public String getTemplate() {
		return template;
	}

	@JsonSetter
	public void setTemplate(String template) {
		this.template = template;
	}

	@JsonIgnore
	public void setITemplate(ITemplate template) {
		this.template = template.toString();
	}

	@JsonIgnore
	public ITemplate getITemplate() {
		return ITemplates.getTemplate(this.template);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
