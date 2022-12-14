package com.amx.jax.radar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

/**
 * The Class PostManConfig.
 */
@TenantScoped
@Component
public class RadarConfig {

	public static final String CE_RATE_SCRAPPER_AND_ES_AND_KWT = "${jax.jobs.scrapper.rate:true} " +
			"&& ${elasticsearch.enabled:true} " +
			"&& '${default.tenant}'=='KWT'";

	public static final String CE_RATE_SCRAPPER_AND_ES_AND_ANY_TNT = "${jax.jobs.scrapper.rate:true} " +
			"&& ${elasticsearch.enabled:true} ";

	public static final String CE_RATE_SYNC_AND_ES = "${jax.jobs.rate:true} " +
			"&& ${elasticsearch.enabled:true} ";

	public static final String CE_CUSTOMER_SYNC_AND_ES = "${jax.jobs.customer:true} " +
			"&& ${elasticsearch.enabled:true} ";

	public static final String CE_TRNX_SYNC_AND_ES = "${jax.jobs.trnx:true} " +
			"&& ${elasticsearch.enabled:true} ";

	@TenantValue("${company.name}")
	private String companyName;

	@TenantValue("${company.website.url}")
	private String companyWebSiteUrl;

	@TenantValue("${company.idtype}")
	private String companyIDType;

	@Value("${jax.jobs.customer.counter}")
	private String jobsCustomerVersion;

	@Value("${jax.jobs.trnx.counter}")
	private String jobsTrnxVersion;

	@Value("${jax.jobs.rate.counter}")
	private String jobsRateVersion;
	
	@Value("${jax.jobs.trnx.night}")
	boolean jobTranxNightEnabled;
	
	@Value("${jax.jobs.trnx.day}")
	boolean jobTranxDayEnabled;
	
	@Value("${jax.jobs.wa.fail.retry}")
	boolean jobWAFailRetryEnabled;
	
	@TenantValue("${goaml.fiu.zipfile.location}")
	private static String jobFIUzipLocationEnabled;
	

	public boolean isJobWAFailRetryEnabled() {
		return jobWAFailRetryEnabled;
	}

	public boolean isJobTranxDayEnabled() {
		return jobTranxDayEnabled;
	}

	public boolean isJobTranxNightEnabled() {
		return jobTranxNightEnabled;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyWebSiteUrl() {
		return companyWebSiteUrl;
	}

	public String getCompanyIDType() {
		return companyIDType;
	}

	public String getJobsCustomerVersion() {
		return jobsCustomerVersion;
	}

	public String getJobsTrnxVersion() {
		return jobsTrnxVersion;
	}

	public String getJobsRateVersion() {
		return jobsRateVersion;
	}

	public static String getJobFIUzipLocationEnabled() {
		return jobFIUzipLocationEnabled;
	}

	public static void setJobFIUzipLocationEnabled(String jobFIUzipLocationEnabled) {
		RadarConfig.jobFIUzipLocationEnabled = jobFIUzipLocationEnabled;
	}

	
	

}
