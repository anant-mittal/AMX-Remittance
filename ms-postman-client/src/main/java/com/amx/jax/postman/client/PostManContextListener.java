package com.amx.jax.postman.client;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Channel;
import com.amx.jax.postman.model.Message;
import com.bootloaderjs.config.AppConfig;

public class PostManContextListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		AppConfig appConfig = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())
				.getBean(AppConfig.class);

		if (!appConfig.isDebug()) {
			PostManService postManService = WebApplicationContextUtils
					.getRequiredWebApplicationContext(sce.getServletContext()).getBean(PostManService.class);
			Message msg = new Message();
			msg.setChannel(Channel.DEPLOYER);
			msg.setMessage("App : " + appConfig.getAppName());
			msg.addLine("Status : server is started...");
			try {
				postManService.notifySlack(msg);
			} catch (PostManException e) {
				LOGGER.error("Exception while Sending Server Up Status", e);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		AppConfig appConfig = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())
				.getBean(AppConfig.class);

		if (!appConfig.isDebug()) {
			PostManService postManService = WebApplicationContextUtils
					.getRequiredWebApplicationContext(sce.getServletContext()).getBean(PostManService.class);
			Message msg = new Message();
			msg.setChannel(Channel.DEPLOYER);
			msg.setMessage("App : " + appConfig.getAppName());
			msg.addLine("Status : server is shutdown...");
			try {
				postManService.notifySlack(msg);
			} catch (PostManException e) {
				LOGGER.error("Exception while Sending Server Down Status", e);
			}
		}
	}

}
