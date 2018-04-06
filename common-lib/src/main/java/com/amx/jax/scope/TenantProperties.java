package com.amx.jax.scope;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.utils.ArgUtil;
import com.amx.utils.FileUtil;

@TenantScoped
@Component
public class TenantProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantProperties.class);
	private static TenantProperties obj = new TenantProperties();

	private Properties properties = null;

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
				tenantProperties.load(inSideInputStream);
				LOGGER.info("Loaded Properties from classpath: {}", ufile.getPath());
			}

			outSideInputStream = FileUtil.getExternalResourceAsStream(propertyFile, object.getClass());
			if (outSideInputStream != null) {
				tenantProperties.load(outSideInputStream);
				LOGGER.info("Loaded Properties from jarpath: {}", propertyFile);
			}

		} catch (IllegalArgumentException | IOException e) {
			LOGGER.error("readPropertyException", e);
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

	public static Object assignValues(String tenant, Object object) {
		Properties tenantProperties = getProperties(tenant, object);
		try {
			for (Field field : object.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(TenantValue.class)) {
					TenantValue annotation = field.getAnnotation(TenantValue.class);
					String propertyName = annotation.value().replace("${", "").replace("}", "");
					Object propertyValue = tenantProperties.getProperty(propertyName);
					if (propertyValue != null) {
						// ArgUtil.parseAsT(propertyValue, defaultValue, required)
						String typeName = field.getGenericType().getTypeName();
						field.setAccessible(true);
						if ("java.lang.String".equals(typeName)) {
							field.set(object, propertyValue);
						} else if ("int".equals(typeName) || "java.lang.Integer".equals(typeName)) {
							field.set(object, ArgUtil.parseAsInteger(propertyValue));
						} else if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
							field.set(object, ArgUtil.parseAsBoolean(propertyValue));
						}
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOGGER.error("readPropertyException", e);
		}
		return object;
	}

}
