package com.amx.jax.scope;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TenantProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantProperties.class);

	private static Environment envProperties;

	@Autowired
	public TenantProperties(Environment envProperties) {
		TenantProperties.envProperties = envProperties;
	}

	public static Environment getEnvProperties() {
		return envProperties;
	}

	public static Object assignValues(String tenant, Object object) {
		Properties tenantProperties = new Properties();
		String propertyFile = "application." + tenant + ".properties";
		File jarPath = new File(
				object.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().split("!")[0]);
		String propertiesPath = jarPath.getParent();

		InputStream inSideInputStream = null;
		InputStream outSideInputStream = null;

		try {

			URL u = object.getClass().getClassLoader().getResource("classpath:" + propertyFile);
			if (u != null) {
				inSideInputStream = object.getClass().getClassLoader().getResourceAsStream(u.getPath());
				tenantProperties.load(inSideInputStream);
			}

			URL u2 = object.getClass().getClassLoader().getResource(propertiesPath + "/" + propertyFile);
			if (u2 != null) {
				outSideInputStream = object.getClass().getClassLoader()
						.getResourceAsStream(propertiesPath + "/" + propertyFile);
				tenantProperties.load(outSideInputStream);
			}

			for (Field field : object.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(TenantValue.class)) {
					TenantValue annotation = field.getAnnotation(TenantValue.class);
					String propertyName = annotation.value().replace("${", "").replace("}", "");
					String propertyValue = tenantProperties.getProperty(propertyName);
					field.setAccessible(true);
					field.set(object, propertyValue);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | IOException e) {
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
		return object;
	}

}
