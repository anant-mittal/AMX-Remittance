package com.amx.jax.rest;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.filter.AppClientInterceptor;
import com.amx.utils.ArgUtil;

@Component
public class RestService {

	public static RestTemplate staticRestTemplate;

	@Autowired(required = false)
	RestTemplate restTemplate;

	public void setErrorHandler(ResponseErrorHandler errorHandler) {
		Assert.notNull(errorHandler, "ResponseErrorHandler must not be null");
		this.restTemplate.setErrorHandler(errorHandler);
	}

	public RestTemplate getRestTemplate() {
		if (staticRestTemplate == null) {
			if (restTemplate != null) {
				restTemplate.setInterceptors(Collections.singletonList(new AppClientInterceptor()));
				RestService.staticRestTemplate = restTemplate;
			} else {
				throw new RuntimeException("No RestTemplate bean found");
			}
		}
		return restTemplate;
	}

	public Ajax ajax(String url) {
		return new Ajax(getRestTemplate(), url);
	}

	public Ajax ajax(URI uri) {
		return new Ajax(getRestTemplate(), uri);
	}

	public class Ajax {

		private UriComponentsBuilder builder;
		HttpEntity<?> requestEntity;
		HttpMethod method;
		Map<String, String> uriParams = new HashMap<String, String>();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		RestTemplate restTemplate;
		HttpHeaders headers = new HttpHeaders();
		boolean isForm = false;

		public Ajax(RestTemplate restTemplate, String url) {
			this.restTemplate = restTemplate;
			builder = UriComponentsBuilder.fromUriString(url);
		}

		public Ajax(RestTemplate restTemplate, URI uri) {
			this.restTemplate = restTemplate;
			builder = UriComponentsBuilder.fromUriString(uri.toString());
		}

		public Ajax path(String path) {
			builder.path(path);
			return this;
		}

		public Ajax pathParam(String paramKey, Object paramValue) {
			uriParams.put(paramKey, ArgUtil.parseAsString(paramValue));
			return this;
		}

		public Ajax queryParam(String paramKey, Object paramValue) {
			builder.queryParam(paramKey, paramValue);
			return this;
		}

		public Ajax field(String paramKey, String paramValue) {
			parameters.add(paramKey, paramValue);
			return this;
		}

		public Ajax header(String paramKey, String paramValue) {
			headers.add(paramKey, paramValue);
			return this;
		}

		public Ajax contentTypeJson() {
			headers.add("content-type", "application/json");
			return this;
		}

		public Ajax acceptJson() {
			headers.add("accept", "application/json");
			return this;
		}

		public Ajax header(HttpHeaders header) {
			this.headers = header;
			return this;
		}

		public Ajax post(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.POST;
			this.requestEntity = requestEntity;
			return this;
		}

		public <T> Ajax put(T body) {
			return this.put(new HttpEntity<T>(body, headers));
		}

		public Ajax put() {
			return this.put(new HttpEntity<Object>(null, headers));
		}

		public Ajax put(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.PUT;
			this.requestEntity = requestEntity;
			return this;
		}

		public <T> Ajax post(T body) {
			return this.post(new HttpEntity<T>(body, headers));
		}

		public Ajax post() {
			return this.post(new HttpEntity<Object>(null, headers));
		}

		public Ajax postForm() {
			this.isForm = true;
			return this.post(new HttpEntity<MultiValueMap<String, String>>(parameters, headers));
		}

		public <T> Ajax postJson(T body) {
			return this.header("content-type", "application/json").post(body);
		}

		public Ajax get(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.GET;
			this.requestEntity = requestEntity;
			return this;
		}

		public Ajax get() {
			return this.get(new HttpEntity<Object>(null, headers));
		}

		public <T> T as(Class<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

		public <T> T as(ParameterizedTypeReference<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

		public <T> AmxApiResponse<T, Object> asResponseModel(Class<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate
					.exchange(uri, method, requestEntity, new ParameterizedTypeReference<AmxApiResponse<T, Object>>() {
					}).getBody();
		}

		public <T, M> AmxApiResponse<T, M> asResponseModel(Class<T> responseType, Class<M> modelType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate
					.exchange(uri, method, requestEntity, new ParameterizedTypeReference<AmxApiResponse<T, M>>() {
					}).getBody();
		}

		public String asString() {
			return this.as(String.class);
		}

		public Object asObject() {
			return this.as(Object.class);
		}

		public Object as() {
			return this.asObject();
		}

	}

}
