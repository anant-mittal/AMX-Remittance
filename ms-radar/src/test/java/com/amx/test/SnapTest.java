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
import com.axx.jax.table.PivotTable;
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
				.readFile(FileUtil.normalize(
						"file://" + System.getProperty("user.dir") + "/src/test/java/com/amx/test/sample.json"));
		SnapModelWrapper x = new SnapModelWrapper(json);
		int level = 0;
		if (level  >= 0) {
			List<Map<String, List<String>>> p = x.getPivot();
			List<Map<String, Object>> inputBulk = x.getAggregations().toBulk();

			for (Map<String, List<String>> pivot : p) {
				level--;
				if (level < 0)
					break;
				PivotTable table = new PivotTable(
						pivot.get("rows"), pivot.get("cols"),
						pivot.get("vals"), pivot.get("aggs"), pivot.get("alias"),
						pivot.get("computed"), pivot.get("noncomputed"),
						 pivot.get("colgroups"));
				for (Map<String, Object> map : inputBulk) {
					table.add(map);
				}
				table.calculate();
				inputBulk = table.toBulk();
				// break;
			}
			x.toMap().put("bulk", inputBulk);
			x.removeAggregations();
		}
		
		System.out.println("====="+JsonUtil.toJson(x.get("bulk")));
	}

}
