package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.LoyaltyPointModel;
import com.amx.jax.repository.ILoyaltyPointDAO;

@Service
public class LoyaltyPointService {
	
	
	@Autowired
	ILoyaltyPointDAO loyaltyPointdao;
	
	public List<LoyaltyPointModel> getLoyaltyPointFromLoyaltyTable(BigDecimal cusref,BigDecimal fYear){
		return loyaltyPointdao.getLoyaltyPoints(cusref, fYear);
	}

}
