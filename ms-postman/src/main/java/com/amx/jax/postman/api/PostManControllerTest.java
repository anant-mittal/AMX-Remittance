package com.amx.jax.postman.api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.File;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManControllerTest.class);

	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE + "/{template}.{ext}", method = RequestMethod.GET)
	public String processTemplate(@PathVariable("template") String template, @PathVariable("ext") String ext,
			@RequestBody(required = false) Map<String, Object> data)
			throws IOException, DocumentException, UnirestException {

		Map<String, Object> map = readJsonWithObjectMapper("/json/" + template + ".json");

		if ("pdf".equals(ext)) {
			File file = this.processTemplate(template, map, File.Type.PDF);
			file.create(response, false);
			return null;
		} else if ("html".equals(ext)) {
			return this.processTemplate(template, map, null).getContent();
		} else {
			return JsonUtil.toJson(map);
		}

	}

	public File processTemplate(String template, Map<String, Object> map, File.Type fileType) throws UnirestException {
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
		return objectMapper.readValue(new java.io.File(getClass().getResource(jsonFile).getFile()), Map.class);
	}
}
