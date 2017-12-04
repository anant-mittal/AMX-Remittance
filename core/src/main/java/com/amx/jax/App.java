package com.amx.jax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import com.amx.jax.dal.LoyaltyInsuranceProDao;

public class App {
	
	@Autowired
	LoyaltyInsuranceProDao loyaltyInsuranceProDao;
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		
	
	}
	
	
	
	
	

}
