package com.amx.jax.postman.service;

import java.awt.Image;
import java.io.IOException;
import java.text.Bidi;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantProperties;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.ContextUtil;
import com.amx.utils.IoUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;

@Component
public class TemplateUtils {

	private static Logger log = Logger.getLogger(TemplateUtils.class);
	private static final Map<String, String> base64 = new ConcurrentHashMap<String, String>();

	@Autowired
	TenantProperties tenantProperties;

	@Autowired
	private Environment env;

	@Autowired
	private ApplicationContext applicationContext;

	public String prop(String key) {
		String value = tenantProperties.getProperties().getProperty(key);
		if (ArgUtil.isEmpty(value)) {
			value = env.getProperty(key);
		}
		return ArgUtil.parseAsString(value);
	}

	public String image(String key, boolean clean) {
		if (clean) {
			return this.prop(key).replace("data:image/png;base64,", "");
		}
		return this.prop(key);
	}

	public Image imageJasper(String key, boolean clean) throws JRException {
		return net.sf.jasperreports.engine.util.JRImageLoader.getInstance(new SimpleJasperReportsContext())
				.loadAwtImageFromBytes(javax.xml.bind.DatatypeConverter.parseBase64Binary(image(key, clean)));
	}

	public String image(String key) {
		return this.image(key, false);
	}

	public Image imageJasper(String key) throws JRException {
		return this.imageJasper(key, false);
	}

	public static void reverseFlag(boolean set) {
		ContextUtil.map().put("reverseflag", true);
	}

	public static boolean reverseFlag() {
		return ArgUtil.parseAsBoolean(ContextUtil.map().get("reverseflag"), false);
	}

	public String reverse(String str) {
		if (reverseFlag()) {
			return fixBiDi(str);
			// return new StringBuilder(str).reverse().toString();
		}
		return str;
	}

	public String reverse() {
		return "-X-X-";
	}

	public static String fixBiDi(String wordTemp) {
		String word = ArgUtil.parseAsString(wordTemp, Constants.defaultString);
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

	public Resource readAsResource(String contentId) throws IOException {
		return applicationContext.getResource("classpath:" + contentId);
	}

	public static String fixBiDiCheck(String parseAsString) {
		if (reverseFlag()) {
			return fixBiDi(parseAsString);
		}
		return parseAsString;
	}

}
