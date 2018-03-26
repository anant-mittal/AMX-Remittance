
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

@Component
public class TenantProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantProperties.class);
	private static TenantProperties properties = new TenantProperties();

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

	public static Object assignValues(String tenant, Object object) {
		Properties tenantProperties = new Properties();
		String propertyFile = "application." + tenant + ".properties";
		File jarPath = new File(
				properties.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().split("!")[0]);
		String propertiesPath = jarPath.getParent();

		InputStream inSideInputStream = null;
		InputStream outSideInputStream = null;

		try {

			URL u = properties.getClass().getClassLoader().getResource("classpath:" + propertyFile);
			if (u != null) {
				inSideInputStream = object.getClass().getClassLoader().getResourceAsStream(u.getPath());
				tenantProperties.load(inSideInputStream);
				LOGGER.info("reading inside property file : {}", u.getPath());
			}

			URL u2 = object.getClass().getClassLoader().getResource(propertiesPath + "/" + propertyFile);
			if (u2 != null) {
				outSideInputStream = object.getClass().getClassLoader()
						.getResourceAsStream(propertiesPath + "/" + propertyFile);
				tenantProperties.load(outSideInputStream);
				LOGGER.info("reading outside property file : {}", u2.getPath());
			}

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
