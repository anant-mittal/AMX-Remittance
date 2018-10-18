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

	public static boolean ENABLE_CLI = false;
	public static boolean ENABLE_GUI = false;

	public static SWAdapterGUI GUI = null;

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			ENABLE_CLI = true;
			System.out.println("Starting Commond Line Tool");
			SpringApplication app = new SpringApplication(SWAdapterGUI.class);
			app.setBannerMode(Banner.Mode.OFF);
			GUI = new SWAdapterGUI();
			app.run(args);
		} else {
			ENABLE_GUI = true;
			System.out.println("Starting GUI");
			ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SWAdapterGUI.class).headless(false)
					.run(args);
			EventQueue.invokeLater(() -> {
				SWAdapterGUI ex = ctx.getBean(SWAdapterGUI.class);
				GUI = ex;
				ex.setVisible(true);
			});
		}

	}
}