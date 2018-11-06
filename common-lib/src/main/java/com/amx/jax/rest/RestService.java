package com.amx.jax.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

import com.amx.jax.AppConstants;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.filter.AppClientInterceptor;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@Component
public class RestService {

	public static RestTemplate staticRestTemplate;

	@Autowired(required = false)
	RestTemplate restTemplate;

	@Autowired
	AppClientInterceptor appClientInterceptor;

	public void setErrorHandler(ResponseErrorHandler errorHandler) {
		Assert.notNull(errorHandler, "ResponseErrorHandler must not be null");
		this.restTemplate.setErrorHandler(errorHandler);
	}

	public RestTemplate getRestTemplate() {
		if (staticRestTemplate == null) {
			if (restTemplate != null) {
				restTemplate.setInterceptors(Collections.singletonList(appClientInterceptor));
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

	public static class Ajax {
		public static final Pattern pattern = Pattern.compile("^.*\\{(.*)\\}.*$");

		public static enum RestMethod {
			POST, FORM, GET
		}

		private UriComponentsBuilder builder;
		HttpEntity<?> requestEntity;
		HttpMethod method;
		Map<String, String> uriParams = new HashMap<String, String>();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		RestTemplate restTemplate;
		private HttpHeaders headers = new HttpHeaders();
		Map<String, String> headersMeta = new HashMap<String, String>();
		boolean isForm = false;

		public Ajax(RestTemplate restTemplate, String url) {
			this.restTemplate = restTemplate;
			builder = UriComponentsBuilder.fromUriString(url);
		}

		public Ajax(RestTemplate restTemplate, URI uri) {
			this.restTemplate = restTemplate;
			builder = UriComponentsBuilder.fromUriString(uri.toString());
		}

		public <T> Ajax filter(RestMetaRequestOutFilter<T> restMetaServiceFilter) {
			RestService.exportMetaToStatic(restMetaServiceFilter, this.header());
			return this;
		}

		public Ajax path(String path) {
			builder.path(path);
			return this;
		}

		public Ajax pathParam(String paramKey, Object paramValue) {
			uriParams.put(paramKey, ArgUtil.parseAsString(paramValue));
			return this;
		}

		public Ajax params(Map<String, String> params) {
			uriParams.putAll(params);
			return this;
		}

		public Ajax query(String query) {
			builder.query(query);
			return this;
		}

		public Ajax queryParam(String paramKey, Object paramValue) {
			if (paramValue != null) {
				builder.queryParam(paramKey, paramValue);
			}
			return this;
		}

		public Ajax field(String paramKey, Object paramValue) {
			parameters.add(paramKey, ArgUtil.parseAsString(paramValue));
			return this;
		}

		public Ajax field(RestQuery query, Map<String, String> params) throws UnsupportedEncodingException {
			Map<String, String> s = query.toMap();
			for (Entry<String, String> entry : s.entrySet()) {
				String value = entry.getValue();
				Matcher match = pattern.matcher(value);
				if (match.find() && params.containsKey(match.group(1))) {
					value = value.replace("{" + match.group(1) + "}", params.get(match.group(1)));
				}
				this.field(entry.getKey(), value);
			}
			return this;
		}

		public Ajax header(String paramKey, Object paramValue) {
			headers.add(paramKey, ArgUtil.parseAsString(paramValue));
			return this;
		}

		public Ajax meta(String metaKey, String metaValue) {
			headers.add(AppConstants.META_XKEY, String.format("%s=%s", metaKey, metaValue));
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

		public HttpHeaders header() {
			return this.headers;
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

		public Ajax call(RestMethod method) {
			if (method == RestMethod.FORM) {
				return this.postForm();
			} else if (method == RestMethod.POST) {
				return this.post();
			}
			return this.get();
		}

		public <T> T as(Class<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

		public <T> T as(ParameterizedTypeReference<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

		public String asString() {
			return this.as(String.class);
		}

		public Object asObject() {
			return this.as(Object.class);
		}

		public Map<String, Object> asMap() {
			return this.as(new ParameterizedTypeReference<Map<String, Object>>() {
			});
		}

		public <T> Map<String, T> asMap(Class<T> valueType) {
			return this.as(new ParameterizedTypeReference<Map<String, T>>() {
			});
		}

		public AmxApiResponse<Object, Object> asApiResponse() {
			return this.asApiResponse(Object.class);
		}

		/**
		 * @deprecated use {@link #as(ParameterizedTypeReference)} directly, to have
		 *             smooth casting of resultType
		 * 
		 * @param resultType
		 * @return
		 */
		@Deprecated
		public <T> AmxApiResponse<T, Object> asApiResponse(Class<T> resultType) {
			return this.asApiResponse(resultType, Object.class);
		}

		/**
		 * @deprecated use {@link #as(ParameterizedTypeReference)} directly, to have
		 *             smooth casting of resultType
		 * 
		 * @param resultType
		 * @return
		 */
		@Deprecated
		public <T, M> AmxApiResponse<T, M> asApiResponse(Class<T> resultType, Class<M> metaType) {
			return this.as(new ParameterizedTypeReference<AmxApiResponse<T, M>>() {
			});
		}

		public Object as() {
			return this.asObject();
		}

		public Ajax build(RestMethod reqType, String reqQuery, RestQuery reqFields, Map<String, String> paramValues)
				throws UnsupportedEncodingException {
			if (!ArgUtil.isEmpty(reqQuery)) {
				this.query(reqQuery);
			}
			if (!ArgUtil.isEmpty(paramValues)) {
				this.params(paramValues);
			}

			if (!ArgUtil.isEmpty(reqFields)) {
				this.params(reqFields.toMap());
			}

			if (!ArgUtil.isEmpty(reqFields)) {
				this.field(reqFields, paramValues);
			}
			return this.call(reqType);
		}

	}

	public static <T> void exportMetaToStatic(RestMetaRequestOutFilter<T> restMetaFilter, HttpHeaders httpHeaders) {
		if (restMetaFilter != null) {
			T meta = restMetaFilter.exportMeta();
			httpHeaders.add(AppConstants.META_XKEY, JsonUtil.toJson(meta));
		}
	}

	public static <T> void importMetaFromStatic(RestMetaRequestInFilter<T> restMetaFilter, HttpServletRequest req)
			throws Exception {
		if (restMetaFilter != null) {
			String metaValueString = req.getHeader(AppConstants.META_XKEY);
			restMetaFilter.importMeta(restMetaFilter.export(metaValueString), req);
		}
	}
}
