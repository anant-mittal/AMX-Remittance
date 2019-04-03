package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.ViewCompanyDetailDTO;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.services.AbstractService;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CompanyService extends AbstractService {

	@Autowired
	ICompanyDAO companyDao;

	@Autowired
	MetaData metaData;

	public static List<ViewCompanyDetails> DEFAULT_COMPANY_DETALIS;

	public AmxApiResponse<ViewCompanyDetailDTO, Object> getCompanyDetails(BigDecimal languageId) {
		List<ViewCompanyDetails> companyDetails = companyDao.getCompanyDetails(languageId);
		if (companyDetails.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}
		return AmxApiResponse.buildList(convert(companyDetails));
	}

	public ViewCompanyDetails getCompanyDetail(BigDecimal languageId) {
		List<ViewCompanyDetails> companyDetails = companyDao.getCompanyDetails(languageId);
		if (companyDetails.isEmpty()) {
			throw new GlobalException(JaxError.INVALID_LANGUAGE_ID, "Language Id is invalid");
		}
		return companyDetails.get(0);
	}

	/**
	 * returns the company details based on meta info
	 */
	@Transactional(readOnly = true)
	public ViewCompanyDetails getCompanyDetail() {
		List<ViewCompanyDetails> companyDetails = companyDao.getCompanyDetailsByCompanyId(metaData.getLanguageId(),
				metaData.getCompanyId());
		return companyDetails.get(0);
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

	public ViewCompanyDetails getCompanyDetailsById(BigDecimal companyId) {
		List<ViewCompanyDetails> companyDetails = companyDao.getCompanyDetailsByCompanyId(companyId);
		return companyDetails.get(0);
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
