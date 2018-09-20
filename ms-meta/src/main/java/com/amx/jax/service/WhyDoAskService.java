package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.WhyDoAskInformationDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.WhyDoAskInformation;
import com.amx.jax.repository.IWhyDoAskInformationRepository;
import com.amx.jax.services.AbstractService;

/**
 * 
 * @author :Rabil
 *
 */
@Service
public class WhyDoAskService extends AbstractService{
		
	@Autowired
	IWhyDoAskInformationRepository whyDoAskInformationRepository;
	
	public AmxApiResponse<WhyDoAskInformationDTO, Object> getWhyDoAskInformation(BigDecimal languageId,BigDecimal countryId){
		List<WhyDoAskInformation> whyInfo = whyDoAskInformationRepository.getwhyDoAskInformation(languageId,countryId);
		if(whyInfo.isEmpty()) {
			throw new GlobalException("Info not avaliable");
		}
		return AmxApiResponse.buildList(convert(whyInfo));
	}
	
	
	
	private List<WhyDoAskInformationDTO> convert(List<WhyDoAskInformation> whyInfo) {
		List<WhyDoAskInformationDTO> list = new ArrayList<WhyDoAskInformationDTO>();
		for (WhyDoAskInformation whyaskInfo : whyInfo) {
			WhyDoAskInformationDTO model = new WhyDoAskInformationDTO();
			model.setCompanyId(whyaskInfo.getCompanyId());
			model.setCountryId(whyaskInfo.getCountryId());
			model.setDescription(whyaskInfo.getDescription());
			model.setLanguageId(whyaskInfo.getLanguageId());
			model.setStatus(whyaskInfo.getStatus());
			model.setInfoId(whyaskInfo.getInfoId());
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
