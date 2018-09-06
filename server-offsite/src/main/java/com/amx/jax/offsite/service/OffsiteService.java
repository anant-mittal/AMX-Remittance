package com.amx.jax.offsite.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

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
import com.amx.jax.rbaac.error.AuthServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;


@Component
public class OffsiteService {
	
	private static Logger logger = Logger.getLogger(OffsiteService.class);
	
	public Map<String, FieldListDto> getFieldList(DynamicFieldRequest model) {
		// TODO Auto-generated method stub
		
		String tenant = model.getTenant();
		String nationality = model.getNationality();
		String component = model.getComponent();
		
		/**
		 * Input -> Invalid
		 */
		if (StringUtils.isBlank(tenant) || StringUtils.isBlank(nationality) || StringUtils.isBlank(component)) {
			throw new AuthServiceException("Tenant, Nationality, Component are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}
		
		FieldListDto fieldListDto = new FieldListDto();
		fieldListDto.setComponent(component);
		fieldListDto.setNationality(nationality);
		fieldListDto.setTenant(tenant);
		
		Map<String, FieldListDto> mapFieldListDto = new HashMap<String, FieldListDto>();
		mapFieldListDto.put("FieldListDto", fieldListDto);
		
		logger.info("Field List : " + fieldListDto.toString());
	
		return null;
	}
	
	public IncomeRangeDto getIncomeRangeResponse(EmploymentDetailsRequest model) {
		// TODO Auto-generated method stub
		
		BigDecimal articleDetailsId = model.getArticleDetailsId();
		//BigDecimal articleId = model.getArticleId();
		//BigDecimal countryId = model.getCountryId();
		
		/**
		 * Input -> Invalid
		 */
		if (articleDetailsId == null) {
			throw new AuthServiceException("Article Details Id are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}
		
		IncomeRangeDto incomeRangeDto = new IncomeRangeDto();
		incomeRangeDto.setArticleDetailsId(articleDetailsId);
		
		return incomeRangeDto;
	}
	
	public ArticleDetailsDescDto getDesignationListResponse(EmploymentDetailsRequest model) {
		// TODO Auto-generated method stub
		
		BigDecimal articleDetailsId = model.getArticleDetailsId();
		//BigDecimal articleId = model.getArticleId();
		//BigDecimal countryId = model.getCountryId();
		
		/**
		 * Input -> Invalid
		 */
		if (articleDetailsId == null) {
			throw new AuthServiceException("Article Details Id are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}
		
		ArticleDetailsDescDto articleDetailsDescDto = new ArticleDetailsDescDto();
		articleDetailsDescDto.setArticleDetailsId(articleDetailsId);
		
		return articleDetailsDescDto;
	}
	
	public ArticleMasterDescDto getArticleListResponse(CommonRequest model) {
		// TODO Auto-generated method stub
		
		BigDecimal countryId = model.getCountryId();
		//BigDecimal stateId = model.getStateId();
		//BigDecimal districtId = model.getDistrictId();
		//BigDecimal cityId = model.getCityId();
		//BigDecimal nationalityId = model.getNationalityId();
		
		/**
		 * Input -> Invalid
		 */
		if (countryId == null) {
			throw new AuthServiceException("Country Id are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}
		
		ArticleMasterDescDto articleMasterDescDto = new ArticleMasterDescDto();
		//articleMasterDescDto.setArticleId(articleId);
		
		return null;
	}
	
	public String validateOtpForEmailAndMobile(OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		// TODO Auto-generated method stub
		
		String mOtp = offsiteCustRegModel.getmOtp();
		String eOtp = offsiteCustRegModel.geteOtp();
		//String identityInt = offsiteCustRegModel.getIdentityInt();
		//String email = offsiteCustRegModel.getEmail();
		//String mobile = offsiteCustRegModel.getMobile();
		//BigDecimal countryId = offsiteCustRegModel.getCountryId();
		//BigDecimal nationalityId = offsiteCustRegModel.getNationalityId();
		
		/**
		 * Input -> Invalid
		 */
		if (StringUtils.isBlank(mOtp) || StringUtils.isBlank(eOtp)) {
			throw new AuthServiceException("MOtp, EOtp are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}
		
		return null;
	}
	
	public ComponentDataDto sendEmploymentTypeList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ComponentDataDto sendProfessionList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ComponentDataDto sendIdTypes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List sendOtpForEmailAndMobile(CustomerPersonalDetail customerPersonalDetail){
		// TODO Auto-generated method stub
		
		BigDecimal countryId = customerPersonalDetail.getCountryId();
		BigDecimal nationalityId = customerPersonalDetail.getNationalityId();
		String identityInt = customerPersonalDetail.getIdentityInt();
		String title = customerPersonalDetail.getTitle();
		String firstName = customerPersonalDetail.getFirstName();
		String lastName = customerPersonalDetail.getLastName();
		String email = customerPersonalDetail.getEmail();
		String mobile = customerPersonalDetail.getMobile();
		String telPrefix = customerPersonalDetail.getTelPrefix();
		
		/**
		 * Input -> Invalid
		 */
		if (countryId == null || nationalityId == null || StringUtils.isBlank(identityInt) || StringUtils.isBlank(title) || StringUtils.isBlank(firstName) 
				|| StringUtils.isBlank(lastName) || StringUtils.isBlank(email) || StringUtils.isBlank(mobile) || StringUtils.isBlank(telPrefix)) {
			throw new AuthServiceException("Country Id, Nationality Id,Identity Int, Title, First Name, Last Name, Email, Mobile, Tel Prefix are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}
		
		//List obj = 
		
		return null;
	}

}
