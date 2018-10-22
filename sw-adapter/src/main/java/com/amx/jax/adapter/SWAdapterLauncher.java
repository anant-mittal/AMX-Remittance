package com.amx.jax.adapter;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.amx.utils.FileUtil;

@SpringBootApplication
@ComponentScan("com.amx.jax")
public class SWAdapterLauncher {

	public static boolean ENABLE_CLI = false;
	public static boolean ENABLE_GUI = false;

	public static void main(String[] args) throws Exception {
		boolean cli = false;

		if (args.length > 0) {
			for (String str : args) {
				if (str.contains("-cli")) {
					cli = true;
				}
			}
		}

		if (cli) {
			ENABLE_CLI = true;
			System.out.println("Starting Commond Line Tool with args" + args[0]);
			SpringApplication app = new SpringApplication(SWAdapterGUI.class);
			app.setBannerMode(Banner.Mode.OFF);
			SWAdapterGUI.CONTEXT = new SWAdapterGUI();
			app.run(args);
		} else {
			ENABLE_GUI = true;
			System.out.println("Starting GUI");
			ConfigurableApplicationContext ctx = new SpringApplicationBuilder(SWAdapterGUI.class).headless(false)
					.run(args);
			EventQueue.invokeLater(() -> {
				SWAdapterGUI ex = ctx.getBean(SWAdapterGUI.class);
				SWAdapterGUI.CONTEXT = ex;
				ex.setVisible(true);
			});
		}

	}

	/**
	private String allPassword = "123";

	@Bean
	public String templatetKeyStore(RestTemplate restTemplate) throws KeyManagementException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
		File fl = FileUtil.getExternalFile("ext-resources/truststore.jks");
		SSLContext sslContext = SSLContextBuilder.create()
				.loadKeyMaterial(fl, allPassword.toCharArray(), allPassword.toCharArray())
				.loadTrustMaterial(fl, allPassword.toCharArray()).build();

		HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();

		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		return allPassword;
	}
	**/
}