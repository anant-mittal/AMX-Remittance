package com.bootloaderjs;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println();

		String template = "/name/{name}/age/{age}";
		UriTemplate uriTemplate = new UriTemplate(template);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("name", "Arnold");
		parameters.put("age", "23");

		UriComponentsBuilder builder = UriComponentsBuilder.fromPath(template);
		System.out.println(builder.buildAndExpand(parameters));

		Matcher matcher = pattern.matcher("com.amx.jax.logger.client.AuditFilter<com.amx.jax.logger.events.UserEvent>");
		if (matcher.find()) {
			System.out.println(matcher.group(1));
		}
	}
}
