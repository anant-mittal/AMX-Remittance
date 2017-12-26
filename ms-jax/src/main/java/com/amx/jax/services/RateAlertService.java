package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.RateAlert;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.SourceOfIncomeView;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.repository.IRateAlertDao;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.repository.ISourceOfIncomeDao;
import com.amx.jax.service.CurrencyMasterService;

@Service
@SuppressWarnings("rawtypes")
public class RateAlertService extends AbstractService {

	@Autowired
	IRateAlertDao rateAlertDao;
	
	@Autowired
	CurrencyMasterService currencyService;

	public ApiResponse saveRateAlert(Map<String,Object> paramMap) {
		
		ApiResponse response = getBlackApiResponse();
		RateAlert rateAlertModel = new  RateAlert();
		//List<RateAlert> rateAlertList = null;
		
		BigDecimal customerId = (BigDecimal) paramMap.get("customerId");
		//BigDecimal onlineRateAlertId = (BigDecimal)paramMap.get("onlineRateAlertId");
		String rule =(String)paramMap.get("rule");
		Date fromDate = (Date) paramMap.get("fromDate");
		Date toDate = (Date) paramMap.get("toDate");
		BigDecimal baseCurrencyId = (BigDecimal) paramMap.get("basecur");
		BigDecimal foreignCurrencyId = (BigDecimal) paramMap.get("fccur");
		BigDecimal alertRate = (BigDecimal) paramMap.get("alertRate");
		
		try {
			rateAlertModel.setCustomerId(customerId);
			rateAlertModel.setBaseCurrencyId(baseCurrencyId);
			rateAlertModel.setBaseCurrencyCode(baseCurrencyId.toString());
			rateAlertModel.setForeignCurrencyId(foreignCurrencyId);
			rateAlertModel.setForeignCurrencyCode(foreignCurrencyId.toString());
			rateAlertModel.setAlertRate(alertRate);
			rateAlertModel.setRule(rule);
			rateAlertModel.setFromDate(fromDate);
			rateAlertModel.setToDate(toDate);
			rateAlertModel.setCreatedDate(new Date());
			rateAlertModel.setIsActive("Y");
			
			rateAlertDao.save(rateAlertModel);
			response.setResponseStatus(ResponseStatus.OK);
			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			throw new GlobalException("Error while update");
		}
		return response;
	}
	
	public ApiResponse getRateAlertForCustomer(Map<String,Object> paramMap) {
		
		ApiResponse response = getBlackApiResponse();
		try {
			List<RateAlert> rateAlertList = null;
	        List<RateAlertDTO> dtoList= new ArrayList<RateAlertDTO>();
	        
			rateAlertList = rateAlertDao.getRateAlertForCustomer(new BigDecimal((String)paramMap.get("customerId")));
			
			if (!rateAlertList.isEmpty()) {
				
				for (RateAlert rec : rateAlertList) {
					RateAlertDTO rateDTO = new RateAlertDTO();
					
					rateDTO.setAlertRate(rec.getAlertRate());
					rateDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					rateDTO.setBaseCurrencyQuote(currencyService.getCurrencyMasterById(rec.getBaseCurrencyId()).getQuoteName());
					rateDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					rateDTO.setForeignCurrencyQuote(currencyService.getCurrencyMasterById(rec.getForeignCurrencyId()).getQuoteName());
					rateDTO.setRule(rec.getRule());
					rateDTO.setFromDate(rec.getFromDate());
					rateDTO.setToDate(rec.getToDate());
					rateDTO.setCustomerId(rec.getCustomerId());
					rateDTO.setRateAlertId(rec.getOnlineRateAlertId());
					
					dtoList.add(rateDTO);
				}
				
				response.getData().getValues().add(dtoList);
				response.setResponseStatus(ResponseStatus.OK);
			} else {
				throw new GlobalException("No record found");
			}

			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Error while fetching rate alert records.");
		}
	}
	
	
	public ApiResponse delteRateAlert(Map<String,Object> paramMap) {
		
		ApiResponse response = getBlackApiResponse();
		try {
			BigDecimal id = (BigDecimal)paramMap.get("alertId");
			
			List<RateAlert> rateAlertList = rateAlertDao.getRateAlertDetails(id);
			
			if (!rateAlertList.isEmpty()) {
				RateAlert rec = rateAlertList.get(0);
				rec.setIsActive("N");
				rec.setUpdatedDate(new Date());
				rateAlertDao.save(rec);
			}else {
				throw new GlobalException("No record found");
			}
			
		    //rateAlertDao.delete(id);
			response.setResponseStatus(ResponseStatus.OK);
			return response;
		} catch (Exception e) {
			throw new GlobalException("Error while deleting rate alert record.");
		}
	}


	/* (non-Javadoc)
	 * @see com.amx.jax.services.AbstractService#getModelType()
	 */
	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.amx.jax.services.AbstractService#getModelClass()
	 */
	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
