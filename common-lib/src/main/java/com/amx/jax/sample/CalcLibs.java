package com.amx.jax.sample;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.scope.TenantContext;

@Service
public class CalcLibs extends TenantContext<CalcLib> {

	@Autowired
	public CalcLibs(List<CalcLib> libs) {
		super(libs);
	}

}
