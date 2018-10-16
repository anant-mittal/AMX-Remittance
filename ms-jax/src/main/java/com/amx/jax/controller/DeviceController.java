package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.service.DeviceService;

@RestController
@RequestMapping(META_API_ENDPOINT + "/device")
public class DeviceController {

	@Autowired
	DeviceService deviceService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public AmxApiResponse<DeviceDto, Object> registerDevice(@Valid @RequestBody DeviceRegistrationRequest request) {

		return deviceService.registerDevice(request);
	}
}
