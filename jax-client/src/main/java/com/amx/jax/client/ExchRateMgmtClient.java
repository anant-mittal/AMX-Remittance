package com.amx.jax.client;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.RateUploadRequestDto;
import com.amx.jax.pricer.dto.RateUploadRuleDto;
import com.amx.jax.pricer.dto.RoutingCountryBankInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.GROUP_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;
import com.amx.jax.rest.RestService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchRateMgmtClient extends AbstractJaxServiceClient {
	private static final Logger LOGGER = Logger.getLogger(ExchRateMgmtClient.class);

	@Autowired
	RestService restService;

	@Autowired
	PricerServiceClient pricerServiceClient;

	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(
			ExchRateEnquiryReqDto rateEnquiryReqDto) {
		return pricerServiceClient.enquireExchangeRates(rateEnquiryReqDto);
	}

	public AmxApiResponse<GroupDetails, Object> getGroupsOfType(GROUP_TYPE groupType) {
		return pricerServiceClient.getGroupsOfType(groupType);
	}

	public AmxApiResponse<GroupDetails, Object> saveGroup(GroupDetails group) {
		return pricerServiceClient.saveGroup(group);
	}

	public AmxApiResponse<Long, Object> deleteGroup(BigDecimal applicationCountryId, BigDecimal groupId,
			GROUP_TYPE groupType, String groupName) {
		return pricerServiceClient.deleteGroup(applicationCountryId, groupId, groupType, groupName);
	}

	public AmxApiResponse<RoutingCountryBankInfo, Object> getRoutingCountryBanksForCurrency(BigDecimal currencyId) {
		return pricerServiceClient.getRoutingCountryBanksForCurrency(currencyId);
	}

	public AmxApiResponse<List<RateUploadRuleDto>, Object> getRateUploadRulesByStatus(RATE_UPLOAD_STATUS status,
			Boolean onlyActive) {
		return pricerServiceClient.getRateUploadRulesByStatus(status, onlyActive);
	}

	public AmxApiResponse<Long, Object> rateUpoadRuleMaker(RateUploadRequestDto rateUploadRequestDto) {
		return pricerServiceClient.rateUpoadRuleMaker(rateUploadRequestDto);
	}

	public AmxApiResponse<Long, Object> rateUpoadRuleChecker(RateUploadRequestDto rateUploadRequestDto) {
		return pricerServiceClient.rateUpoadRuleChecker(rateUploadRequestDto);
	}

	// public AmxApiResponse<GroupDetails, Object> saveGroup(GroupDetails group) {
	// LOGGER.info("In save Group client");
	// if(ArgUtil.isEmpty(group.getApplCountryId())){
	// JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();
	// group.setApplCountryId(jaxMetaInfo.getCountryBranchId());
	// }
	// return
	// restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.SAVE_GROUP).post(group)
	// .as(new ParameterizedTypeReference<AmxApiResponse<GroupDetails, Object>>() {
	// });
	// }

}
