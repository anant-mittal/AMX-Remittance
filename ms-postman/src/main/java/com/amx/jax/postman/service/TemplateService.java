package com.amx.jax.postman.service;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amx.jax.postman.custom.HelloDialect;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Templates;
import com.bootloaderjs.IoUtils;

@Component
public class TemplateService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private ApplicationContext context;

	private TemplateEngine customTemplateEngine;
	
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private TemplateEngine textTemplateEngine;
	
	@Autowired
	TemplateService(TemplateEngine templateEngine){
		templateEngine.addDialect(new HelloDialect());
	}

	public String processHtml(Templates template, Context context) {
		String rawStr = templateEngine.process(template.getFileName(), context);

		Pattern p = Pattern.compile("src=\"inline:(.*?)\"");
		Matcher m = p.matcher(rawStr);
		while (m.find()) {
			String contentId = m.group(1);
			try {
				rawStr = rawStr.replace("src=\"inline:" + contentId + "\"",
						"src=\"" + this.readAsBase64String(contentId) + "\"");
			} catch (IOException e) {
				log.error("Template parsing Error : " + template.getFileName(), e);
			}
		}

		return rawStr;
	}

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	private Locale getLocal() {
		return localeResolver.resolveLocale(request);
		//return new Locale("ar-kw");
	}

	@Autowired
	TemplateUtils templateUtils;

	/**
	 * Parses file.template and creates content;
	 * 
	 * @param file
	 * @return
	 */
	public File process(File file) {
		String reverse = messageSource.getMessage("flag.reverse.char", null, getLocal());

		if (("true".equalsIgnoreCase(reverse)) && file.getType() == File.Type.PDF) {
			templateUtils.reverseFlag(true);
		}

		log.info("======" + getLocal().toString() + "======" + reverse + "   " + templateUtils.reverseFlag());

		Context context = new Context(getLocal());
		context.setVariable("_tu", templateUtils);
		context.setVariables(file.getModel());
		String content = this.processHtml(file.getTemplate(), context);
		file.setContent(content);
		return file;
	}

	public String processText(Templates template, Context context) {
		return textTemplateEngine.process(template.getFileName(), context);
	}

	public InputStreamSource readImageAsInputStreamSource(String contentId) throws IOException {
		InputStreamSource imageSource = new ByteArrayResource(
				IoUtils.toByteArray(context.getResource("classpath:" + contentId).getInputStream()));
		return imageSource;
	}

	public Resource readAsResource(String contentId) throws IOException {
		return context.getResource("classpath:" + contentId);
	}

	public String readAsBase64String(String contentId) throws IOException {

		StringBuilder sb = new StringBuilder();
		sb.append("data:image/png;base64,");
		byte[] imageByteArray = IoUtils.toByteArray(context.getResource("classpath:" + contentId).getInputStream());
		sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false)));
		return sb.toString();

	}

}
