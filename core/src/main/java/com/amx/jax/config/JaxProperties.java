package com.amx.jax.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaxProperties {

	@Value("#{'${support.soa.email}'.split(',')}")
	List<String> supportSoaEmail;
	@Value("${jax.tpc.secret}")
	String tpcSecret;

	public List<String> getSupportSoaEmail() {
		return supportSoaEmail;
	}

	public void setSupportSoaEmail(List<String> supportSoaEmail) {
		this.supportSoaEmail = supportSoaEmail;
	}

	public String getTpcSecret() {
		return tpcSecret;
	}

	public void setTpcSecret(String tpcSecret) {
		this.tpcSecret = tpcSecret;
	}

}
