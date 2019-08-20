package com.amx.jax.adapter;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang.SystemUtils;
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

import com.amx.utils.FileUtil;

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

	public static int PORT;

	@Value("${server.port}")
	private int port;

	public static void main(String[] args) throws Exception {
		ENABLE_GUI = true;
		System.out.println("Starting GUI");
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SWAdapterLauncher.class).headless(false)
				.run(args);
		EventQueue.invokeLater(() -> {
			SWAdapterGUI ex = ctx.getBean(SWAdapterGUI.class);
			SWAdapterGUI.CONTEXT = ex;

			Image icon = new ImageIcon(FileUtil.getResource("logo.png", SWAdapterLauncher.class)).getImage();
			if (SystemUtils.IS_OS_MAC) {
				try {
					// com.apple.eawt.Application.getApplication().setDockIconImage(icon);
					Class.forName("com.apple.eawt.Application");
					// Test whether the compilation has worked
					Class<?> applClass = Class.forName("com.apple.eawt.Application");
					// application.setEnabledPreferencesMenu(true);
					Method getApplication = applClass.getMethod("getApplication");
					Method setDockIconImage = applClass.getMethod("setDockIconImage", new Class[] { Image.class });
					Object app = getApplication.invoke(null);
					setDockIconImage.invoke(app, icon);

				} catch (Exception e) {
					System.out.println("Not Able to Set Icon for Mac Device");
				}
			}
			ex.setIconImage(icon);
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