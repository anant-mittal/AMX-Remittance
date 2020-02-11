package com.amx.jax.pricer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.amx.jax.pricer.dto.RateUploadRuleDto;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.dto.RoutingProductStatusDetails;
import com.amx.jax.pricer.dto.RoutingStatusUpdateRequestDto;
import com.amx.jax.pricer.dto.RoutingCountryBankInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.GROUP_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

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

	public AmxApiResponse<GroupDetails, Object> getGroupsOfType(GROUP_TYPE groupType);
	AmxApiResponse<RoutingProductStatusDetails, Object> getRoutingProductStatus(BigDecimal countryId,
			BigDecimal currencyId);

	AmxApiResponse<Integer, Object> updateRoutingProductStatus(RoutingStatusUpdateRequestDto request);
	public AmxApiResponse<GroupDetails, Object> saveGroup(GroupDetails group);

	public AmxApiResponse<Long, Object> deleteGroup(BigDecimal applicationCountryId, BigDecimal groupId,
			GROUP_TYPE groupType, String groupName);

	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(
			ExchRateEnquiryReqDto rateEnquiryReqDto);

	public AmxApiResponse<Long, Object> rateUpoadRuleMaker(RateUploadRequestDto rateUploadRequestDto);

	public AmxApiResponse<Long, Object> rateUpoadRuleChecker(RateUploadRequestDto rateUploadRequestDto);

	public AmxApiResponse<List<RateUploadRuleDto>, Object> getRateUploadRulesByStatus(RATE_UPLOAD_STATUS status,
			Boolean onlyActive);

	public AmxApiResponse<RoutingCountryBankInfo, Object> getRoutingCountryBanksForCurrency(BigDecimal currencyId, Boolean isActive);

}
