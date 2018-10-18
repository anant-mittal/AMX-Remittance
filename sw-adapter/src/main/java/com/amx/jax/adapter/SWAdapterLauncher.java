package com.amx.jax.adapter;

import java.awt.EventQueue;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.amx.jax")
public class SWAdapterLauncher {

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			SpringApplication app = new SpringApplication(SWAdapterGUI.class);
			app.setBannerMode(Banner.Mode.OFF);
			app.run(args);
		} else {
			ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SWAdapterGUI.class).headless(false)
					.run(args);
			EventQueue.invokeLater(() -> {
				SWAdapterGUI ex = ctx.getBean(SWAdapterGUI.class);
				ex.setVisible(true);
			});
		}

	}
}