package com.amx.jax.postman.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.Email;
import com.lowagie.text.DocumentException;

@RestController
public class PostManController {

	@Autowired
	private PostManService postManService;

	private Logger logger = Logger.getLogger(PostManController.class);

	@RequestMapping(value = "/postman/template/{template}", method = RequestMethod.GET)
	public String showtemplate(@PathVariable String template, HttpServletResponse resp)
			throws IOException, DocumentException {
		return postManService.processTemplate(template, new Email(), null).getContent();
	}

}
