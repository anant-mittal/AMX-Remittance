package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;

import com.amx.jax.client.snap.SnapModels.AggregationField;
import com.amx.jax.client.snap.SnapModels.Aggregations;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.FileUtil;
import com.amx.utils.JsonUtil;

import net.minidev.json.JSONUtil;

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
						"file://" + System.getProperty("user.dir") + "/src/test/java/com/amx/test/es-output.json"));
		SnapModelWrapper wrapper = new SnapModelWrapper(json);

		String jsFile = FileUtil
				.readFile(FileUtil.normalize(
						"file://" + System.getProperty("user.dir") + "/src/main/resources/static/tabify.js"));

		jsFile = jsFile + " function cb(x){ return tabify(" + json + "); }";
		runScript(jsFile, "cb", new Object[] { "AAAAAAAAnH0=" });

	}

	private static void runScript(String javaScriptCode, String functionNameInJavaScriptCode, Object[] params) {
		org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
		rhino.setOptimizationLevel(-1);
		try {
			Scriptable scope = rhino.initStandardObjects();

			rhino.evaluateString(scope, javaScriptCode, "JavaScript", 1, null);

			// Get the functionName defined in JavaScriptCode
			Object obj = scope.get(functionNameInJavaScriptCode, scope);

			if (obj instanceof org.mozilla.javascript.Function) {
				org.mozilla.javascript.Function jsFunc = (org.mozilla.javascript.Function) obj;

				// Call the function with params
				Object jsResult = jsFunc.call(rhino, scope, scope, params);
				System.out.println(JsonUtil.toJson(jsResult));
				// Parse the jsResult object to a String
				String result = org.mozilla.javascript.Context.toString(jsResult);
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			org.mozilla.javascript.Context.exit();
		}

	}

}
