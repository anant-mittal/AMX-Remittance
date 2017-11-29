package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.BeneficiaryCountryView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IBeneficiaryCountryDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.services.AbstractService;

@Service
public class BeneficiaryOnlineService extends AbstractService{
	
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;
	
	@Autowired
	IBeneficiaryCountryDao beneficiaryCountryDao;
	
	
	public ApiResponse getBeneficiaryListForOnline(BigDecimal customerId,BigDecimal applicationCountryId,BigDecimal beneCountryId) {
		List<BenificiaryListView> beneOnlineList = null;
		if(beneCountryId!=null && beneCountryId.compareTo(BigDecimal.ZERO)!=0) {
			beneOnlineList = beneficiaryOnlineDao.getOnlineBeneListFromViewForCountry(customerId, applicationCountryId,beneCountryId);
		}else {
			beneOnlineList = beneficiaryOnlineDao.getOnlineBeneListFromView(customerId, applicationCountryId);
		}
		
		ApiResponse response = getBlackApiResponse();
		if(beneOnlineList.isEmpty()) {
			throw new GlobalException("Beneficiary list is not found");
		}else {
	    response.getData().getValues().addAll(beneOnlineList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("onlinebene");
		return response;
	}
	
	
	
	
	public ApiResponse getBeneficiaryListForBranch(BigDecimal customerId,BigDecimal applicationCountryId,BigDecimal beneCountryId) {
		
		List<BenificiaryListView> beneOnlineList = null;
		if(beneCountryId!=null && beneCountryId.compareTo(BigDecimal.ZERO)!=0) {
			beneOnlineList = beneficiaryOnlineDao.getBeneListFromViewForCountry(customerId, applicationCountryId, beneCountryId);
		}else {
			beneOnlineList = beneficiaryOnlineDao.getBeneListFromView(customerId, applicationCountryId);
		}
		
		ApiResponse response = getBlackApiResponse();
		if(beneOnlineList.isEmpty()) {
			throw new GlobalException("Beneficiary list is not found");
		}else {
	    response.getData().getValues().addAll(beneOnlineList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("onlinebene");
		return response;
	}
	
	
	
	public ApiResponse getBeneficiaryCountryListForOnline(BigDecimal customerId) {
		List<BeneficiaryCountryView> beneocountryList = beneficiaryCountryDao.getBeneCountryForOnline(customerId);
		ApiResponse response = getBlackApiResponse();
		if(beneocountryList.isEmpty()) {
			throw new GlobalException("Beneficiary country list is not found");
		}else {
		response.getData().getValues().addAll(convert(beneocountryList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("benecountry");
		return response;
	}
	
	
	public ApiResponse getBeneficiaryCountryListForBranch(BigDecimal customerId) {
		List<BeneficiaryCountryView> beneocountryList = beneficiaryCountryDao.getBeneCountryForBranch(customerId);
		ApiResponse response = getBlackApiResponse();
		if(beneocountryList.isEmpty()) {
			throw new GlobalException("Beneficiary country list is not found");
		}else {
	    response.getData().getValues().addAll(convert(beneocountryList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("benecountry");
		return response;
	}
	
	
	
	
	
private List<BeneCountryDTO> convert(List<BeneficiaryCountryView> beneocountryList) {
		List<BeneCountryDTO> list = new ArrayList<BeneCountryDTO>();
		for (BeneficiaryCountryView beneCountry : beneocountryList) {
			BeneCountryDTO model = new BeneCountryDTO();
			model.setApplicationCountry(beneCountry.getApplicationCountry());
			model.setBeneCountry(beneCountry.getBeneCountry());
			model.setCountryCode(beneCountry.getCountryCode());
			model.setCountryName(beneCountry.getCountryName());
			model.setCustomerId(beneCountry.getCustomerId());
			model.setIdNo(beneCountry.getIdNo());
			model.setOrsStatus(beneCountry.getOrsStatus());
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
