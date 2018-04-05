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

	private static Environment envProperties;

	@Autowired
	public TenantProperties(Environment envProperties) {
		TenantProperties.envProperties = envProperties;
	}

	public TenantProperties() {
	}

	public static Environment getEnvProperties() {
		return envProperties;
	}

	public static Properties getProperties(String tenant, Object object) {
		Properties tenantProperties = new Properties();
		String propertyFile = "application." + tenant + ".properties";
		File jarPath = new File(
				obj.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().split("!")[0]);
		String propertiesPath = jarPath.getParent();

		InputStream inSideInputStream = null;
		InputStream outSideInputStream = null;

		try {
//			inSideInputStream = object.getClass().getClassLoader().getResourceAsStream(
//					FileUtil.getFile(""));
			
			URL ufile = object.getClass().getClassLoader().getResource(propertyFile);
			if (ufile != null) {
				//LOG.info("STEP 2");
				//return new File("classpath:" + filePath);
			//}
			
			//File ufile = FileUtil.getFile("classpath:" + propertyFile, object.getClass());
			//if (ufile.isFile()) {
				inSideInputStream = object.getClass().getClassLoader().getResourceAsStream(ufile.getPath());
				if (inSideInputStream == null) {
					inSideInputStream = object.getClass().getClassLoader().getResourceAsStream(propertyFile);
				}
				tenantProperties.load(inSideInputStream);
				LOGGER.info("FOUND : inside file : {}", ufile.getPath());
			} else {
				LOGGER.info("NOFOUND : inside file : {}", ufile.getPath());
			}

			File u2file = FileUtil.getFile(propertyFile, object.getClass());
			if (u2file.isFile()) {
				outSideInputStream = object.getClass().getClassLoader().getResourceAsStream(u2file.getPath());
				tenantProperties.load(outSideInputStream);
				LOGGER.info("FOUND : outside file : {}", u2file.getPath());
			} else {
				LOGGER.info("NOFOUND : outside file : {}", u2file.getPath());
			}

			/**
			 * URL u2 = object.getClass().getClassLoader().getResource(propertiesPath + "/"
			 * + propertyFile); if (u2 != null) { outSideInputStream =
			 * object.getClass().getClassLoader() .getResourceAsStream(propertiesPath + "/"
			 * + propertyFile); tenantProperties.load(outSideInputStream);
			 * LOGGER.info("reading outside property file : {}", u2.getPath()); }
			 **/

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
