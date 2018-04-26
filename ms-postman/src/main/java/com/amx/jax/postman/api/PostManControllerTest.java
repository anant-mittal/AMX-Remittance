package com.amx.jax.postman.api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.client.GeoLocationClient;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.GeoLocation;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.amx.utils.IoUtils;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.Transliterator;
//import com.itextpdf.text.DocumentException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
@RequestMapping("test/")
public class PostManControllerTest {

	@Autowired
	private HttpServletResponse response;

	@Value("${jax.postman.url}")
	private String postmanUrl;

	@Autowired
	PostManClient postManClient;

	@Autowired
	PostManServiceImpl postManServiceImpl;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	GeoLocationClient geoLocationClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManControllerTest.class);

	@RequestMapping(value = "exception")
	public Message notifySlack() throws UnirestException {

		try {
			throw new Exception("Some Error");
		} catch (Exception e) {
			postManClient.notifyException("My Error", e);
		}
		return null;
	}

	@RequestMapping(value = PostManUrls.GEO_LOC, method = RequestMethod.GET)
	public GeoLocation location() throws PostManException {
		String remoteAddr = null;
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return geoLocationClient.getLocation(remoteAddr);
	}

	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE + "/print", method = RequestMethod.GET)
	public Message print(@RequestParam Tenant tnt) throws PostManException {

		Message msg = new Message();
		final String ENG_TO_DEV = "Latin-Devanagari";
		Transliterator toDevnagiri = Transliterator.getInstance(ENG_TO_DEV);
		String devnagiri = toDevnagiri.transliterate("lalit");

		postManServiceImpl.print();
		return msg;
	}

	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE + "/{template}.{ext}", method = RequestMethod.GET)
	public String processTemplate(@PathVariable("template") Templates template, @PathVariable("ext") String ext,
			@RequestParam(name = "email", required = false) String email,
			@RequestBody(required = false) Map<String, Object> data, @RequestParam(required = false) Tenant tnt,
			@RequestParam(required = false) File.PDFConverter lib)
			throws IOException, /*DocumentException,*/ PostManException {

		Map<String, Object> map = readJsonWithObjectMapper("json/" + template.getFileName() + ".json");

		// LOGGER.info("====={}", messageSource.getMessage("sender.details", null,
		// localeResolver.resolveLocale(request)));

		postManClient.setLang(localeResolver.resolveLocale(request).toString());

		File file = new File();
		file.setModel(map);
		file.setTemplate(template);
		file.setConverter(lib);

		if ("pdf".equals(ext)) {
			file.setType(File.Type.PDF);
			file = postManClient.processTemplate(file);
			file.create(response, false);
			return null;
		} else if ("html".equals(ext)) {
			file = postManClient.processTemplate(file);
			if (email != null) {
				Email eml = new Email();
				eml.setSubject("Email Template : " + template);
				eml.setFrom("amxjax@gmail.com");
				eml.addTo(email);
				eml.setTemplate(template);
				eml.setHtml(true);
				eml.setModel(map);
				//this.readImageWithObjectMapper(null);

				File file2 = new File();
				file2.setTemplate(template);
				file2.setType(File.Type.PDF);
				file2.setModel(map);
				file2.setConverter(lib);

				eml.addFile(file2);

				postManClient.sendEmailAsync(eml);
			}
			return file.getContent();
		} else {
			return JsonUtil.toJson(map);
		}

	}

	public File processTemplate(Templates template, Map<String, Object> map, File.Type fileType)
			throws UnirestException {
		HttpResponse<File> response = Unirest.post(postmanUrl + PostManUrls.PROCESS_TEMPLATE)
				// .header("content-type", "application/json")
				.header("accept", "application/json").field("template", template).field("data", JsonUtil.toJson(map))
				.field("fileType", fileType).asObject(File.class);
		return response.getBody();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> readJsonWithObjectMapper(String jsonFile) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		// return objectMapper.readValue(new File(jsonFile), Map.class);
		return objectMapper.readValue(context.getResource("classpath:" + jsonFile).getInputStream(), Map.class);
	}

	public InputStreamSource readImageWithObjectMapper(String jsonFile) throws IOException {
		// return objectMapper.readValue(new File(jsonFile), Map.class);
		InputStreamSource imageSource = new ByteArrayResource(
				IoUtils.toByteArray(context.getResource("classpath:images/logo.png").getInputStream()));
		return imageSource;

	}
}
