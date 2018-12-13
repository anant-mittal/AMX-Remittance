package com.amx.jax.postman.client;

import java.awt.Color;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.amx.jax.AppConfig;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;

public class PostManContextListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManContextListener.class);

	public PostManService getService(ServletContextEvent sce) {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())
				.getBean(PostManService.class);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			AppConfig appConfig = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())
					.getBean(AppConfig.class);

			if (!appConfig.isDebug()) {
				PostManService postManService = getService(sce);
				Notipy msg = new Notipy();
				msg.setChannel(Channel.DEPLOYER);
				msg.setColor("#36a64f");
				msg.setMessage("App : " + appConfig.getAppName());
				msg.addLine("Status : server is started...");
				LOGGER.info("{} : Status : server is started...", appConfig.getAppName());

				postManService.notifySlack(msg);
			}

		} catch (Exception e) {
			LOGGER.error("Exception while Sending Server Up Status", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			AppConfig appConfig = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())
					.getBean(AppConfig.class);
			if (!appConfig.isDebug()) {
				PostManService postManService = getService(sce);
				Notipy msg = new Notipy();
				msg.setChannel(Channel.DEPLOYER);
				msg.setColor("danger");
				msg.setMessage("App : " + appConfig.getAppName());
				msg.addLine("Status : server is shutdown...");
				LOGGER.info("{} : Status : server is shutdown...", appConfig.getAppName());
				postManService.notifySlack(msg);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while Sending Server Down Status", e);
		}
	}

}
