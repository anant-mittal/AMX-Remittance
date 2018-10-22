package com.amx.jax.adapter.kwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.amx.jax.adapter.ACardReaderService;
import com.amx.jax.device.CardReader;

@Configuration
@EnableScheduling
@Component
public class KWTCardReaderService extends ACardReaderService {

	@Bean
	public KWTCardReader kwtCardReader() {
		return new KWTCardReader();
	}

	@Override
	public CardReader read() throws InterruptedException {
		return KWTCardReader.read();
	}

	@Override
	public boolean ping() {
		return KWTCardReader.ping();
	}

	@Override
	public boolean start() {
		KWTCardReader.start();
		return true;
	}

}
