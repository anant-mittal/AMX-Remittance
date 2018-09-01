package com.amx.jax.util;

import com.amx.amxlib.meta.model.CustomerRatingDTO;
import com.amx.jax.dbmodel.CustomerRating;

public class CustomerRatingUtil {

	public static CustomerRating getCustomerRatingModel(CustomerRatingDTO dto) {
		CustomerRating customerRatingmodel = new CustomerRating();
		
		customerRatingmodel.setApplicationCountryId(dto.getApplicationCountryId());
		customerRatingmodel.setCreatedDate(dto.getCreatedDate());
		customerRatingmodel.setCustomerId(dto.getCustomerId());
		customerRatingmodel.setRating(dto.getRating());
		customerRatingmodel.setRatingId(dto.getRatingId());
		customerRatingmodel.setRatingRemark(dto.getRatingRemark());
		customerRatingmodel.setRemittanceApplicationId(dto.getRemittanceApplicationId());
		customerRatingmodel.setRemittanceTransactionId(dto.getRemittanceTransactionId());
		
		return customerRatingmodel;
	}
}
