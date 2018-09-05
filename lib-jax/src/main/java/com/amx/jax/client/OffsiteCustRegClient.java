package com.amx.jax.client;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import com.amx.jax.AppConfig;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.CommonRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.rest.RestService;

@Component
public class OffsiteCustRegClient extends AbstractJaxServiceClient implements ICustRegService {
	private static final Logger LOGGER = Logger.getLogger(OffsiteCustRegClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public static final String OFFSITE_CUSTOMER_REG = "/offsite-cust-reg";

	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model) {
		try {
			LOGGER.info("Get all the FieldList");

			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/new-field-list/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(model, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<Map<String, FieldListDto>, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getFieldList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model) {
		try {
			LOGGER.info("Get all the Income Range Details");

			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/incomeRangeList/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(model, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<IncomeRangeDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getIncomeRangeResponse : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model) {
		try {
			LOGGER.info("Get all the Designation List Details");

			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/designationList/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(model, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ArticleDetailsDescDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getDesignationListResponse : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse(CommonRequest model) {
		try {
			LOGGER.info("Get all the Article List Details");

			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/articleList/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(model, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ArticleMasterDescDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getArticleListResponse : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		try {
			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/customer-mobile-email-validate-otp/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(offsiteCustRegModel, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtpForEmailAndMobile : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		try {
			LOGGER.info("Get all the Employment List Details");
			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/employmentTypeList/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendEmploymentTypeList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		try {
			LOGGER.info("Get all the Profession List Details");
			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/professionList/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendProfessionList : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendIdTypes() {
		try {
			LOGGER.info("Get all the Id Type List Details");
			String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/send-id-types/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendIdTypes : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	
	  public AmxApiResponse<List, Object> sendOtpForEmailAndMobile(CustomerPersonalDetail customerPersonalDetail)
	  {
			try {
				LOGGER.info("Get OTP for email and mobile");				
				String url = this.getBaseUrl() + OFFSITE_CUSTOMER_REG + "/customer-mobile-email-send-otp/";
				HttpEntity<Object> requestEntity = new HttpEntity<Object>(customerPersonalDetail,getHeader());
				return restService.ajax(url).post(requestEntity)
						.as(new ParameterizedTypeReference<AmxApiResponse<List, Object>>() {
						});
			} catch (AbstractJaxException ae) {
				throw ae;
			} catch (Exception e) {
				LOGGER.error("exception in sendIdTypes : ", e);
				throw new JaxSystemError();
			} // end of try-catch
		}
	 

}
