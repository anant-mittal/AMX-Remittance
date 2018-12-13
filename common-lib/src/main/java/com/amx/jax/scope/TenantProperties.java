package com.amx.jax.scope;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.model.Stringable;
import com.amx.utils.ArgUtil;
import com.amx.utils.FileUtil;

@TenantScoped
@Component
public class TenantProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantProperties.class);
	private static TenantProperties obj = new TenantProperties();
	private static Environment ENV;

	private Properties properties = null;

	TenantProperties() {
	}

	@Autowired
	public TenantProperties(Environment env) {
		if (ENV == null) {
			ENV = env;
		}
	}

	public static void setEnviroment(Environment env) {
		ENV = env;
	}

	public static Environment getEnviroment() {
		return ENV;
	}

	public Properties getProperties() {
		if (properties == null) {
			properties = getProperties(TenantContextHolder.currentSite().toString().toLowerCase(), obj);
		}
		return properties;
	}

	public static Properties getProperties(String tenant, Object object) {
		Properties tenantProperties = new Properties();
		String propertyFile = "application." + tenant + ".properties";
		InputStream inSideInputStream = null;
		InputStream outSideInputStream = null;

		try {

			URL ufile = FileUtil.getResource(propertyFile, object.getClass());
			if (ufile != null) {
				inSideInputStream = ufile.openStream();
				// tenantProperties.load(inSideInputStream);
				tenantProperties.putAll(loadPropertiesMap(inSideInputStream));
				LOGGER.info("Loaded Properties from classpath: {}", ufile.getPath());
			}

			outSideInputStream = FileUtil.getExternalResourceAsStream(propertyFile, object.getClass());
			if (outSideInputStream != null) {
				// tenantProperties.load(outSideInputStream);
				tenantProperties.putAll(loadPropertiesMap(outSideInputStream));
				LOGGER.info("Loaded Properties from jarpath: {}", propertyFile);
			}

		} catch (IllegalArgumentException | IOException e) {
			LOGGER.error("readPropertyFileException", e);
		} finally {
			try {
				if (outSideInputStream != null) {
					outSideInputStream.close();
				}
				if (inSideInputStream != null) {
					inSideInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
		return tenantProperties;
	}

	@SuppressWarnings("serial")
	public static Map<String, String> loadPropertiesMap(InputStream s) throws IOException {
		final Map<String, String> ordered = new LinkedHashMap<String, String>();
		// Hack to use properties class to parse but our map for preserved order
		Properties bp = new Properties() {
			@Override
			public synchronized Object put(Object key, Object value) {
				ordered.put((String) key, (String) value);
				return super.put(key, value);
			}
		};
		bp.load(s);
		final Map<String, String> resolved = new LinkedHashMap<String, String>(ordered.size());
		StrSubstitutor sub = new StrSubstitutor(new StrLookup() {
			@Override
			public String lookup(String key) {
				String value = resolved.get(key);

				if (ArgUtil.isEmpty(value)) {
					if (getEnviroment() != null) {
						value = getEnviroment().getProperty(key);
					}
					if (ArgUtil.isEmpty(value)) {
						value = System.getProperty(key);
					}
				}
				return value;
			}
		});
		for (String k : ordered.keySet()) {
			String value = sub.replace(ordered.get(k));
			resolved.put(k, value);
		}
		return resolved;
	}

	public static Object assignValues(String tenant, Object object) {
		Properties tenantProperties = getProperties(tenant, object);
		try {
			Class<?> clazz = AopProxyUtils.ultimateTargetClass(object);

			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(TenantValue.class)) {
					TenantValue annotation = field.getAnnotation(TenantValue.class);
					String propertyName = annotation.value().replace("${", "").replace("}", "");
					Object propertyValue = tenantProperties.getProperty(propertyName);
					if (propertyValue != null) {
						// ArgUtil.parseAsT(propertyValue, defaultValue, required)
						Type type = field.getGenericType();
						String typeName = type.getTypeName();
						field.setAccessible(true);
						if ("java.lang.String".equals(typeName)) {
							field.set(object, propertyValue);
						} else if ("int".equals(typeName) || "java.lang.Integer".equals(typeName)) {
							field.set(object, ArgUtil.parseAsInteger(propertyValue));
						} else if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
							field.set(object, ArgUtil.parseAsBoolean(propertyValue));
						} else if (Language.class.getName().equals(typeName)) {
							field.set(object, ArgUtil.parseAsEnum(propertyValue, Language.DEFAULT));
						} else if (Tenant.class.getName().equals(typeName)) {
							field.set(object, ArgUtil.parseAsEnum(propertyValue, Tenant.DEFAULT));
						} else if ("java.lang.String[]".equals(typeName)) {
							field.set(object, ArgUtil.parseAsStringArray(propertyValue));
						} else if (type instanceof Class && ((Class<?>) type).isArray()
								&& ((Class<?>) type).getComponentType().isEnum()) {
							Class<?> componentType = ((Class<?>) type).getComponentType();
							field.set(object, ArgUtil.parseAsEnumArray(propertyValue, componentType));
						} else if (type instanceof Class && ((Class<?>) type).isEnum()) {
							field.set(object, ArgUtil.parseAsEnum(propertyValue, type));
						} else if (Stringable.class.isAssignableFrom((Class<?>) type)
								|| ((Class<?>) type).isAssignableFrom(Stringable.class)) {
							Class<?> cl = Class.forName(typeName);
							Constructor<?> cons = cl.getConstructor();
							Stringable o = (Stringable) cons.newInstance();
							o.fromString(ArgUtil.parseAsString(propertyValue));
							field.set(object, o);
						} else {
							LOGGER.warn("********** Property Type Undefined *****  " + typeName);
							field.set(object, ArgUtil.parseAsObject((Class<?>) type, propertyValue));
						}
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | ClassCastException | ClassNotFoundException
				| NoSuchMethodException | SecurityException | InstantiationException | InvocationTargetException e) {
			LOGGER.error("readPropertyException", e);
		}
		return object;
	}

}
