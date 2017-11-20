package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.TermsAndCondition;
import com.amx.jax.repository.ITermsAndConditionRepository;


@Service
public class TermsAndConditionService {
	
	@Autowired
	ITermsAndConditionRepository termsAndCondition;
	
	public List<TermsAndCondition> getTermsAndCondition(BigDecimal languageId){
		return termsAndCondition.getTermsAndCondition(languageId);
	}

}
