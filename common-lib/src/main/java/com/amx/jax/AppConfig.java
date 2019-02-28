package com.amx.jax;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.dict.Project;
import com.amx.jax.dict.Tenant;
import com.amx.jax.filter.AppClientErrorHanlder;
import com.amx.jax.filter.AppClientInterceptor;
import com.amx.jax.scope.TenantProperties;
import com.amx.jax.scope.TenantScoped;
import com.amx.utils.ArgUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@TenantScoped
@PropertySource("classpath:application-lib.properties")
@EnableEncryptableProperties
public class AppConfig {

	private static final String PROP_SUFFIX = "}";
	private static final String PROP_PREFIX = "${";
	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");
	public static final String APP_ENV = "${app.env}";
	public static final String APP_GROUP = "${app.group}";
	public static final String APP_NAME = "${app.name}";
	public static final String APP_ID = "${app.id}";

	public static final String APP_PROD = "${app.prod}";
	public static final String APP_SWAGGER = "${app.swagger}";
	public static final String APP_DEBUG = "${app.debug}";
	public static final String APP_CACHE = "${app.cache}";
	public static final String APP_LOGGER = "${app.logger}";
	public static final String APP_MONITOR = "${app.monitor}";

	public static final String APP_CONTEXT_PREFIX = "${server.contextPath}";
	public static final String SPRING_APP_NAME = "${spring.application.name}";

	@Deprecated
	public static final String APP_CLASS = "${app.class}";

	public static final String APP_AUTH_KEY = "${app.auth.key}";
	public static final String APP_AUTH_ENABLED = "${app.auth.enabled}";

	public static final String DEFAULT_TENANT = "${default.tenant}";

	public static final String JAX_CDN_URL = "${jax.cdn.url}";
	public static final String JAX_APP_URL = "${jax.app.url}";
	public static final String JAX_SERVICE_URL = "${jax.service.url}";
	public static final String JAX_POSTMAN_URL = "${jax.postman.url}";

	public static final String JAX_PAYMENT_URL = "${jax.payment.url}";
	public static final String JAX_LOGGER_URL = "${jax.logger.url}";
	public static final String JAX_SSO_URL = "${jax.sso.url}";
	public static final String JAX_AUTH_URL = "${jax.auth.url}";
	public static final String JAX_RADAR_URL = "${jax.radar.url}";

	public static final String SPRING_REDIS_HOST = "${spring.redis.host}";
	public static final String SPRING_REDIS_PORT = "${spring.redis.port}";
	public static final String JAX_PRICER_URL = "${jax.pricer.url}";

	@Value(APP_ENV)
	@AppParamKey(AppParam.APP_ENV)
	private String appEnv;

	@Value(APP_GROUP)
	@AppParamKey(AppParam.APP_GROUP)
	private String appGroup;

	@Value(APP_NAME)
	@AppParamKey(AppParam.APP_NAME)
	private String appName;

	@Value(SPRING_APP_NAME)
	@AppParamKey(AppParam.SPRING_APP_NAME)
	private String springAppName;

	@Value(APP_ID)
	@AppParamKey(AppParam.APP_ID)
	private String appId;

	@Value(APP_CLASS)
	@AppParamKey(AppParam.APP_CLASS)
	private String appClass;

	@Value(APP_PROD)
	@AppParamKey(AppParam.APP_PROD)
	private Boolean prodMode;

	@Value(APP_SWAGGER)
	@AppParamKey(AppParam.APP_SWAGGER)
	private boolean swaggerEnabled;

	@Value(APP_DEBUG)
	@AppParamKey(AppParam.APP_DEBUG)
	private Boolean debug;

	@Value(APP_LOGGER)
	@AppParamKey(AppParam.APP_LOGGER)
	private boolean logger;

	@Value(APP_MONITOR)
	@AppParamKey(AppParam.APP_MONITOR)
	private boolean monitor;

	@Value(APP_AUTH_KEY)
	private String appAuthKey;

	@Value(APP_AUTH_ENABLED)
	@AppParamKey(AppParam.APP_AUTH_ENABLED)
	private boolean appAuthEnabled;

	@Value(APP_CACHE)
	@AppParamKey(AppParam.APP_CACHE)
	private Boolean cache;

	@Value(DEFAULT_TENANT)
	@AppParamKey(AppParam.DEFAULT_TENANT)
	private Tenant defaultTenant;

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

	@Value(JAX_AUTH_URL)
	@AppParamKey(AppParam.JAX_AUTH_URL)
	private String authURL;

	@Value(JAX_RADAR_URL)
	@AppParamKey(AppParam.JAX_RADAR_URL)
	private String radarURL;

	@Value(SPRING_REDIS_HOST)
	@AppParamKey(AppParam.SPRING_REDIS_HOST)
	private String redisSpringHost;

	@Value(SPRING_REDIS_PORT)
	@AppParamKey(AppParam.SPRING_REDIS_PORT)
	private String redisSpringPort;

	@Value(JAX_PRICER_URL)
	@AppParamKey(AppParam.JAX_PRICER_URL)
	private String pricerURL;

	@Value(APP_CONTEXT_PREFIX)
	@AppParamKey(AppParam.APP_CONTEXT_PREFIX)
	private String appPrefix;

	@Value("${server.session.cookie.http-only}")
	private boolean cookieHttpOnly;

	@Value("${server.session.cookie.secure}")
	private boolean cookieSecure;

	@Value("${spring.profiles.active}")
	private String[] springProfile;

	@Value("${app.audit.file.print}")
	String[] printableAuditMarkers;

	@Value("${app.audit.file.skip}")
	String[] skipAuditMarkers;

	@Value("${encrypted.app.property}")
	String appSpecifcDecryptedProp;

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
		return cache;
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

		return null;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, AppClientErrorHanlder errorHandler,
			AppClientInterceptor appClientInterceptor) {
		builder.rootUri("https://localhost.com");
		RestTemplate restTemplate = builder.build();
		restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate.setInterceptors(Collections.singletonList(appClientInterceptor));
		restTemplate.setErrorHandler(errorHandler);
		return restTemplate;
	}

	@Bean
	public Project project(@Value("${app.project}") Project project) {
		ProjectConfig.PROJECT = project;
		return project;
	}

	public String getSsoURL() {
		return ssoURL;
	}

	public String getAuthURL() {
		return authURL;
	}

	public void setAuthURL(String authURL) {
		this.authURL = authURL;
	}

	public final String getPricerURL() {
		return pricerURL;
	}

	public final void setPricerURL(String pricerURL) {
		this.pricerURL = pricerURL;
	}

	public String getAppAuthKey() {
		return appAuthKey;
	}

	public boolean isAppAuthEnabled() {
		return appAuthEnabled;
	}

	@Deprecated
	public String getAppClass() {
		return appClass;
	}

	public String getAppEnv() {
		return appEnv;
	}

	public String getAppGroup() {
		return appGroup;
	}

	public String getAppId() {
		return appId;
	}

	public String[] getPrintableAuditMarkers() {
		return printableAuditMarkers;
	}

	public String[] getSkipAuditMarkers() {
		return skipAuditMarkers;
	}

	public boolean isLogger() {
		return logger;
	}

	public String getAppPrefix() {
		return appPrefix;
	}

	@Autowired
	private Environment environment;

	@PostConstruct
	public void init() {
		TenantProperties.setEnviroment(environment);
		if (defaultTenant != null) {
			Tenant.DEFAULT = defaultTenant;
		}
	}

	public Tenant getDefaultTenant() {
		return defaultTenant;
	}

	public String getSpringAppName() {
		return springAppName;
	}

	public String getRadarURL() {
		return radarURL;
	}

	public String getAppSpecifcDecryptedProp() {
		return appSpecifcDecryptedProp;
	}

}
