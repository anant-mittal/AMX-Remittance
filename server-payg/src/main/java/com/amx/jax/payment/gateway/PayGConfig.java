package com.amx.jax.payment.gateway;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.PaygErrorMasterDTO;

@Component
public class PayGConfig {

	@Value("${knet.callback.url}")
	String serviceCallbackUrl;

	private Map<String,PaygErrorMasterDTO> errorCodeMap;
	
	public String getServiceCallbackUrl() {
		return serviceCallbackUrl;
	}
	
    public Map<String, PaygErrorMasterDTO> getErrorCodeMap() {
        return errorCodeMap;
    }

    public void setErrorCodeMap(Map<String, PaygErrorMasterDTO> errorCodeMap) {
        this.errorCodeMap = errorCodeMap;
	}
}
