package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.FileUtil;
import com.amx.utils.JsonUtil;

public class EsToCSVTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	private static final Logger LOGGER = LoggerService.getLogger(EsToCSVTest.class);

	public static class FlatCell {
		public String key;
		public long count;
		public Object value;

	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		System.out.println(System.getProperty("user.dir"));
		String json = FileUtil
				.readFile(FileUtil.normalize(
						"file://" + System.getProperty("user.dir") + "/ext-resources/es-output-short.json"));
		SnapModelWrapper wrapper = new SnapModelWrapper(json);
		List<Map<String, Object>> x = wrapper.getAggregations().toBulk(" ");
		
		for (Map<String, Object> map : x) {
			System.out.println(JsonUtil.toJson(map));
		}

	}

}
