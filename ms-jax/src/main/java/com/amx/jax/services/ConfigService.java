package com.amx.jax.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.services.AbstractService;

@Service
public class ConfigService extends AbstractService {

	@Autowired
	OtpSettings otpsettings;

	public OtpSettings getOtpSettings() {
		return this.otpsettings;
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
