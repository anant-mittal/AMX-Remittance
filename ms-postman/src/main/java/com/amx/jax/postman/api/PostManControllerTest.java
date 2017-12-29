package com.amx.jax.postman.api;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
@RequestMapping("test/")
public class PostManControllerTest {

	@Autowired
	private PostManServiceImpl postManService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManControllerTest.class);

	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE + "/{template}.{ext}", method = RequestMethod.GET)
	public String processTemplate(@PathVariable("template") String template, @PathVariable("ext") String ext,
			@RequestBody(required = false) Map<String, Object> data)
			throws IOException, DocumentException, UnirestException {

		Map<String, Object> map = readJsonWithObjectMapper("/json/"+template+".json");

		if ("pdf".equals(ext)) {
			postManService.createPDF(template, map);
			return null;
		} else if ("html".equals(ext)) {
			return postManService.processTemplate(template, map, "test.pdf")
					.getContent();
		} else {
			return JsonUtil.toJson(map);
		}
	}
	
    @SuppressWarnings("unchecked")
	public Map<String, Object> readJsonWithObjectMapper(String jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //return objectMapper.readValue(new File(jsonFile), Map.class);
        return objectMapper.readValue(new File(getClass().getResource(jsonFile).getFile()), Map.class);
    }
}
