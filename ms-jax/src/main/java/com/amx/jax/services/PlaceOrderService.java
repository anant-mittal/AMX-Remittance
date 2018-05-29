package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

/**
 * @author Subodh Bhoir
 *
 */
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

	/**
	 * Saved place order
	 * @param dto
	 * @return
	 */
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

	/**
	 * Get place order list
	 * @param customerId
	 * @return
	 */
	public ApiResponse<PlaceOrderDTO> getPlaceOrder(BigDecimal customerId) {
		
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		
		List<PlaceOrder> placeOrderList = null;
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();
		
		try {
			placeOrderList = placeOrderdao.getPlaceOrder(customerId);
			
			if(!placeOrderList.isEmpty()) {
				for(PlaceOrder rec : placeOrderList) {
					PlaceOrderDTO placeDTO = new PlaceOrderDTO();
					
					placeDTO.setCustomerId(rec.getCustomerId());
					placeDTO.setCreatedDate(rec.getCreatedDate());
					placeDTO.setValidFromDate(rec.getValidFromDate());
					placeDTO.setValidToDate(rec.getValidToDate());
					placeDTO.setPayAmount(rec.getPayAmount());
					placeDTO.setReceiveAmount(rec.getReceiveAmount());
					
					dtoList.add(placeDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("place-order-dto");
			}
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching Place Order List");
			e.printStackTrace();
		}
		
		response.getData().getValues().addAll(dtoList);
		return response;
	}

	
	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
