package com.amx.jax.tpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.service.ITpcService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.response.tpc.TpcGenerateClientSecretResponse;

@RestController
@RequestMapping(ITpcService.Path.SERVICE_PREFIX)
public class TpcController implements ITpcService {

	@Autowired
	TpcService tpcService;

	@Override
	@RequestMapping(path = Path.GENERATE_SECRET)
	public AmxApiResponse<TpcGenerateClientSecretResponse, Object> generateSecret(@RequestParam String clientId,
			@RequestParam String actualSecret) {
		TpcGenerateClientSecretResponse response = tpcService.generateSecret(clientId, actualSecret);
		return AmxApiResponse.build(response);
	}
}
