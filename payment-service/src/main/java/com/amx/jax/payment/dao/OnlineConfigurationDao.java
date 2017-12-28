/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.payment.model.db.Demo;
import com.amx.jax.payment.model.db.OnlineConfiguration;
import com.amx.jax.payment.repository.DemoRepository;
import com.amx.jax.payment.repository.OnlineConfigurationRepository;

/**
 * @author Viki Sangani
 * 15-Dec-2017
 * DemoDao.java
 */
@Component
public class OnlineConfigurationDao {
	
	@Autowired
	private OnlineConfigurationRepository onlineConfigurationRepository;
	
	@Transactional
	public OnlineConfiguration getPGConfig(BigDecimal id) {
		OnlineConfiguration config = null;

		List<OnlineConfiguration> configList = onlineConfigurationRepository.getPGConfiguration(id);
		if (configList != null && !configList.isEmpty()) {
			config = configList.get(0);
		}
		return config;
	} //end of getPGConfig()
	
}
