package com.amx.jax.client;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.pricer.AbstractProbotInterface.ApiEndPoints;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.GROUP_TYPE;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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
		return pricerServiceClient.getGroupsOfType(groupType.toString());
	}

	public AmxApiResponse<GroupDetails, Object> saveGroup(GroupDetails group) {
		return pricerServiceClient.saveGroups(group);
	}

	// public AmxApiResponse<GroupDetails, Object> saveGroup(GroupDetails group) {
	// 	LOGGER.info("In save Group client");
	// 	if(ArgUtil.isEmpty(group.getApplCountryId())){
	// 		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();
	// 		group.setApplCountryId(jaxMetaInfo.getCountryBranchId());
	// 	}
	// 	return restService.ajax(appConfig.getPricerURL()).path(ApiEndPoints.SAVE_GROUPS).post(group)
	// 			.as(new ParameterizedTypeReference<AmxApiResponse<GroupDetails, Object>>() {
	// 			});
	// }

}
