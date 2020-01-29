package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.JAX_FIELD_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.GetJaxFieldRequest;
import com.amx.amxlib.model.JaxCondition;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldEntity;
import com.amx.jax.rest.RestService;

@Component
public class JaxFieldClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(JaxFieldClient.class);

	@Autowired
	private RestService restService;

	/**
	 * @return
	 *         <p>
	 *         Returns the dynamic fields based upon passed conditions
	 *         </p>
	 * 
	 */
	public ApiResponse<JaxConditionalFieldDto> getJaxFieldsForEntity(GetJaxFieldRequest request) {

					String url = this.getBaseUrl() + JAX_FIELD_ENDPOINT + "/get";
			HttpEntity<GetJaxFieldRequest> requestEntity = new HttpEntity<GetJaxFieldRequest>(request, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<JaxConditionalFieldDto>>() {
					});
		
	

	}

	/**
	 * @return
	 *         <p>
	 *         Returns the dynamic fields of beneficiary based upon beneficiary
	 *         country Id passed
	 *         </p>
	 * @param beneCountryId
	 *            - country id returned from list country api
	 * 
	 */
	public ApiResponse<JaxConditionalFieldDto> getDynamicFieldsForBeneficiary(BigDecimal beneCountryId) {

	
			LOGGER.info("Get getJaxFieldsForEntity beneCountryId= " + beneCountryId);
			JaxCondition condition = new JaxCondition("bene-country-id", beneCountryId.toString());
			GetJaxFieldRequest request = new GetJaxFieldRequest(condition, JaxFieldEntity.BENEFICIARY);
			String url = this.getBaseUrl() + JAX_FIELD_ENDPOINT + "/get";
			HttpEntity<GetJaxFieldRequest> requestEntity = new HttpEntity<GetJaxFieldRequest>(request, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<JaxConditionalFieldDto>>() {
					});

	}
}
