package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.constant.RuleEnum;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.RateAlert;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IRateAlertDao;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.util.RateAlertUtil;

@Service
@SuppressWarnings("rawtypes")
public class RateAlertService extends AbstractService {
	
	private Logger logger = Logger.getLogger(RateAlertService.class);

	@Autowired
	IRateAlertDao rateAlertDao;
	
	@Autowired
	CurrencyMasterService currencyService;
	
	public ApiResponse saveRateAlert(RateAlertDTO dto) {
		
		ApiResponse response = getBlackApiResponse();
		RateAlert rateAlertModel = RateAlertUtil.getRateAlertModel(dto);

		try {
			rateAlertModel.setCreatedDate(new Date());
			rateAlertModel.setIsActive("Y");
			
			rateAlertDao.save(rateAlertModel);
			response.setResponseStatus(ResponseStatus.OK);

			
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while saving rae alert.");
			e.printStackTrace();
		}
		return response;
	}
	
	public ApiResponse delteRateAlert(RateAlertDTO dto) {
		
		ApiResponse response = getBlackApiResponse();
		try {
			List<RateAlert> rateAlertList = rateAlertDao.getRateAlertDetails(dto.getRateAlertId());
			
			if (!rateAlertList.isEmpty()) {
				RateAlert rec = rateAlertList.get(0);
				rec.setIsActive("N");
				rec.setUpdatedDate(new Date());
				rateAlertDao.save(rec);
			}else {
				throw new GlobalException("No record found");
			}
			response.setResponseStatus(ResponseStatus.OK);

		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while deleting rate alert record.");
			e.printStackTrace();
		}
		return response;
	}
	
	public ApiResponse<RateAlertDTO> getRateAlertForCustomer(BigDecimal customerId) {
		
		ApiResponse<RateAlertDTO> response = getBlackApiResponse();
		
			List<RateAlert> rateAlertList = null;
	        List<RateAlertDTO> dtoList= new ArrayList<RateAlertDTO>();
	        
	        try {
			rateAlertList = rateAlertDao.getRateAlertForCustomer(customerId);
			
			if (!rateAlertList.isEmpty()) {
				
				for (RateAlert rec : rateAlertList) {
					RateAlertDTO rateDTO = new RateAlertDTO();
					
					rateDTO.setAlertRate(rec.getAlertRate());
					rateDTO.setBaseCurrencyId(rec.getBaseCurrencyId());
					rateDTO.setBaseCurrencyQuote(currencyService.getCurrencyMasterById(rec.getBaseCurrencyId()).getQuoteName());
					rateDTO.setForeignCurrencyId(rec.getForeignCurrencyId());
					rateDTO.setForeignCurrencyQuote(currencyService.getCurrencyMasterById(rec.getForeignCurrencyId()).getQuoteName());
					rateDTO.setRule(RuleEnum.valueOf(rec.getRule().toUpperCase()));
					rateDTO.setFromDate(rec.getFromDate());
					rateDTO.setToDate(rec.getToDate());
					rateDTO.setCustomerId(rec.getCustomerId());
					rateDTO.setRateAlertId(rec.getOnlineRateAlertId());
					rateDTO.setPayAmount(rec.getPayAmount());
					rateDTO.setReceiveAmount(rec.getReceiveAmount());
					dtoList.add(rateDTO);
				}
				
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("rate-alert-dto");
			} /*else {
				throw new GlobalException("No record found");
			}*/
		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching rate alerts");
			e.printStackTrace();
		}
		response.getData().getValues().addAll(dtoList);
		return response;
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
