package com.amx.jax.scope.sample;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.scope.TenantLibs;

@Service
public class CalcLibs extends TenantLibs<CalcLib> {

	@Autowired
	public CalcLibs(List<CalcLib> libs) {
		super(libs);
	}

}
