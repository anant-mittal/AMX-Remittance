package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.remittance.IRemittanceService.Params;
import com.amx.jax.dbmodel.PurposeOfTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BranchRemittanceService extends AbstractService{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * @return :to get the fc sale purpose of Trnx
	 */
	/*public AmxApiResponse<PurposeOfTransactionDto, Object> getPurposeofTrnxList() {
		List<PurposeOfTransaction> purposeofTrnxList = purposetrnxDao.getPurposeOfTrnx();
		if (purposeofTrnxList.isEmpty()) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No data found");
		}
		return AmxApiResponse.buildList(convertPurposeOfTrnxDto(purposeofTrnxList));
	}
	*/
	
	public AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(BigDecimal countryBranchId,BigDecimal employeeId,String transactiondate){
		List<UserwiseTransactionDto>  trnxCount = null;
		
		return null;//AmxApiResponse.buildList();
	}
	
}
