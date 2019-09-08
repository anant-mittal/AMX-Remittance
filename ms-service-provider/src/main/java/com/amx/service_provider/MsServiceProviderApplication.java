package com.amx.service_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ MsServiceProviderConfig.class })

public class MsServiceProviderApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(MsServiceProviderApplication.class, args);
	}

}
