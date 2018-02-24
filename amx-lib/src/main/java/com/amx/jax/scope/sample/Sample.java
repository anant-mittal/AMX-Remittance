package com.amx.jax.scope.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sample implements CalcLib {

	@Autowired
	CalcLibs calcLibs;

	@Override
	public String getRSName() {
		return calcLibs.get().getRSName();
	}

}
