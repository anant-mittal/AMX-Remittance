package com.amx.jax.bot;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.utils.FileUtil;

@Component
public class Chatbot {

	private static final Logger LOGGER = LoggerFactory.getLogger(Chatbot.class);
	private static Chat CHAT_SESSION;
	private static boolean IS_TRAINED = false;

	static String botName = "super";

	@Value("${jax.bot.enabled}")
	boolean botEnabled;

	public static void initChatSession(String newNotName) {
		train(newNotName);
		if (CHAT_SESSION == null) {
			LOGGER.info("initChatSession");
			String resourcesPath = getResourcesPath();
			MagicBooleans.trace_mode = false;// LOGGER.isDebugEnabled();
			Bot bot = new Bot(newNotName, resourcesPath);
			CHAT_SESSION = new Chat(bot);
			bot.brain.nodeStats();
		}
	}

	public static String sanaitze(String text) {
		if ("I have no answer for that.".equals(text)) {
			return "NO_RESPONSE";
		}
		return text;
	}

	public static String response(String number, String textLine) {
		initChatSession("duper");
		return responseInternal(number, textLine, null);
	}

	public String response(String number, String textLine, String defReply) {
		if (botEnabled) {
			initChatSession("duper");
		}
		return responseInternal(number, textLine, defReply);
	}

	private static String responseInternal(String number, String textLine, String defReply) {
		try {
			if (CHAT_SESSION == null) {
				return defReply;
			}

			if ((textLine == null) || (textLine.length() < 1))
				textLine = MagicStrings.null_input;
			String request = textLine;
			String response = CHAT_SESSION.multisentenceRespond(request);
			while (response.contains("&lt;"))
				response = response.replace("&lt;", "<");
			while (response.contains("&gt;"))
				response = response.replace("&gt;", ">");
			return sanaitze(response);
		} catch (Exception e) {
			LOGGER.error("Error", e);
		}
		return textLine;
	}

	public static String responseWithSession(String number, String textLine) {
		try {

			String resourcesPath = getResourcesPath();
			System.out.println(resourcesPath);
			MagicBooleans.trace_mode = LOGGER.isDebugEnabled();
			Bot bot = new Bot(botName, resourcesPath);
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

	public static void train(String newNotName) {
		try {
			if (IS_TRAINED) {
				return;
			}
			LOGGER.info("TRAINING");
			String resourcesPath = getResourcesPath();
			MagicBooleans.trace_mode = false;
			Bot bot = new Bot(newNotName, resourcesPath);
			bot.writeAIMLFiles();
			IS_TRAINED = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getResourcesPath() {

		return FileUtil.getExternalFile("ext-resources").getAbsolutePath();
		/*
		 * File currDir = new File("."); String path = currDir.getAbsolutePath(); path =
		 * path.substring(0, path.length() - 2); System.out.println(path); String
		 * resourcesPath = path + File.separator + "src" + File.separator + "main" +
		 * File.separator + "resources"; return resourcesPath;
		 */
	}
}
