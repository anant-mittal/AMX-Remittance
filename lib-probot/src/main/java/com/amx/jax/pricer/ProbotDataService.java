package com.amx.jax.pricer;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.RateUploadRequestDto;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;

public interface ProbotDataService extends AbstractProbotInterface {
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemet(
			DiscountMgmtReqDTO discountMgmtReqDTO);

	public AmxApiResponse<HolidayResponseDTO, Object> fetchHolidayList(BigDecimal Id, Date fromDate, Date toDate);

	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(BigDecimal countryId,
			BigDecimal currencyId);

	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(
			DiscountDetailsReqRespDTO discountMgmtReqDTO);

	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData();

	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(BigDecimal groupId, BigDecimal currencyId);

	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(BigDecimal groupId);

	public AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData(
			OnlineMarginMarkupReq OnlineMarginMarkupReq);

	public AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData(
			OnlineMarginMarkupInfo OnlineMarginMarkupReq);

	public AmxApiResponse<GroupDetails, Object> getGroupsOfType(String groupType);

	public AmxApiResponse<GroupDetails, Object> saveGroups(GroupDetails group);

	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(
			ExchRateEnquiryReqDto rateEnquiryReqDto);

	public AmxApiResponse<Long, Object> rateUpoadRuleMaker(RateUploadRequestDto rateUploadRequestDto);

	public AmxApiResponse<Long, Object> rateUpoadRuleChecker(RateUploadRequestDto rateUploadRequestDto);

}
