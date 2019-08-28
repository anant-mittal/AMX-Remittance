package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.amx.jax.client.snap.SnapModels.AggregationField;
import com.amx.jax.client.snap.SnapModels.Aggregations;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.FileUtil;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class SnapTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	private static final Logger LOGGER = LoggerService.getLogger(SnapTest.class);

	public static XmlMapper xmlMapper = new XmlMapper();

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
				.readFile("file://" + System.getProperty("user.dir") + "/src/test/java/com/amx/test/sample.json");

		SnapModelWrapper wrapper = new SnapModelWrapper(json);
		AggregationField x = wrapper.getAggregations().field("tranx");
		System.out.println("x=====" + JsonUtil.toJson(x));
		List<Aggregations> xl = x.getBuckets();
		System.out.println("[x1]=====" + JsonUtil.toJson(xl));
		Aggregations x1 = x.getBuckets().get(0);
		System.out.println("x1=====" + JsonUtil.toJson(x1));
		AggregationField x2 = x1.field("channel");
		System.out.println("x2=====" + JsonUtil.toJson(x2));
		System.out.println("x2.key=====" + x2.getBuckets().get(0).getKey());
	}

}
