package com.amx.jax.adapter;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
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
	public static File ADAPTER_FOLDER = null;

	public static int PORT;

	@Value("${server.port}")
	private int port;

	public static void main(String[] args) throws Exception {

		final Class<?> referenceClass = SWAdapterLauncher.class;
		final URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();
		try {
			ADAPTER_FOLDER = new File(url.toURI()).getParentFile();
		} catch (final URISyntaxException e) {
		}
		ENABLE_GUI = true;
		System.out.println("Starting GUI");
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SWAdapterLauncher.class).headless(false)
				.run(args);
		EventQueue.invokeLater(() -> {
			SWAdapterGUI ex = ctx.getBean(SWAdapterGUI.class);
			SWAdapterGUI.CONTEXT = ex;
			SWAdapterGUI.ADAPTER_FOLDER = ADAPTER_FOLDER;
			ex.setVisible(true);
			// opnePage();
		});
	}

	public static void opnePage() {
		try {
			URI homepage = new URI("http://127.0.0.1:" + PORT + "/");
			Desktop.getDesktop().browse(homepage);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void postConstruct() {
		PORT = port;
	}

}