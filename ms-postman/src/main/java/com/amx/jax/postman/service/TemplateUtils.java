package com.amx.jax.postman.service;

import java.awt.Image;
import java.io.IOException;
import java.text.Bidi;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantProperties;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.ContextUtil;
import com.amx.utils.IoUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;

/**
 * The Class TemplateUtils.
 */
@Component
public class TemplateUtils {

	/** The log. */
	private static Logger log = Logger.getLogger(TemplateUtils.class);

	/** The Constant base64. */
	private static final Map<String, String> base64 = new ConcurrentHashMap<String, String>();
	private static final Map<String, String> templateFiles = new ConcurrentHashMap<String, String>();
	private static boolean IS_TEMPLATE_SCANNED = false;

	/** The tenant properties. */
	@Autowired
	TenantProperties tenantProperties;

	/** The env. */
	@Autowired
	private Environment env;

	/** The application context. */
	@Autowired
	private ApplicationContext applicationContext;

	@Value("classpath*:*templates/html/*.html")
	private Resource[] htmlFiles;

	@Value("classpath*:*/templates/json/*.json")
	private Resource[] jsonFiles;

	@Value("classpath*:*/templates/jasper/*.jrxml")
	private Resource[] jasperFiles;

	public String getTemplateFile(String file, Tenant tnt, Locale locale) {
		if (!IS_TEMPLATE_SCANNED) {
			for (Resource resource : htmlFiles) {
				String[] fileName = resource.getFilename().split("\\.(?=[^\\.]+$)");
				String filePath = "html/" + fileName[0];
				templateFiles.put(filePath, filePath);
			}
			for (Resource resource : jsonFiles) {
				String[] fileName = resource.getFilename().split("\\.(?=[^\\.]+$)");
				String filePath = "json/" + fileName[0];
				templateFiles.put(filePath, filePath);
			}
			for (Resource resource : jasperFiles) {
				String[] fileName = resource.getFilename().split("\\.(?=[^\\.]+$)");
				String filePath = "jasper/" + fileName[0];
				templateFiles.put(filePath, filePath);
			}
			IS_TEMPLATE_SCANNED = true;
		}
		String specficFile = String.format("%s_%s.%s", file, locale.getLanguage(),
				ArgUtil.parseAsString(tnt, Constants.BLANK).toLowerCase());
		if (templateFiles.containsKey(specficFile)) {
			return templateFiles.get(specficFile);
		}

		String tenantFile = String.format("%s.%s", file,
				ArgUtil.parseAsString(tnt, Constants.BLANK).toLowerCase());
		if (templateFiles.containsKey(tenantFile)) {
			templateFiles.put(specficFile, tenantFile);
			return tenantFile;
		}

		String localeFile = String.format("%s_%s", file,
				ArgUtil.parseAsString(tnt, Constants.BLANK).toLowerCase());
		if (templateFiles.containsKey(localeFile)) {
			templateFiles.put(specficFile, localeFile);
			return tenantFile;
		}

		templateFiles.put(specficFile, file);

		return file;
	}

	/**
	 * Prop.
	 *
	 * @param key the key
	 * @return the string
	 */
	public String prop(String key) {
		String value = tenantProperties.getProperties().getProperty(key);
		if (ArgUtil.isEmpty(value)) {
			value = env.getProperty(key);
		}
		return ArgUtil.parseAsString(value);
	}

	/**
	 * Image.
	 *
	 * @param key   the key
	 * @param clean the clean
	 * @return the string
	 */
	public String image(String key, boolean clean) {
		if (clean) {
			return this.prop(key).replace("data:image/png;base64,", "");
		}
		return this.prop(key);
	}

	/**
	 * Image jasper.
	 *
	 * @param key   the key
	 * @param clean the clean
	 * @return the image
	 * @throws JRException the JR exception
	 */
	public Image imageJasper(String key, boolean clean) throws JRException {
		return net.sf.jasperreports.engine.util.JRImageLoader.getInstance(new SimpleJasperReportsContext())
				.loadAwtImageFromBytes(javax.xml.bind.DatatypeConverter.parseBase64Binary(image(key, clean)));
	}

	/**
	 * Image.
	 *
	 * @param key the key
	 * @return the string
	 */
	public String image(String key) {
		return this.image(key, false);
	}

	/**
	 * Image jasper.
	 *
	 * @param key the key
	 * @return the image
	 * @throws JRException the JR exception
	 */
	public Image imageJasper(String key) throws JRException {
		return this.imageJasper(key, false);
	}

	/**
	 * Reverse flag.
	 *
	 * @param set the set
	 */
	public static void reverseFlag(boolean set) {
		ContextUtil.map().put("reverseflag", true);
	}

	/**
	 * Reverse flag.
	 *
	 * @return true, if successful
	 */
	public static boolean reverseFlag() {
		return ArgUtil.parseAsBoolean(ContextUtil.map().get("reverseflag"), false);
	}

	/**
	 * Reverse.
	 *
	 * @param str the str
	 * @return the string
	 */
	public String reverse(String str) {
		if (reverseFlag()) {
			return fixBiDi(str);
			// return new StringBuilder(str).reverse().toString();
		}
		return str;
	}

	/**
	 * Reverse.
	 *
	 * @return the string
	 */
	public String reverse() {
		return "-X-X-";
	}

	/**
	 * Fix bi di.
	 *
	 * @param wordTemp the word temp
	 * @return the string
	 */
	public static String fixBiDi(String wordTemp) {
		String word = ArgUtil.parseAsString(wordTemp, Constants.DEFAULT_STRING);
		Bidi bidi = new Bidi(word, -2);
		if (!bidi.isMixed() && bidi.getBaseLevel() == 0) {
			return word;
		} else {
			int runCount = bidi.getRunCount();
			byte[] levels = new byte[runCount];
			Integer[] runs = new Integer[runCount];

			for (int result = 0; result < runCount; ++result) {
				levels[result] = (byte) bidi.getRunLevel(result);
				runs[result] = Integer.valueOf(result);
			}

			Bidi.reorderVisually(levels, 0, runs, 0, runCount);
			StringBuilder bidiText = new StringBuilder();

			for (int i = 0; i < runCount; ++i) {
				int index = runs[i].intValue();
				int start = bidi.getRunStart(index);
				int end = bidi.getRunLimit(index);
				byte level = levels[index];
				if ((level & 1) != 0) {
					while (true) {
						--end;
						if (end < start) {
							break;
						}

						char character = word.charAt(end);
						if (Character.isMirrored(word.codePointAt(end))) {
							bidiText.append(character);
						} else {
							bidiText.append(character);
						}
					}
				} else {
					bidiText.append(word, start, end);
				}
			}

			return bidiText.toString();
		}
	}

	/**
	 * Read as base 64 string.
	 *
	 * @param contentId the content id
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readAsBase64String(String contentId) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("data:image/png;base64,");
		String base64String = null;
		if (base64.containsKey(contentId)) {
			base64String = base64.get(contentId);
		} else {
			byte[] imageByteArray = IoUtils
					.toByteArray(applicationContext.getResource("classpath:" + contentId).getInputStream());
			base64String = StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false));
			base64.put(contentId, base64String);
		}
		sb.append(base64String);
		return sb.toString();
	}

	/**
	 * Read as resource.
	 *
	 * @param contentId the content id
	 * @return the resource
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Resource readAsResource(String contentId) throws IOException {
		return applicationContext.getResource("classpath:" + contentId);
	}

	/**
	 * Fix bi di check.
	 *
	 * @param parseAsString the parse as string
	 * @return the string
	 */
	public static String fixBiDiCheck(String parseAsString) {
		if (reverseFlag()) {
			return fixBiDi(parseAsString);
		}
		return parseAsString;
	}

}
