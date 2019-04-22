package com.amx.jax.tpc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.service.ITpcService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;

@RestController
@RequestMapping(ITpcService.Path.SERVICE_PREFIX)
public class TpcController implements ITpcService {

	@Override
	public AmxApiResponse<BoolRespModel, Object> generatePassword(String clientId, String actualPassword) {
		return null;
	}
}
