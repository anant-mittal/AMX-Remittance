package com.amx.jax.test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.dict.VendorFeatures;
import com.amx.jax.ui.service.GeoHotPoints;
import com.amx.utils.JsonUtil;
import com.github.gianlucanitti.javaexpreval.ExpressionException;

public class OWATest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	private static Logger LOGGER = LoggerFactory.getLogger(OWATest.class);

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ExpressionException
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */

	public static void main(String[] args) throws MalformedURLException, URISyntaxException {

		LOGGER.info("GHP====== {}", JsonUtil.toJson(GeoHotPoints.values()));
		String messageJson = JsonUtil
				.toJson(GeoHotPoints.fromString(GeoHotPoints.class, "ABDULLAH_AL_MUBARAK_CO_OPERATIVE"));
		LOGGER.info("====== {}", messageJson);

		LOGGER.info("VF====== {}", JsonUtil.toJson(VendorFeatures.values()));
		String messageJson2 = JsonUtil.toJson(VendorFeatures.fromString(VendorFeatures.class, "REMIT2"));
		LOGGER.info("====== {}", messageJson2);

	}
}
