package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.WhyDoAskInformation;
import com.amx.jax.repository.IWhyDoAskInformationRepository;

@Service
public class WhyDoAskService {
		
	@Autowired
	IWhyDoAskInformationRepository whyDoAskInformationRepository;
	
	public List<WhyDoAskInformation> getWhyDoAskInformation(BigDecimal languageId,BigDecimal countryId){
		return whyDoAskInformationRepository.getwhyDoAskInformation(languageId,countryId);
	}

}
