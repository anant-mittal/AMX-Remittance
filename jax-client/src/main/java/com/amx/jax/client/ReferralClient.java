package com.amx.jax.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.LinkDTO;
import com.amx.amxlib.model.ReferralDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.RestService;

import static com.amx.amxlib.constant.ApiEndpoint.REFER_API_ENDPOINT;;

@Component
public class ReferralClient extends AbstractJaxServiceClient {
	
	@Autowired
	RestService restService;
	
	public AmxApiResponse<ReferralDTO,Object> getRefferal() {
		try {			
			return restService.ajax(appConfig.getJaxURL()).path(REFER_API_ENDPOINT + "/get/")					
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ReferralDTO,Object>>() {
					});
		} catch (Exception e) {
			return JaxSystemError.evaluate(e);
		}
	}
	
	public AmxApiResponse<LinkDTO,Object> getReferralLink(LinkDTO linkDTO) {
		try {			
			return restService.ajax(appConfig.getJaxURL()).path(REFER_API_ENDPOINT + "/link/make")					
					.meta(new JaxMetaInfo()).post(linkDTO)
					.as(new ParameterizedTypeReference<AmxApiResponse<LinkDTO,Object>>() {
					});
		} catch (Exception e) {
			return JaxSystemError.evaluate(e);
		}
	}
	
	public AmxApiResponse<LinkDTO,Object> updateReferralLink(LinkDTO linkDTO) {
		try {			
			return restService.ajax(appConfig.getJaxURL()).path(REFER_API_ENDPOINT + "/link/open")					
					.meta(new JaxMetaInfo()).post(linkDTO)
					.as(new ParameterizedTypeReference<AmxApiResponse<LinkDTO,Object>>() {
					});
		} catch (Exception e) {
			return JaxSystemError.evaluate(e);
		}
	}
}
