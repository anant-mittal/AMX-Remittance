package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CustomerRating;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.ICustomerRatingDao;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerRatingService {

	private Logger logger = Logger.getLogger(CustomerRatingService.class);

	@Autowired
	ICustomerRatingDao customerRatingdao;
	
	@Autowired
	MetaData metaData;

	/**
	 * Saved customer rating
	 * 
	 * @param dto
	 * @return
	 */
	public AmxApiResponse<CustomerRating, ?> saveCustomerRating(CustomerRating dto) {
		try {
			BigDecimal customerId = metaData.getCustomerId();
			BigDecimal applicationCountryId = metaData.getCountryId();
			
			dto.setCustomerId(customerId);
			dto.setApplicationCountryId(applicationCountryId);
			dto.setCreatedDate(new Date());
			customerRatingdao.save(dto);
		} catch (Exception e) {
			logger.error("Error while saving customer rating.", e);
		}
		return AmxApiResponse.build();
	}

}
