package com.amx.jax.postman.converter.jasper;

//import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * The Class JasperRerportsSimpleConfig.
 */
@Configuration
public class JasperRerportsSimpleConfig {

	// @Bean
	// public DataSource dataSource() {
	// return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
	// .addScript("classpath:employee-schema.sql").build();
	// }

	/**
	 * Report filler.
	 *
	 * @return the simple report filler
	 */
	@Bean
	public SimpleReportFiller reportFiller() {
		return new SimpleReportFiller();
	}

	/**
	 * Report exporter.
	 *
	 * @return the simple report exporter
	 */
	@Bean
	public SimpleReportExporter reportExporter() {
		return new SimpleReportExporter();
	}

}