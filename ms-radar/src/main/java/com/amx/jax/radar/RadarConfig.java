package com.amx.jax.radar;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncJobs;
import com.amx.jax.scope.TenantProperties;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.utils.ArgUtil;

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

	@Value("${jax.jobs.trnx.day}")
	boolean jobTranxDayEnabled;

	public boolean isJobTranxDayEnabled() {
		return jobTranxDayEnabled;
	}

	public boolean isJobTranxNightEnabled() {
		return jobTranxNightEnabled;
	}

	@Value("${jax.jobs.trnx.night}")
	boolean jobTranxNightEnabled;

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

}
