package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.ViewCompanyDetailDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.services.AbstractService;

@Service
public class CompanyService extends AbstractService {

	@Autowired
	ICompanyDAO companyDao;

	public static List<ViewCompanyDetails> DEFAULT_COMPANY_DETALIS;

	public ApiResponse getCompanyDetails(BigDecimal languageId) {
		List<ViewCompanyDetails> companyDetails = companyDao.getCompanyDetails(languageId);
		ApiResponse response = getBlackApiResponse();
		if (companyDetails.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		} else {
			response.getData().getValues().addAll(convert(companyDetails));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("company");
		return response;
	}

	public  static ViewCompanyDetails getCompanyDetailsFromInMemory(BigDecimal languageId) {
		ViewCompanyDetails companyDetails=null;
		for (ViewCompanyDetails vcd : DEFAULT_COMPANY_DETALIS) {
			
			if (vcd.getLanguageId().compareTo(languageId)==0) {
				companyDetails = vcd;
			}
		}
		return companyDetails;
	}

	public List<ViewCompanyDetailDTO> convert(List<ViewCompanyDetails> companyDetails) {

		List<ViewCompanyDetailDTO> list = new ArrayList<ViewCompanyDetailDTO>();
		for (ViewCompanyDetails comp : companyDetails) {
			ViewCompanyDetailDTO modelDto = new ViewCompanyDetailDTO();
			modelDto.setApplicationCountryId(comp.getApplicationCountryId());
			modelDto.setArabicAddress1(comp.getArabicAddress1());
			modelDto.setArabicAddress2(comp.getArabicAddress2());
			modelDto.setArabicAddress3(comp.getArabicAddress3());
			modelDto.setEngAddress1(comp.getEngAddress1());
			modelDto.setEngAddress2(comp.getEngAddress2());
			modelDto.setEngAddress3(comp.getEngAddress3());
			modelDto.setCapitalAmount(comp.getCapitalAmount());
			modelDto.setRegistrationNumber(comp.getRegistrationNumber());
			modelDto.setCompanyCode(comp.getCompanyCode());
			modelDto.setCompanyName(comp.getCompanyName());
			modelDto.setEmail(comp.getEmail());
			modelDto.setCurrencyId(comp.getCurrencyId());
			modelDto.setEmail(comp.getEmail());
			modelDto.setEstYear(comp.getEstYear());
			modelDto.setFaxNo(comp.getFaxNo());
			modelDto.setLanguageId(comp.getLanguageId());
			modelDto.setTelephoneNo(comp.getTelephoneNo());
			modelDto.setCompanyId(comp.getCompanyId());
			modelDto.setOnlineHeaderEnglishText(comp.getOnlineHeaderEnglishText());
			list.add(modelDto);
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
