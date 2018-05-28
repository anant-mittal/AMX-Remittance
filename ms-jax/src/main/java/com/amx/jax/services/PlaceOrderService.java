package com.amx.jax.services;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.PlaceOrderUtil;

@Service
@SuppressWarnings("rawtypes")
public class PlaceOrderService extends AbstractService {
	
	private Logger logger = Logger.getLogger(PlaceOrderService.class);
	
	@Autowired
	IPlaceOrderDao placeOrderdao;
	
	@Autowired
	CurrencyMasterService currencyService;

	@Autowired
	CustomerDao customerDao;

	public ApiResponse savePlaceOrder(PlaceOrderDTO dto) {
		// TODO Auto-generated method stub
		ApiResponse response = getBlackApiResponse();
		PlaceOrder placeOrderModel = PlaceOrderUtil.getPlaceOrderModel(dto);
		
		try {
			placeOrderModel.setCreatedDate(new Date());
			placeOrderModel.setIsActive("Y");
			
			placeOrderdao.save(placeOrderModel);
			response.setResponseStatus(ResponseStatus.OK);
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while saving Place Order.");
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

}
