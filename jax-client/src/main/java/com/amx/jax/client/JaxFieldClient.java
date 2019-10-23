package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.JAX_FIELD_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.GetJaxFieldRequest;
import com.amx.amxlib.model.JaxCondition;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constants.JaxFieldEntity;
import com.amx.jax.rest.RestService;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;

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

		try {
			LOGGER.info("Get getJaxFieldsForEntity ");

			String url = this.getBaseUrl() + JAX_FIELD_ENDPOINT + "/get";
			HttpEntity<GetJaxFieldRequest> requestEntity = new HttpEntity<GetJaxFieldRequest>(request, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<JaxConditionalFieldDto>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getJaxFieldsForEntity : ", e);
			throw new JaxSystemError();
		} // end of try-catch

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

		try {
			LOGGER.info("Get getJaxFieldsForEntity beneCountryId= " + beneCountryId);
			JaxCondition condition = new JaxCondition("bene-country-id", beneCountryId.toString());
			GetJaxFieldRequest request = new GetJaxFieldRequest(condition, JaxFieldEntity.BENEFICIARY);
			String url = this.getBaseUrl() + JAX_FIELD_ENDPOINT + "/get";
			HttpEntity<GetJaxFieldRequest> requestEntity = new HttpEntity<GetJaxFieldRequest>(request, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<JaxConditionalFieldDto>>() {
					});

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getJaxFieldsForEntity : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}
}
