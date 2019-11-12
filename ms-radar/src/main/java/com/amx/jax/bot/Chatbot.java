package com.amx.jax.bot;

import java.io.File;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chatbot {

	private static final Logger LOGGER = LoggerFactory.getLogger(Chatbot.class);

	static String botName = "super";

	public static String response(String number, String textLine) {
		try {

			String resourcesPath = getResourcesPath();
			System.out.println(resourcesPath);
			MagicBooleans.trace_mode = LOGGER.isDebugEnabled();
			Bot bot = new Bot("super", resourcesPath);
			Chat chatSession = new Chat(bot);
			bot.brain.nodeStats();

			if ((textLine == null) || (textLine.length() < 1))
				textLine = MagicStrings.null_input;

			if (textLine.equals("bye")) {
				bot.writeQuit();
				System.exit(0);
			} else {
				String request = textLine;
				LOGGER.debug(
						"STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0)
								+ ":TOPIC=" + chatSession.predicates.get("topic"));
				String response = chatSession.multisentenceRespond(request);
				while (response.contains("&lt;"))
					response = response.replace("&lt;", "<");
				while (response.contains("&gt;"))
					response = response.replace("&gt;", ">");
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Thank you.";
	}

	private static String getResourcesPath() {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		path = path.substring(0, path.length() - 2);
		System.out.println(path);
		String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
		return resourcesPath;
	}
}
