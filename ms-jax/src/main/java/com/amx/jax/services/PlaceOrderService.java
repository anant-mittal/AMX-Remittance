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
import com.amx.jax.exception.GlobalException;
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
		ApiResponse response = getBlackApiResponse();
		PlaceOrder placeOrderModel = PlaceOrderUtil.getPlaceOrderModel(dto);
		//dto.getBeneficiaryRelationshipSeqId();
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
	 * Get Place Order list for Customer
	 * @param customerId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ApiResponse<PlaceOrderDTO> getPlaceOrderForCustomer(BigDecimal customerId) {
		
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		
		List<PlaceOrder> placeOrderList = null;
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();
		
		try {
			placeOrderList = placeOrderdao.getPlaceOrderForCustomer(customerId);
			
			if(!placeOrderList.isEmpty()) {
				for(PlaceOrder rec : placeOrderList) {
					PlaceOrderDTO placeDTO = new PlaceOrderDTO();
					
					placeDTO.setCustomerId(rec.getCustomerId());
					placeDTO.setPlaceOrderId(rec.getOnlinePlaceOrderId());
					placeDTO.setBeneficiaryRelationshipSeqId(rec.getBeneficiaryRelationshipSeqId());
					placeDTO.setTargetExchangeRate(rec.getTargetExchangeRate());
					placeDTO.setSrlId(rec.getSrlId());
					placeDTO.setSourceOfIncomeId(rec.getSourceOfIncomeId());
					placeDTO.setIsActive(rec.getIsActive());
					placeDTO.setBankRuleFieldId(rec.getBankRuleFieldId());
					placeDTO.setCreatedDate(rec.getCreatedDate());
					placeDTO.setValidFromDate(rec.getValidFromDate());
					placeDTO.setValidToDate(rec.getValidToDate());
					placeDTO.setPayAmount(rec.getPayAmount());
					placeDTO.setReceiveAmount(rec.getReceiveAmount());
					placeDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					placeDTO.setBaseCurrencyQuote(rec.getBaseCurrencyQuote());
					placeDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					placeDTO.setForeignCurrencyQuote(rec.getForeignCurrencyQuote());
					
					dtoList.add(placeDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("place-order-dto");
			}
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching Place Order List by Customer");
			e.printStackTrace();
		}
		
		response.getData().getValues().addAll(dtoList);
		return response;
	}

	/**
	 * Get All Place Order list
	 * @return
	 */
	public ApiResponse getAllPlaceOrder() {
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		
		List<PlaceOrder> placeOrderList = null;
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();
		try {
			placeOrderList = placeOrderdao.getAllPlaceOrder();
			
			if(!placeOrderList.isEmpty()) {
				for(PlaceOrder rec : placeOrderList) {
					PlaceOrderDTO placeDTO = new PlaceOrderDTO();
					
					placeDTO.setCustomerId(rec.getCustomerId());
					placeDTO.setPlaceOrderId(rec.getOnlinePlaceOrderId());
					placeDTO.setBeneficiaryRelationshipSeqId(rec.getBeneficiaryRelationshipSeqId());
					placeDTO.setTargetExchangeRate(rec.getTargetExchangeRate());
					placeDTO.setSrlId(rec.getSrlId());
					placeDTO.setSourceOfIncomeId(rec.getSourceOfIncomeId());
					placeDTO.setIsActive(rec.getIsActive());
					placeDTO.setBankRuleFieldId(rec.getBankRuleFieldId());
					placeDTO.setCreatedDate(rec.getCreatedDate());
					placeDTO.setValidFromDate(rec.getValidFromDate());
					placeDTO.setValidToDate(rec.getValidToDate());
					placeDTO.setPayAmount(rec.getPayAmount());
					placeDTO.setReceiveAmount(rec.getReceiveAmount());
					placeDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					placeDTO.setBaseCurrencyQuote(rec.getBaseCurrencyQuote());
					placeDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					placeDTO.setForeignCurrencyQuote(rec.getForeignCurrencyQuote());
					
					dtoList.add(placeDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("place-order-dto");
			}
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching All Place Order List");
			e.printStackTrace();	
		}
		
		response.getData().getValues().addAll(dtoList);
		return response;
	}
	
	public ApiResponse deletePlaceOrder(PlaceOrderDTO dto) {
		ApiResponse response = getBlackApiResponse();
		try {
			List<PlaceOrder> placeOrderList = placeOrderdao.getPlaceOrderDelete(dto.getPlaceOrderId());
			if(!placeOrderList.isEmpty()) {
				PlaceOrder rec = placeOrderList.get(0);
				rec.setIsActive("N");
				rec.setUpdatedDate(new Date());
				
				placeOrderdao.save(rec);
			}else {
				throw new GlobalException("No record found");
			}
			response.setResponseStatus(ResponseStatus.OK);
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while deleting Place Order record.");
			e.printStackTrace();
		}
		return response;
	}
	
	public ApiResponse<PlaceOrderDTO> getPlaceOrderForId(BigDecimal placeOrderId) {
		ApiResponse<PlaceOrderDTO> response = getBlackApiResponse();
		
		List<PlaceOrder> placeOrderList = null;
		List<PlaceOrderDTO> dtoList = new ArrayList<PlaceOrderDTO>();	
		
		try {
			placeOrderList = placeOrderdao.getPlaceOrderForId(placeOrderId);
			
			if(!placeOrderList.isEmpty()) {
				for(PlaceOrder rec : placeOrderList) {
					PlaceOrderDTO placeDTO = new PlaceOrderDTO();
					
					placeDTO.setCustomerId(rec.getCustomerId());
					placeDTO.setPlaceOrderId(rec.getOnlinePlaceOrderId());
					placeDTO.setBeneficiaryRelationshipSeqId(rec.getBeneficiaryRelationshipSeqId());
					placeDTO.setTargetExchangeRate(rec.getTargetExchangeRate());
					placeDTO.setSrlId(rec.getSrlId());
					placeDTO.setSourceOfIncomeId(rec.getSourceOfIncomeId());
					placeDTO.setIsActive(rec.getIsActive());
					placeDTO.setBankRuleFieldId(rec.getBankRuleFieldId());
					placeDTO.setCreatedDate(rec.getCreatedDate());
					placeDTO.setValidFromDate(rec.getValidFromDate());
					placeDTO.setValidToDate(rec.getValidToDate());
					placeDTO.setPayAmount(rec.getPayAmount());
					placeDTO.setReceiveAmount(rec.getReceiveAmount());
					placeDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					placeDTO.setBaseCurrencyQuote(rec.getBaseCurrencyQuote());
					placeDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					placeDTO.setForeignCurrencyQuote(rec.getForeignCurrencyQuote());
					
					dtoList.add(placeDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("place-order-dto");
			}
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching Place Order List By Id");
			e.printStackTrace();	
		}
		
		response.getData().getValues().addAll(dtoList);
		return response;
	}

	public ApiResponse updatePlaceOrder(PlaceOrderDTO dto) {
		ApiResponse response = getBlackApiResponse();
		try {
			List<PlaceOrder> placeOrderList = placeOrderdao.getPlaceOrderUpdate(dto.getPlaceOrderId());
			if(!placeOrderList.isEmpty()) {
				PlaceOrder rec = placeOrderList.get(0);
				
				rec.setIsActive("Y");
				rec.setUpdatedDate(new Date());
				rec.setCustomerId(dto.getCustomerId());
				rec.setBeneficiaryRelationshipSeqId(dto.getBeneficiaryRelationshipSeqId());
				rec.setBankRuleFieldId(dto.getBankRuleFieldId());
				rec.setSrlId(dto.getSrlId());
				rec.setTargetExchangeRate(dto.getTargetExchangeRate());
				rec.setSourceOfIncomeId(dto.getSourceOfIncomeId());
				rec.setValidFromDate(dto.getValidFromDate());
				rec.setValidToDate(dto.getValidToDate());
				rec.setPayAmount(dto.getPayAmount());
				rec.setReceiveAmount(dto.getReceiveAmount());
				rec.setCreatedDate(dto.getCreatedDate());
				rec.setBaseCurrencyId(rec.getBaseCurrencyId());
				rec.setBaseCurrencyQuote(rec.getBaseCurrencyQuote());
				rec.setForeignCurrencyId(rec.getForeignCurrencyId());
				rec.setForeignCurrencyQuote(rec.getForeignCurrencyQuote());
				
				placeOrderdao.save(rec);
				
			}else {
				throw new GlobalException("No record found");
			}
			response.setResponseStatus(ResponseStatus.OK);
		
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while deleting Place Order record.");
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
