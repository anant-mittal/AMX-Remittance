package com.amx.jax.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.UserFinancialYear;
import com.amx.jax.repository.IFinanceYearRespository;

@Service
public class FinancialService {
	
	@Autowired
	IFinanceYearRespository financialYearRepository;
	
	public List<UserFinancialYear> getFinancialYear(){
		return financialYearRepository.getFinancialYear();
		
	}

}
