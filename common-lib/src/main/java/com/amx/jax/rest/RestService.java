package com.amx.jax.rest;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.jax.filter.AppClientInterceptor;

@Component
public class RestService {

	public static RestTemplate staticRestTemplate;

	@Autowired(required = false)
	RestTemplate restTemplate;

	RestService() {
		if (restTemplate != null) {
			restTemplate.setInterceptors(Collections.singletonList(new AppClientInterceptor()));
			RestService.staticRestTemplate = restTemplate;
		}
	}

	public Ajax ajax(String url) {
		return new Ajax(url);
	}

	public class Ajax {

		private UriComponentsBuilder builder;
		HttpEntity<?> requestEntity;
		HttpMethod method;
		Map<String, String> uriParams = new HashMap<String, String>();

		public Ajax(String url) {
			builder = UriComponentsBuilder.fromUriString(url);
		}

		public Ajax pathParam(String paramKey, String paramValue) {
			uriParams.put(paramKey, paramValue);
			return this;
		}

		public Ajax queryParam(String paramKey, String paramValue) {
			builder.queryParam(paramKey, paramValue);
			return this;
		}

		public Ajax post(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.POST;
			this.requestEntity = requestEntity;
			return this;
		}

		public Ajax get() {
			this.method = HttpMethod.GET;
			return this;
		}

		public <T> T as(Class<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return staticRestTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

		public <T> T as(ParameterizedTypeReference<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return staticRestTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

	}

}
