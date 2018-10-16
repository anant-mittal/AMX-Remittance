package com.amx.jax.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.services.AbstractService;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeviceService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(DeviceService.class);

	@Autowired
	DeviceDao deviceDao;

	public AmxApiResponse<DeviceDto, Object> registerDevice(DeviceRegistrationRequest request) {
		logger.info("In register device with request: {}", request);
		DeviceDto newDevice = deviceDao.saveDevice(request);
		logger.info("device registered with id: {}", newDevice.getRegistrationId());
		return AmxApiResponse.build(newDevice);
	}
}
