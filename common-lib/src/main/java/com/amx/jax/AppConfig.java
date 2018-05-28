package com.amx.jax;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.filter.AppClientInterceptor;
import com.amx.utils.ArgUtil;

@Configuration
@PropertySource("classpath:application-lib.properties")
public class AppConfig {

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");
	public static final String APP_NAME = "${app.name}";
	public static final String APP_PROD = "${app.prod}";
	public static final String APP_SWAGGER = "${app.swagger}";
	public static final String APP_DEBUG = "${app.debug}";
	public static final String APP_CACHE = "${app.cache}";

	public static final String JAX_CDN_URL = "${jax.cdn.url}";
	public static final String JAX_APP_URL = "${jax.app.url}";
	public static final String JAX_SERVICE_URL = "${jax.service.url}";
	public static final String JAX_POSTMAN_URL = "${jax.postman.url}";

	public static final String JAX_PAYMENT_URL = "${jax.payment.url}";
	public static final String JAX_LOGGER_URL = "${jax.logger.url}";
	public static final String JAX_SSO_URL = "${jax.sso.url}";

	@Value(APP_NAME)
	@AppParamKey(AppParam.APP_NAME)
	private String appName;

	@Value(APP_PROD)
	@AppParamKey(AppParam.APP_PROD)
	private Boolean prodMode;

	@Value(APP_SWAGGER)
	@AppParamKey(AppParam.APP_SWAGGER)
	private boolean swaggerEnabled;

	@Value(APP_DEBUG)
	@AppParamKey(AppParam.APP_DEBUG)
	private Boolean debug;

	@Value(APP_CACHE)
	@AppParamKey(AppParam.APP_CACHE)
	private Boolean cache;

	@Value(JAX_CDN_URL)
	@AppParamKey(AppParam.JAX_CDN_URL)
	private String cdnURL;

	@Value(JAX_APP_URL)
	@AppParamKey(AppParam.JAX_APP_URL)
	private String appURL;

	@Value(JAX_SERVICE_URL)
	@AppParamKey(AppParam.JAX_SERVICE_URL)
	private String jaxURL;

	@Value(JAX_POSTMAN_URL)
	@AppParamKey(AppParam.JAX_POSTMAN_URL)
	private String postmapURL;

	@Value(JAX_PAYMENT_URL)
	@AppParamKey(AppParam.JAX_PAYMENT_URL)
	private String paygURL;

	@Value(JAX_LOGGER_URL)
	@AppParamKey(AppParam.JAX_LOGGER_URL)
	private String loggerURL;

	@Value(JAX_SSO_URL)
	@AppParamKey(AppParam.JAX_SSO_URL)
	private String ssoURL;

	@Value("${server.session.cookie.http-only}")
	private boolean cookieHttpOnly;

	@Value("${server.session.cookie.secure}")
	private boolean cookieSecure;

	public boolean isCookieHttpOnly() {
		return cookieHttpOnly;
	}

	public boolean isCookieSecure() {
		return cookieSecure;
	}

	public String getAppName() {
		return appName;
	}

	public Boolean isProdMode() {
		return prodMode;
	}

	public Boolean isSwaggerEnabled() {
		return swaggerEnabled;
	}

	public Boolean isDebug() {
		return debug;
	}

	public Boolean isCache() {
		return debug;
	}

	public String getCdnURL() {
		return cdnURL;
	}

	public String getAppURL() {
		return appURL;
	}

	public String getJaxURL() {
		return jaxURL;
	}

	public String getPostmapURL() {
		return postmapURL;
	}

	public String getPaygURL() {
		return paygURL;
	}

	public String getLoggerURL() {
		return loggerURL;
	}

	@Bean
	public AppParam loadAppParams() {

		for (Field field : AppConfig.class.getDeclaredFields()) {
			AppParamKey s = field.getAnnotation(AppParamKey.class);
			Value v = field.getAnnotation(Value.class);
			if (s != null && v != null) {
				Matcher match = pattern.matcher(v.value());
				if (match.find()) {
					s.value().setProperty(match.group(1));
				}

				String typeName = field.getGenericType().getTypeName();
				Object value = null;
				try {
					value = field.get(this);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				if ("java.lang.String".equals(typeName)) {
					s.value().setValue(ArgUtil.parseAsString(value));
				} else if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
					s.value().setEnabled(ArgUtil.parseAsBoolean(value));
				}
			}
		}

		// AppParam.APP_PROD.setEnabled(this.prodMode, APP_PROD);
		// AppParam.APP_SWAGGER.setEnabled(this.swaggerEnabled, APP_SWAGGER);
		// AppParam.APP_DEBUG.setEnabled(this.debug, APP_DEBUG);
		// AppParam.APP_CACHE.setEnabled(this.cache, APP_CACHE);
		//
		// AppParam.JAX_CDN_URL.setValue(this.cdnURL, JAX_CDN_URL);
		// AppParam.JAX_APP_URL.setValue(this.appURL, JAX_APP_URL);
		// AppParam.JAX_SERVICE_URL.setValue(this.jaxURL, JAX_SERVICE_URL);
		// AppParam.JAX_POSTMAN_URL.setValue(this.postmapURL, JAX_POSTMAN_URL);
		// AppParam.JAX_PAYMENT_URL.setValue(this.paygURL, JAX_PAYMENT_URL);
		// AppParam.JAX_LOGGER_URL.setValue(this.loggerURL, JAX_LOGGER_URL);
		// AppParam.JAX_SSO_URL.setValue(this.ssoURL, JAX_SSO_URL);

		return null;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		builder.rootUri("https://localhost.com");
		RestTemplate restTemplate = builder.build();
		restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate.setInterceptors(Collections.singletonList(new AppClientInterceptor()));
		return restTemplate;
	}

	public String getSsoURL() {
		return ssoURL;
	}

}
