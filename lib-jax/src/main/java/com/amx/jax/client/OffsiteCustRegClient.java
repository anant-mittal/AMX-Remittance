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
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.customer.ICustRegService;
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
import com.amx.jax.model.response.customer.AddressProofDTO;
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
			LOGGER.debug("error in getheader of jaxclient", e);
		}
		return headers;
	}

	public static final String OFFSITE_CUSTOMER_REG = "/offsite-cust-reg";

	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model) {
		
			return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.GET_DYNAMIC_FIELDS).post(model)
					.as(new ParameterizedTypeReference<AmxApiResponse<Map<String, FieldListDto>, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model) {
		
			return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.GET_INCOME_RANGE_LIST)
					.meta(new JaxMetaInfo()).post(model)
					.as(new ParameterizedTypeReference<AmxApiResponse<IncomeRangeDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_DESIGNATION_LIST).post(model)
					.as(new ParameterizedTypeReference<AmxApiResponse<ArticleDetailsDescDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse() {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_ARTICLE_LIST)
					.post().as(new ParameterizedTypeReference<AmxApiResponse<ArticleMasterDescDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.VALIDATE_OTP)
					.post(offsiteCustRegModel).as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_EMPLOYMENT_TYPE_LIST).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_PROFESSION_LIST).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_ID_TYPES)
					.post().as(new ParameterizedTypeReference<AmxApiResponse<ComponentDataDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<SendOtpModel, Object> sendOtp(CustomerPersonalDetail customerPersonalDetail) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_CUSTOMER_OTP)
					.post(customerPersonalDetail)
					.as(new ParameterizedTypeReference<AmxApiResponse<SendOtpModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(CustomerInfoRequest model) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.SAVE_CUST_INFO)
					.post(model).as(new ParameterizedTypeReference<AmxApiResponse<CustomerInfo, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<String, Object> saveCustomeKycDocument(ImageSubmissionRequest modelData)
			throws ParseException {
		
			return restService.ajax(appConfig.getJaxURL()).path(CustRegApiEndPoints.SAVE_KYC_DOC)
					.meta(new JaxMetaInfo())
					.post(modelData).as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<String, Object> saveCustomerSignature(ImageSubmissionRequest model) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.SAVE_SIGNATURE)
					.post(model).as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<CardDetail, Object> cardScan(CardDetail cardDetail) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo()).path(CustRegApiEndPoints.SCAN_CARD)
					.post(cardDetail).as(new ParameterizedTypeReference<AmxApiResponse<CardDetail, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<CustomerCredential, Object> saveLoginDetailOffsite(CustomerCredential customerCredential) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.SAVE_OFFSITE_LOGIN)
					.post(customerCredential)
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerCredential, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerData(String identityInt,
			BigDecimal identityType) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_OFFSITE_CUSTOMER_DATA).queryParam(Params.IDENTITY_INT, identityInt)
					.queryParam(Params.IDENTITY_TYPE, identityType).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<OffsiteCustomerDataDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ResourceDTO, Object> getDesignationList() {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.DESIGNATION_LIST).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ResourceDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerDetails(String identityInt,
			BigDecimal identityType,BigDecimal customerId) {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.GET_CUSTOMER_DEATILS).queryParam(Params.IDENTITY_INT, identityInt)
					.queryParam(Params.IDENTITY_TYPE, identityType).get()
					.queryParam(Params.CUSTOMER_ID, customerId).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<OffsiteCustomerDataDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<AddressProofDTO, Object> getAddressProof() {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.ADDRESS_PROOF).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<AddressProofDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveDocumentUploadReference(
			ImageSubmissionRequest imageSubmissionRequest) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustRegApiEndPoints.DOCUMENT_UPLOAD_REFERENCE)
					.post(imageSubmissionRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

}
