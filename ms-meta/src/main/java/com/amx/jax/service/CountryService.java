package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CountryMasterDesc;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.LanguageType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryMasterRepository;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.services.AbstractService;
/**
 * 
 * @author Rabil
 *
 */

@Service
public class CountryService extends AbstractService {
	@Autowired
	CountryRepository countryRepository;
	
	@Autowired
	CountryMasterRepository countryMasterRepository;
	
	@Autowired
	MetaData meta;
	
	
	public AmxApiResponse<CountryMasterView, Object> getCountryListResponse(){
		List<CountryMasterView> countryList = countryRepository.findByLanguageId(meta.getLanguageId());
		ApiResponse response = getBlackApiResponse();
		if(countryList.isEmpty()) {
			throw new GlobalException("Country list is not abaliable");
		}else {
		response.getData().getValues().addAll(countryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("country");
		return AmxApiResponse.buildList(countryList);
	}
	
	
	@Deprecated
	public AmxApiResponse<CountryMasterView, Object> getCountryListByLanguageIdResponse(BigDecimal languageId){
		List<CountryMasterView> countryList = countryRepository.findByLanguageId(languageId);		
		if(countryList.isEmpty()) {
			throw new GlobalException("Country list is not abaliable");
		}	
		return AmxApiResponse.buildList(countryList);
	}
	
	
	public AmxApiResponse<CountryMasterView, Object> getCountryByLanguageIdAndCountryIdResponse(BigDecimal languageId,BigDecimal countryId){
		List<CountryMasterView> countryList = countryRepository.findByLanguageIdAndCountryId(languageId,countryId);
		
		if(countryList.isEmpty()) {
			throw new GlobalException("Country is not abaliable");
		}
		return AmxApiResponse.buildList(countryList);
	}
	

	
	public AmxApiResponse<CountryMasterView, Object> getBusinessCountryResponse(BigDecimal languageId){
		List<CountryMasterView> countryList = countryRepository.getBusinessCountry(languageId);
		if(countryList.isEmpty()) {
			throw new GlobalException("Business country is not abaliable");
		}
		return AmxApiResponse.buildList(countryList);
	}
	
	
	
	private List<CountryMasterDTO> convert(List<CountryMasterView> countryList) {
		List<CountryMasterDTO> list = new ArrayList<>();
		for (CountryMasterView country : countryList) {
			CountryMasterDTO model = new CountryMasterDTO();
			model.setIdNo(country.getIdNo());
			model.setBusinessCountry(country.getBusinessCountry());
			model.setCountryActive(country.getCountryActive());
			model.setCountryAlpha2Code(country.getCountryAlpha2Code());
			model.setCountryAlpha3Code(country.getCountryAlpha3Code());
			model.setCountryCode(country.getCountryCode());
			model.setCountryId(country.getCountryId());
			model.setCountryName(country.getCountryName());
			model.setCountryTelCode(country.getCountryTelCode());
			model.setCountryISOCode(country.getCountryISOCode());
			model.setLanguageCode(country.getLanguageCode());
			model.setLanguageName(country.getLanguageName());
			model.setLanguageId(country.getLanguageId());
			
			
			list.add(model);
		}
		return list;
	}

	@Override
	public String getModelType() {
		return "country";
	}

	@Override
	public Class<?> getModelClass() {
		return null;
	}
	
	public boolean isBangladeshCountry(BigDecimal countryId) {
		String countryAlfa3Code = countryRepository.findByLanguageIdAndCountryId(meta.getLanguageId(), countryId).get(0)
				.getCountryAlpha3Code();
		if (ConstantDocument.BANGLADESH_ALPHA3_CODE.equals(countryAlfa3Code)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param countryId
	 * @return CountryMaster
	 * 
	 */
	public CountryMaster getCountryMaster(BigDecimal countryId) {
		return countryMasterRepository.findOne(countryId);
	}
	
	public AmxApiResponse<CountryMasterView, Object> getCountryListOffsite(){
		List<CountryMasterView> countryList = countryRepository.findByLanguageId(meta.getLanguageId());		
		if(countryList.isEmpty()) {
			throw new GlobalException("Country list is not abaliable");
		}	
		return AmxApiResponse.buildList(countryList);	
	}
	
	public CountryMasterDesc getCountryMasterDesc(BigDecimal countryId, BigDecimal languageId) {
		List<CountryMasterDesc> countryMasterDescs = countryMasterRepository.findOne(countryId)
				.getFsCountryMasterDescs();
		CountryMasterDesc countryMasterDesc = null;
		for (CountryMasterDesc desc : countryMasterDescs) {
			if (desc.getFsLanguageType().getLanguageId().equals(languageId)) {
				countryMasterDesc = desc;
				break;
			}
		}
		return countryMasterDesc;
	}
}
