package com.amx.jax.adapter;

import java.awt.EventQueue;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.amx.jax")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class

		, JmxAutoConfiguration.class

		, WebSocketAutoConfiguration.class

		, SitePreferenceAutoConfiguration.class

		, SpringApplicationAdminJmxAutoConfiguration.class

		, ValidationAutoConfiguration.class

})
public class SWAdapterLauncher {

	public static boolean ENABLE_CLI = false;
	public static boolean ENABLE_GUI = false;

	public static void main(String[] args) throws Exception {
		ENABLE_GUI = true;
		System.out.println("Starting GUI");
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SWAdapterLauncher.class).headless(false)
				.run(args);
		EventQueue.invokeLater(() -> {
			SWAdapterGUI ex = ctx.getBean(SWAdapterGUI.class);
			SWAdapterGUI.CONTEXT = ex;
			ex.setVisible(true);
		});

	}

}