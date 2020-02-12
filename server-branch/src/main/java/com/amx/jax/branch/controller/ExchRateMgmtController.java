package com.amx.jax.branch.controller;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.IDiscManagementService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.beans.BranchSession;
import com.amx.jax.client.ExchRateMgmtClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.RateUploadRequestDto;
import com.amx.jax.pricer.dto.RateUploadRuleDto;
import com.amx.jax.pricer.dto.RoutingCountryBankInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.GROUP_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Discount Management APIs")
@ApiStatusService(IDiscManagementService.class)
public class ExchRateMgmtController {

	@Autowired
	ExchRateMgmtClient exchRateMgmtClient;

	@Autowired
	MetaClient metaClient;

	@Autowired
	BranchSession branchSession;

	@Autowired
	private SSOUser ssoUser;
	private static final Logger LOGGER = LoggerService.getLogger(BDiscountMgmtController.class);

	@RequestMapping(value = "/api/exch/rate/inquiry", method = { RequestMethod.POST })
	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(
			@RequestBody ExchRateEnquiryReqDto rateEnquiryReqDto) {
		return exchRateMgmtClient.enquireExchangeRates(rateEnquiryReqDto);
	}

	@RequestMapping(value = "/api/exch/group/get", method = { RequestMethod.POST })
	public AmxApiResponse<GroupDetails, Object> getGroupsOfType(@RequestParam GROUP_TYPE groupType) {
		return exchRateMgmtClient.getGroupsOfType(groupType);
	}

	@RequestMapping(value = "/api/exch/group/create", method = { RequestMethod.POST })
	public AmxApiResponse<GroupDetails, Object> saveGroup(@RequestBody GroupDetails group) {
		if (ArgUtil.isEmpty(group.getApplCountryId())) {
			group.setApplCountryId(ssoUser.getUserDetails().getCountryId());
		}
		return exchRateMgmtClient.saveGroup(group);
	}

	@RequestMapping(value = "/api/exch/group/delete", method = { RequestMethod.POST })
	public AmxApiResponse<Long, Object> deleteGroup(@RequestParam(required = false) BigDecimal applicationCountryId,
			@RequestParam BigDecimal groupId, @RequestParam GROUP_TYPE groupType, @RequestParam String groupName) {
		applicationCountryId = applicationCountryId == null ? ssoUser.getUserDetails().getCountryId()
				: applicationCountryId;
		return exchRateMgmtClient.deleteGroup(applicationCountryId, groupId, groupType, groupName);
	}

	@RequestMapping(value = "/api/exch/country_bank/get", method = { RequestMethod.POST })
	public AmxApiResponse<RoutingCountryBankInfo, Object> getCountryBankFromCurrency(BigDecimal currencyId) {
		return exchRateMgmtClient.getRoutingCountryBanksForCurrency(currencyId);
	}

	@PreAuthorize("hasPermission('RATE_MGMT.CHECKER', 'VIEW') or hasPermission('RATE_MGMT.MAKER', 'VIEW')")
	@RequestMapping(value = "/api/exch/checker/rules", method = { RequestMethod.POST })
	public AmxApiResponse<List<RateUploadRuleDto>, Object> getRateUploadRulesByStatus(
			@RequestParam RATE_UPLOAD_STATUS status, @RequestParam Boolean onlyActive) {
		return exchRateMgmtClient.getRateUploadRulesByStatus(status, onlyActive);
	}

	@PreAuthorize("hasPermission('RATE_MGMT.MAKER', 'VIEW')")
	@RequestMapping(value = "/api/exch/maker/submit", method = { RequestMethod.POST })
	public AmxApiResponse<Long, Object> rateUpoadRuleMaker(@RequestBody RateUploadRequestDto rateUploadRequestDto) {
		if (ArgUtil.isEmpty(rateUploadRequestDto.getUpdatedBy())) {
			rateUploadRequestDto.setUpdatedBy(ssoUser.getUserDetails().getEmployeeName());
		}
		if (ArgUtil.isEmpty(rateUploadRequestDto.getApplicationCountryId())){
			rateUploadRequestDto.setApplicationCountryId(ssoUser.getUserDetails().getCountryId());
		}
		return exchRateMgmtClient.rateUpoadRuleMaker(rateUploadRequestDto);
	}

	@PreAuthorize("hasPermission('RATE_MGMT.CHECKER', 'VIEW')")
	@RequestMapping(value = "/api/exch/checker/submit", method = { RequestMethod.POST })
	public AmxApiResponse<Long, Object> rateUpoadRuleChecker(@RequestBody RateUploadRequestDto rateUploadRequestDto) {
		if (ArgUtil.isEmpty(rateUploadRequestDto.getUpdatedBy())) {
			rateUploadRequestDto.setUpdatedBy(ssoUser.getUserDetails().getEmployeeName());
		}
		if (ArgUtil.isEmpty(rateUploadRequestDto.getApplicationCountryId())){
			rateUploadRequestDto.setApplicationCountryId(ssoUser.getUserDetails().getCountryId());
		}
		return exchRateMgmtClient.rateUpoadRuleChecker(rateUploadRequestDto);
	}

}
