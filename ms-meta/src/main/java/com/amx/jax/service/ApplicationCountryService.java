package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.services.AbstractService;

@Service
public class ApplicationCountryService  extends AbstractService{
	
	
	@Autowired
	IApplicationCountryRepository applicationCountryRepository;
	

	
	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountryListResponse(){
		List<ApplicationSetup> appCountryList = applicationCountryRepository.findAll();
		if(appCountryList.isEmpty()) {
			throw new GlobalException("Application country is not set");
		}
		return AmxApiResponse.buildList(convert(appCountryList));
		
	}
	
	
	
	
	
	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountryResponse(BigDecimal companyId,BigDecimal countryId){
		List<ApplicationSetup> appCountryList = applicationCountryRepository.findByCountryIdAndCompanyId(companyId,countryId);
		if(appCountryList.isEmpty()) {
			throw new GlobalException("Application list is not abaliable");
		}
		return AmxApiResponse.buildList(convert(appCountryList));
	}
	
	
	private List<ApplicationSetupDTO> convert(List<ApplicationSetup> appCountryList) {
		List<ApplicationSetupDTO> list = new ArrayList<>();
		for (ApplicationSetup appl : appCountryList) {
			ApplicationSetupDTO model = new ApplicationSetupDTO();
			model.setAmlCheck(appl.getAmlCheck());
			model.setApplicationCountryId(appl.getApplicationCountryId());
			model.setApplicationSetupId(appl.getApplicationSetupId());
			model.setCompanyId(appl.getCompanyId());
			model.setEmailAliasName(appl.getEmailAliasName());
			model.setEmailHost(appl.getEmailHost());
			model.setEmailId(appl.getEmailId());
			model.setEmailIndicator(appl.getEmailIndicator());
			model.setEmailPassword(appl.getEmailPassword());
			model.setEmailPortNo(appl.getEmailPortNo());
			model.setEmailUserName(appl.getEmailUserName());
			model.setFaAccountNumber(appl.getFaAccountNumber());
			model.setHeadOfficeLocationBranchCode(appl.getHeadOfficeLocationBranchCode());
			model.setInsuranceIndicator(appl.getInsuranceIndicator());
			model.setLoyalityIndicator(appl.getLoyalityIndicator());
			model.setOnlineDomUrl(appl.getOnlineDomUrl());
			model.setOnlineFeedbackEmail(appl.getOnlineFeedbackEmail());
			model.setOrsIndicator(appl.getOrsIndicator());
			model.setPayableGLNumber(appl.getPayableGLNumber());
			model.setRoundFactor(appl.getRoundFactor());
			model.setScanIndicator(appl.getScanIndicator());
			model.setSmsIndicator(appl.getSmsIndicator());
			model.setIdExpiryYears(appl.getIdExpiryYears());
			model.setIsoCode(appl.getIsoCode());
			model.setAmlSystem(appl.getAmlSystem());
			model.setAmlCustomerSource(appl.getAmlCustomerSource());
			model.setAmlInstanciated(appl.getAmlInstanciated());
			list.add(model);
		}
		return list;
	}
	

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
