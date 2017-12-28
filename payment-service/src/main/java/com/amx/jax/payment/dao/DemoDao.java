/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.payment.model.db.Demo;
import com.amx.jax.payment.repository.DemoRepository;

/**
 * @author Viki Sangani
 * 15-Dec-2017
 * DemoDao.java
 */
@Component
public class DemoDao {
	
	@Autowired
	private DemoRepository demo;
	
	@Transactional
	public Demo getCustomerByCivilId(String civilId) {
		Demo cust = null;
//		BigDecimal countryId = meta.getCountryId();
		List<Demo> demos = demo.getDemoUser("AA01");
		if (demos != null && !demos.isEmpty()) {
			cust = demos.get(0);
		}
		return cust;
	}
	
	@Transactional
	public Demo getKnetConfig() {
		Demo config = null;

		List<Demo> demos = demo.getKnetConfig("knet");
		if (demos != null && !demos.isEmpty()) {
			config = demos.get(0);
		}
		return config;
	}

}
