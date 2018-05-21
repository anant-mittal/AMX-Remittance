package com.amx.jax.postman.service;

import java.awt.Image;
import java.text.Bidi;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantProperties;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.ContextUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;

@Component
public class TemplateUtils {

	private static Logger log = Logger.getLogger(TemplateUtils.class);

	@Autowired
	TenantProperties tenantProperties;

	public String prop(String key) {
		return tenantProperties.getProperties().getProperty(key);
	}

	public String image(String key, boolean clean) {
		if (clean) {
			return tenantProperties.getProperties().getProperty(key).replace("data:image/png;base64,", "");
		}
		return tenantProperties.getProperties().getProperty(key);
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

	public static String fixBiDiCheck(String parseAsString) {
		if (reverseFlag()) {
			return fixBiDi(parseAsString);
		}
		return parseAsString;
	}

}
