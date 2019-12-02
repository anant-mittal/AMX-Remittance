package com.amx.jax.sso.server;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.jax.sso.SSOConfig;

@Component
public class OutlookService {
	private static final String authority = "https://login.microsoftonline.com";
	private static final String authorizeUrl = authority + "/common/oauth2/v2.0/authorize";

	private static String[] scopes = {
			"User.Read"
	};

	@Autowired
	SSOConfig ssoConfig;

	private String getScopes() {
		StringBuilder sb = new StringBuilder();
		for (String scope : scopes) {
			sb.append(scope + " ");
		}
		return sb.toString().trim();
	}

	public String getLoginUrl(UUID state, UUID nonce) {

		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(authorizeUrl);
		urlBuilder.queryParam("client_id", ssoConfig.getOutLookAppId());
		urlBuilder.queryParam("redirect_uri", ssoConfig.getOutLookAppUrl());
		urlBuilder.queryParam("response_type", "code id_token");
		urlBuilder.queryParam("scope", getScopes());
		urlBuilder.queryParam("state", state);
		urlBuilder.queryParam("nonce", nonce);
		urlBuilder.queryParam("response_mode", "form_post");

		return urlBuilder.toUriString();
	}
}
