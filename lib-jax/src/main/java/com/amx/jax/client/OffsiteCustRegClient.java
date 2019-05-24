package com.amx.jax.client;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.CustomerCredential;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.customer.ICustRegService;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.CardDetail;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.rest.RestService;
import com.amx.jax.scope.TenantContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OffsiteCustRegClient implements ICustRegService {

	private static final Logger LOGGER = LoggerService.getLogger(OffsiteCustRegClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public HttpHeaders getHeader() {

		HttpHeaders headers = new HttpHeaders();
		try {

			JaxMetaInfo metaInfo = new JaxMetaInfo();
			metaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
			metaInfo.setTenant(TenantContextHolder.currentSite());
			headers.add("meta-info", new ObjectMapper().writeValueAsString(metaInfo.copy()));
		} catch (JsonProcessingException e) {
			LOGGER.error("error in getheader of jaxclient", e);
		}
		return headers;
	}

	public static final String OFFSITE_CUSTOMER_REG = "/offsite-cust-reg";

	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.GET_DYNAMIC_FIELDS).post(model)
					.as(new ParameterizedTypeReference<AmxApiResponse<Map<String, FieldListDto>, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getFieldList : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.GET_INCOME_RANGE_LIST)
					.meta(new JaxMetaInfo()).post(model)
					.as(new ParameterizedTypeReference<AmxApiResponse<IncomeRangeDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getIncomeRangeResponse : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_DESIGNATION_LIST).post(model)
					.as(new ParameterizedTypeReference<AmxApiResponse<ArticleDetailsDescDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getDesignationListResponse : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse() {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_ARTICLE_LIST)
					.post().as(new ParameterizedTypeReference<AmxApiResponse<ArticleMasterDescDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getArticleListResponse : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.VALIDATE_OTP)
					.post(offsiteCustRegModel).as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in validateOtpForEmailAndMobile : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_EMPLOYMENT_TYPE_LIST).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in sendEmploymentTypeList : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_PROFESSION_LIST).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in sendProfessionList : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_ID_TYPES)
					.post().as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in sendIdTypes : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<SendOtpModel, Object> sendOtp(CustomerPersonalDetail customerPersonalDetail) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_CUSTOMER_OTP)
					.post(customerPersonalDetail)
					.as(new ParameterizedTypeReference<AmxApiResponse<SendOtpModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in sendOtp : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(CustomerInfoRequest model) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.SAVE_CUST_INFO)
					.post(model).as(new ParameterizedTypeReference<AmxApiResponse<CustomerInfo, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveCustomerInfo : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<String, Object> saveCustomeKycDocument(ImageSubmissionRequest modelData)
			throws ParseException {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.SAVE_KYC_DOC)
					.meta(new JaxMetaInfo())
					.post(modelData).as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveCustomerInfo : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch}
	}

	@Override
	public AmxApiResponse<String, Object> saveCustomerSignature(ImageSubmissionRequest model) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.SAVE_SIGNATURE)
					.post(model).as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveCustomerInfo : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch}
	}

	@Override
	public AmxApiResponse<CardDetail, Object> cardScan(CardDetail cardDetail) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo()).path(CustRegApiEndPoints.SCAN_CARD)
					.post(cardDetail).as(new ParameterizedTypeReference<AmxApiResponse<CardDetail, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in cardScan : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch}
	}

	@Override
	public AmxApiResponse<CustomerCredential, Object> saveLoginDetailOffsite(CustomerCredential customerCredential) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.SAVE_OFFSITE_LOGIN)
					.post(customerCredential)
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerCredential, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveLoginDetailOffsite : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch}
	}

	@Override
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerData(String identityInt,
			BigDecimal identityType) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_OFFSITE_CUSTOMER_DATA).queryParam(Params.IDENTITY_INT, identityInt)
					.queryParam(Params.IDENTITY_TYPE, identityType).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<OffsiteCustomerDataDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveLoginDetailOffsite : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch}
	}
	
	
	@Override
	public AmxApiResponse<ResourceDTO, Object> getDesignationList() {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.DESIGNATION_LIST).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ResourceDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getDesignationListResponse : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerDetails(String identityInt,
			BigDecimal identityType) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_CUSTOMER_DEATILS).queryParam(Params.IDENTITY_INT, identityInt)
					.queryParam(Params.IDENTITY_TYPE, identityType).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<OffsiteCustomerDataDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveLoginDetailOffsite : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-c
	}

}
