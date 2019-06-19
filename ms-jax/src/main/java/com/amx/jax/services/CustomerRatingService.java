package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.CustomerRatingDTO;
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
	public AmxApiResponse<CustomerRating, ?> saveCustomerRating(CustomerRatingDTO dto) {
		try {
			CustomerRating customerRating = new CustomerRating();
			BigDecimal customerId = metaData.getCustomerId();
			BigDecimal applicationCountryId = metaData.getCountryId();
			customerRating.setRating(dto.getRating());
			customerRating.setRatingId(dto.getRatingId());
			customerRating.setRatingRemark(dto.getRatingRemark());
			customerRating.setRemittanceApplicationId(dto.getRemittanceApplicationId());
			customerRating.setRemittanceTransactionId(dto.getRemittanceTransactionId());
			customerRating.setCustomerId(customerId);
			customerRating.setApplicationCountryId(applicationCountryId);
			customerRating.setCreatedDate(new Date());
			customerRatingdao.save(customerRating);
		} catch (Exception e) {
			logger.error("Error while saving customer rating.", e);
		}
		return AmxApiResponse.build();
	}

}
