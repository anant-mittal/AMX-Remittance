package com.amx.jax.services;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.CustomerRatingDTO;
import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CustomerRating;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.repository.ICustomerRatingDao;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.util.CustomerRatingUtil;
import com.amx.jax.util.PlaceOrderUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerRatingService extends AbstractService{

	private Logger logger = Logger.getLogger(CustomerRatingService.class);
	
	@Autowired
	ICustomerRatingDao customerRatingdao;
	
	/**
	 * Saved customer rating
	 * @param dto
	 * @return
	 */
	public ApiResponse saveCustomerRating(CustomerRatingDTO dto) {
		ApiResponse response = getBlackApiResponse();
		CustomerRating customerRatingModel = CustomerRatingUtil.getCustomerRatingModel(dto);
		try {
			dto.setCreatedDate(new Date());
			customerRatingdao.save(customerRatingModel);
			response.setResponseStatus(ResponseStatus.OK);
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while saving customer rating.");
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String getModelType() {
		return "customer-rating";
	}

}
