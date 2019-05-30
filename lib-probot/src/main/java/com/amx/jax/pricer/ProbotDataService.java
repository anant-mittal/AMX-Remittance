package com.amx.jax.pricer;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerApiStatusBuilder.PricerApiStatus;

public interface ProbotDataService extends AbstractProbotInterface{

	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemet(DiscountMgmtReqDTO discountMgmtReqDTO);
	
	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(BigDecimal countryId, BigDecimal currencyId);
	
	@PricerApiStatus({ PricerServiceError.INVALID_CHANNEL_DISC_PIPS, PricerServiceError.INVALID_CUST_CAT_DISC_PIPS,
		PricerServiceError.INVALID_AMT_SLAB_DISC_PIPS })
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(DiscountDetailsReqRespDTO discountMgmtReqDTO);
	
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData();
	
	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(BigDecimal groupId, BigDecimal currencyId);
	
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(BigDecimal groupId);
	
}
