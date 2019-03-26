package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerEmploymentInfo;
import com.amx.jax.dbmodel.IncomeModel;
import com.amx.jax.userservice.repository.CustomerEmploymentInfoRepository;
import com.amx.jax.userservice.repository.IncomeRepository;

@Component
public class IncomeDao {
	
	@Autowired
	IncomeRepository incomeRepository;
	
	@Autowired
	CustomerEmploymentInfoRepository customerEmploymentInfoRepository;
	
	
	public List<IncomeModel> getAnnualIncome(BigDecimal customerId) {
		List<IncomeModel> incomeList = incomeRepository.getAnnualIncome();
		return incomeList;

	}
	
	public CustomerEmploymentInfo getCustById(BigDecimal customerId) {
		CustomerEmploymentInfo customerEmploymentInfo = customerEmploymentInfoRepository.getCustById(new Customer(customerId));
		return customerEmploymentInfo;
	}
	
	public void saveCustomerEmploymentInfo(CustomerEmploymentInfo c) {
		customerEmploymentInfoRepository.save(c);
	}
	public IncomeModel getAnnualIncomeRangeId(BigDecimal incomeRangeFrom, BigDecimal incomeRangeTo) {
		IncomeModel incomeModel  = incomeRepository.getAnnualIncomeRangeId(incomeRangeFrom, incomeRangeTo);
		return incomeModel;
	}
	
}
