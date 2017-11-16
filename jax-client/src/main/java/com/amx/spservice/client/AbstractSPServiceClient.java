package com.amx.spservice.client;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public abstract class AbstractSPServiceClient {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	@Qualifier("base_url")
	protected URL baseUrl;

}
