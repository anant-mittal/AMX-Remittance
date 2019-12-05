package com.amx.service_provider.api_gates.common;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.amg.vcHomeSend.ows.HWSBinding_2_3Stub;
import com.amg.vcHomeSend.ows.RemittanceRequest;
import com.amg.vcHomeSend.ows.RemittanceResponse;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.service_provider.api_gates.vintaja.VintajaUtils;
import com.amx.service_provider.dbmodel.webservice.ExOwsLoginCredentials;
import com.amx.utils.JsonUtil;

public class TimeOutResponse implements Callable<Object>{
	HashMap<String, Object> request;
	int i = 0,callRespone = 3;
	
	Logger logger = Logger.getLogger(getClass());
	
	public TimeOutResponse(HashMap<String, Object> request) {
		super();
		this.request = request;
	}

	@Override
	public Object call() throws Exception {
		Object object = null;
		if(getRequest() != null && !getRequest().isEmpty()){
			String bankCode = (String) getRequest().get("RoutingBankCode");
			if(bankCode.equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.HOME.name())){
				RemittanceResponse remittanceResponse = null;
				HWSBinding_2_3Stub hwsBinding_2_3Stub = (HWSBinding_2_3Stub) getRequest().get("HWSBinding_2_3Stub");
				RemittanceRequest remittanceRequest = (RemittanceRequest) getRequest().get("RemittanceRequest");
				while (i < callRespone) {
					remittanceResponse = hwsBinding_2_3Stub.remittance(remittanceRequest);
					if (remittanceResponse != null) {
						if(remittanceResponse.getHsTransactionId() != null && remittanceResponse.getTransactionId() != null) {
							logger.info("response : " + JsonUtil.toJson(remittanceResponse));
							object = remittanceResponse;
							break;
						}else {
							TimeUnit.SECONDS.sleep(10);
							++i;
							if (i == callRespone) {
								throw new TimeoutException("enable to fetch response from Home Send");
							}
						}
					} else {
						TimeUnit.SECONDS.sleep(10);
						++i;
						if (i == callRespone) {
							throw new TimeoutException("enable to fetch response from Home Send");
						}
					}
				}
			}else if(bankCode.equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.VINTJA.name())){
				String api_input = (String) getRequest().get("api_input");
				String signed_payload = (String) getRequest().get("signed_payload");
				ExOwsLoginCredentials owsLoginCredentialsObject = (ExOwsLoginCredentials) getRequest().get("owsLoginCredentialsObject");
				String ws_call_type = (String) getRequest().get("ws_call_type");
				while (i < callRespone) {
					String response_string = VintajaUtils.call_vintaja_api(api_input, signed_payload, owsLoginCredentialsObject, ws_call_type);
					if (response_string != null && !response_string.equalsIgnoreCase("")) {
						object = response_string;
						break;
					} else {
						TimeUnit.SECONDS.sleep(10);
						++i;
						if (i == callRespone) {
							throw new TimeoutException("enable to fetch response from Vintaja");
						}
					}
				}
			}else{
				// failed
			}
		}
		
		return object;
	}

	public HashMap<String, Object> getRequest() {
		return request;
	}
	public void setRequest(HashMap<String, Object> request) {
		this.request = request;
	}
	
}
