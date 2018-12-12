package com.amx.jax.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.repository.JaxConfigRepository;
import com.amx.jax.util.ConverterUtil;

@Service
public class JaxConfigService extends AbstractService {

	@Autowired
	JaxConfigRepository repo;

	@Autowired
	ConverterUtil converterUtil;

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

	public void saveConfig(String type, String value) {
		JaxConfig jaxConfig = repo.findByType(type);
		if (jaxConfig == null) {
			jaxConfig = new JaxConfig(type, value);
		} else {
			jaxConfig.setValue(value);
		}
		repo.save(jaxConfig);
	}

	public JaxConfig getConfig(String type) {
		JaxConfig jaxConfig = repo.findByType(type);
		return jaxConfig;
	}
	
	public String getConfigValue(String key, String defaultValue) {
		JaxConfig jaxConfig = repo.findByType(key);
		if (jaxConfig != null && jaxConfig.getValue() != null) {
			return jaxConfig.getValue();
		} else {
			return defaultValue;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public ApiResponse saveOtpSettings(OtpSettings settings) {
		ApiResponse response = getBlackApiResponse();
		this.saveConfig(OtpSettings.getType(), converterUtil.marshall(settings));
		response.getData().getValues().add(new BooleanResponse());
		return response;
	}
}
