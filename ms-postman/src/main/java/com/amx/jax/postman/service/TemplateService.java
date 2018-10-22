package com.amx.jax.postman.service;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.custom.HelloDialect;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.ITemplates.ITemplate;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.utils.IoUtils;

/**
 * The Class TemplateService.
 */
@Component
public class TemplateService {

	/** The log. */
	private Logger log = Logger.getLogger(getClass());

	/** The application context. */
	@Autowired
	private ApplicationContext applicationContext;

	/** The template engine. */
	@Autowired
	@Qualifier("messageTemplateEngine")
	private SpringTemplateEngine templateEngine;

	/** The text template engine. */
	@Autowired
	private SpringTemplateEngine textTemplateEngine;

	/** The message source. */
	@Autowired
	private MessageSource messageSource;

	/** The template utils. */
	@Autowired
	private TemplateUtils templateUtils;

	/** The post man config. */
	@Autowired
	private PostManConfig postManConfig;

	/**
	 * Instantiates a new template service.
	 *
	 * @param templateEngine
	 *            the template engine
	 */
	@Autowired
	TemplateService(TemplateEngine templateEngine) {
		templateEngine.addDialect(new HelloDialect());
	}

	/**
	 * Process html.
	 *
	 * @param template
	 *            the template
	 * @param context
	 *            the context
	 * @return the string
	 */
	public String processHtml(ITemplate template, Context context) {
		String rawStr = templateEngine.process(template.getHtmlFile(), context);

		Pattern p = Pattern.compile("src=\"inline:(.*?)\"");
		Matcher m = p.matcher(rawStr);
		while (m.find()) {
			String contentId = m.group(1);
			try {
				rawStr = rawStr.replace("src=\"inline:" + contentId + "\"",
						"src=\"" + templateUtils.readAsBase64String(contentId) + "\"");
			} catch (IOException e) {
				log.error("Template parsing Error : " + template.getFileName(), e);
			}
		}

		return rawStr;
	}

	public String processJson(ITemplate template, Context context) {
		return templateEngine.process(template.getJsonFile(), context);
	}

	/**
	 * Gets the local.
	 *
	 * @param file
	 *            the file
	 * @return the local
	 */
	private Locale getLocal(File file) {
		if (file == null || file.getLang() == null) {
			return new Locale(postManConfig.getTenantLang().getCode());
		}
		return new Locale(file.getLang().getCode());
	}

	/**
	 * Parses file.template and creates content;
	 * 
	 * @param file
	 *            the file
	 * @return the file
	 */
	public File process(File file) {
		Locale locale = getLocal(file);
		String reverse = messageSource.getMessage("flag.reverse.char", null, locale);

		if (("true".equalsIgnoreCase(reverse)) && file.getType() == File.Type.PDF) {
			TemplateUtils.reverseFlag(true);
		}

		log.info("====" + locale.toString() + "======" + reverse + "   " + TemplateUtils.reverseFlag());

		Context context = new Context(locale);

		context.setVariable("_tu", templateUtils);

		context.setVariables(file.getModel());
		if (file.getITemplate().isThymleaf()) {
			String content;
			if (file.getType() == File.Type.JSON) {
				content = this.processJson(file.getITemplate(), context);
			} else {
				content = this.processHtml(file.getITemplate(), context);
			}
			file.setContent(content);
		}
		return file;
	}

	/**
	 * Process text.
	 *
	 * @param template
	 *            the template
	 * @param context
	 *            the context
	 * @return the string
	 */
	public String processText(TemplatesMX template, Context context) {
		return textTemplateEngine.process(template.getFileName(), context);
	}

	/**
	 * Read image as input stream source.
	 *
	 * @param contentId
	 *            the content id
	 * @return the input stream source
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public InputStreamSource readImageAsInputStreamSource(String contentId) throws IOException {
		InputStreamSource imageSource = new ByteArrayResource(
				IoUtils.toByteArray(applicationContext.getResource("classpath:" + contentId).getInputStream()));
		return imageSource;
	}

}
