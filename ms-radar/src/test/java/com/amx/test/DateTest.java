package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.amx.jax.grid.GridConstants;
import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.ESDocumentParser;
import com.amx.jax.radar.jobs.customer.CustomerViewTask;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DateTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	private static final Logger LOGGER = LoggerService.getLogger(DateTest.class);

	public static XmlMapper xmlMapper = new XmlMapper();

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {

		Long lastUpdateDateNow = 978287400000L;
		Long lastUpdateDateNowLimit = lastUpdateDateNow + CustomerViewTask.TIME_PAGE_DELTA;

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Range:{} - {}", lastUpdateDateNow, lastUpdateDateNowLimit);
		LOGGER.info("Range:{} - {}", dateString, dateStringLimit);

		CustomerDetailViewRecord doc = new CustomerDetailViewRecord();
		doc.setCreationDate(new Date());

		System.out.println(JsonUtil.getMapper().writeValueAsString(doc));

		System.out.println(ESDocumentParser.toJson(doc));

	}

}
