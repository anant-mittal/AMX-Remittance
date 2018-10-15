package com.amx.jax.adapter.kwt;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KWTConfiguration {

	//@Bean
	public KWTCardReader kwtCardReader() {
		return new KWTCardReader();
	}

}
