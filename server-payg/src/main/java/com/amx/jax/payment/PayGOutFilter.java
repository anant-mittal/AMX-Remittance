package com.amx.jax.payment;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;

import com.amx.jax.rest.AppRequestContextOutFilter;

@Primary
@Component
public class PayGOutFilter implements AppRequestContextOutFilter {

	@Override
	public void appRequestContextOutFilter(HttpRequest request) {
		// AppContextUtil.getUserClient().setAppType(appType);
	}

}
