package com.amx.jax.pricer;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;

public interface ProbotDataService  extends AbstractProbotInterface{
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemet(DiscountMgmtReqDTO discountMgmtReqDTO);

	public AmxApiResponse<HolidayResponseDTO, Object> fetchHolidayList(BigDecimal Id, Date fromDate, Date toDate);

	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(BigDecimal countryId,
			BigDecimal currencyId);
	
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(DiscountDetailsReqRespDTO discountMgmtReqDTO);
	
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData();
	
	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(BigDecimal groupId, BigDecimal currencyId);
	
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(BigDecimal groupId);
	
	public AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData( OnlineMarginMarkupReq OnlineMarginMarkupReq);

	AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData(OnlineMarginMarkupInfo OnlineMarginMarkupReq); 
	
}
