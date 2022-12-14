package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentBranchParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceProviderParam;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.JaxFieldClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.bene.BeneBranchClient;
import com.amx.jax.client.bene.BeneficaryStatusDto;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.dict.ContactType;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.BankBranchListRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.request.benebranch.ListBeneRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneBankRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneCashRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.model.request.remittance.GetServiceApplicabilityRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.benebranch.AddBeneResponse;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.benebranch.BeneStatusDto;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.remittance.GetServiceApplicabilityResponse;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Beneficiary APIs")
public class BeneficiaryBranchController {

	@Autowired
	private BeneBranchClient beneBranchClient;

	@Autowired
	BeneClient beneClient;

	@Autowired
	MetaClient metaClient;

	@Autowired
	RemittanceClient remittanceClient;

	@Autowired
	private JaxFieldClient jaxFieldClient;

	// Add Bene Api's
	@RequestMapping(value = "/api/bene/country/list", method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterDTO, Object> getBeneficiaryCountryList() {
		return beneClient.getBeneficiaryCountryList().toAmxApiResponse();
	}

	@RequestMapping(value = "/api/bene/currency/list", method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> getBeneficiaryCurrency(@RequestParam BigDecimal countryId,
			@RequestParam(required = false) BigDecimal serviceGroupId,
			@RequestParam(required = false) BigDecimal routingBankId) {
		return metaClient.getBeneficiaryCurrency(countryId, serviceGroupId, routingBankId);
	}

	@RequestMapping(value = "/api/bene/service_provider/list", method = RequestMethod.POST)
	public AmxApiResponse<RoutingBankMasterDTO, Object> getServiceProvider(
			@RequestBody RoutingBankMasterServiceProviderParam param) {
		return beneClient.getServiceProvider(param).toAmxApiResponse();
	}

	@RequestMapping(value = "/api/bene/status_master/list", method = RequestMethod.GET)
	public AmxApiResponse<BeneficaryStatusDto, Object> getBeneStatusMaster(
			@RequestParam(required = false) BigDecimal serviceGroupId) {
		return beneClient.getBeneStatusMaster(serviceGroupId);
	}

	@RequestMapping(value = "/api/bene/agent_branch/list", method = RequestMethod.POST)
	public AmxApiResponse<RoutingBankMasterDTO, Object> getAgentBranch(
			@RequestBody RoutingBankMasterAgentBranchParam param) {
		return beneClient.getAgentBranch(param).toAmxApiResponse();
	}

	@RequestMapping(value = "/api/bene/bank/list", method = RequestMethod.POST)
	@ApiOperation("List bank by country and currency")
	public AmxApiResponse<BankMasterDTO, Object> listBeneBank(@RequestBody ListBeneBankOrCashRequest request) {
		return beneBranchClient.listBeneBank(request);
	}

	@RequestMapping(value = "/api/bene/accounts/list", method = RequestMethod.GET)
	public AmxApiResponse<AccountTypeDto, Object> getBeneficiaryAccountType(BigDecimal beneCountryId) {
		return beneClient.getBeneficiaryAccountType(beneCountryId).toAmxApiResponse();
	}

	@RequestMapping(value = "/api/bene/branch/list", method = RequestMethod.POST)
	public AmxApiResponse<BankBranchDto, Object> listBankBranch(@RequestBody BankBranchListRequest request) {
		return beneBranchClient.listBankBranch(request);

	}

	@RequestMapping(value = "/api/bene/details/list", method = RequestMethod.GET)
	public AmxApiResponse<BeneficiaryListDTO, Object> getBeneByIdNo(@RequestParam Integer idNo) {
		return beneBranchClient.getBeneByIdNo(idNo);
	}

	@RequestMapping(value = "/api/bene/agent_master/list", method = RequestMethod.POST)
	public AmxApiResponse<RoutingBankMasterDTO, Object> getAgentMaster(@RequestBody RoutingBankMasterAgentParam param) {
		return beneClient.getAgentMaster(param).toAmxApiResponse();
	}

	// Ifsc and SWift code mandatory api
	@RequestMapping(value = "/api/bene/fields/list", method = RequestMethod.POST)
	public AmxApiResponse<GetServiceApplicabilityResponse, Object> getServiceApplicability(
			@RequestBody GetServiceApplicabilityRequest request) {
		return remittanceClient.getServiceApplicability(request);

	}

	@RequestMapping(value = "/api/bene/form/fields", method = RequestMethod.GET)
	public AmxApiResponse<JaxConditionalFieldDto, Object> getDynamicFieldsForBeneficiary(
			@RequestParam BigDecimal beneCountryId,@RequestParam BigDecimal beneficaryTypeId) {
		return jaxFieldClient.getDynamicFieldsForBeneficiary(beneCountryId,beneficaryTypeId).toAmxApiResponse();
	}

	// Bene Mgmt Api's
	@RequestMapping(value = "/api/bene/list", method = RequestMethod.POST)
	public AmxApiResponse<BeneficiaryListDTO, Object> listBene(@RequestBody ListBeneRequest request) {
		return beneBranchClient.listBene(request);
	}

	@RequestMapping(value = "/api/bene/status/list", method = RequestMethod.GET)
	public AmxApiResponse<BeneStatusDto, Object> getBeneListStatuses() {
		return beneBranchClient.getBeneListStatuses();
	}

	@RequestMapping(value = "/api/bene/update_status", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> updateBeneStatus(
			@RequestHeader(value = "nOtp", required = false) String nOtpHeader,
			@RequestParam(required = false) String nOtp, @RequestBody UpdateBeneStatusRequest request) {
		JaxAuthContext.contactType(ContactType.NOTP_APP);
		nOtp = JaxAuthContext.nOtp(ArgUtil.ifNotEmpty(nOtp, nOtpHeader));
		JaxAuthContext.nOtp(nOtp);
		return beneBranchClient.updateBeneStatus(request);
	}

	@RequestMapping(value = "/api/bene/cash_agent/list", method = RequestMethod.POST)
	@ApiOperation("List cash agents by country and currency")
	public AmxApiResponse<RoutingBankMasterDTO, Object> listBeneCashAgents(
			@RequestBody @Valid ListBeneBankOrCashRequest request) {
		return beneBranchClient.listBeneCashAgents(request);
	}

	@RequestMapping(value = "/api/bene/add/bank", method = RequestMethod.POST)
	public AmxApiResponse<AddBeneResponse, Object> addBeneBank(
			@RequestHeader(value = "nOtp", required = false) String nOtpHeader,
			@RequestParam(required = false) String nOtp, @RequestBody @Valid AddBeneBankRequest request) {
		JaxAuthContext.contactType(ContactType.NOTP_APP);
		nOtp = JaxAuthContext.nOtp(ArgUtil.ifNotEmpty(nOtp, nOtpHeader));
		JaxAuthContext.nOtp(nOtp);
		return beneBranchClient.addBeneBank(request);
	}

	@RequestMapping(value = "/api/bene/add/cash", method = RequestMethod.POST)
	public AmxApiResponse<AddBeneResponse, Object> addBenecash(
			@RequestHeader(value = "nOtp", required = false) String nOtpHeader,
			@RequestParam(required = false) String nOtp, @RequestBody @Valid AddBeneCashRequest request) {
		JaxAuthContext.contactType(ContactType.NOTP_APP);
		nOtp = JaxAuthContext.nOtp(ArgUtil.ifNotEmpty(nOtp, nOtpHeader));
		JaxAuthContext.nOtp(nOtp);
		return beneBranchClient.addBenecash(request);
	}

	@RequestMapping(value = "/api/bene/add/branch", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> addNewBankBranchRequest(
			@RequestBody @Valid AddNewBankBranchRequest request) {
		return beneBranchClient.addNewBankBranchRequest(request);
	}

	@RequestMapping(value = "/api/bene/update/bank_details", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> updateBeneBank(
			@RequestHeader(value = "nOtp", required = false) String nOtpHeader,
			@RequestParam(required = false) String nOtp, @RequestBody @Valid UpdateBeneBankRequest request) {
		JaxAuthContext.contactType(ContactType.NOTP_APP);
		nOtp = JaxAuthContext.nOtp(ArgUtil.ifNotEmpty(nOtp, nOtpHeader));
		JaxAuthContext.nOtp(nOtp);
		return beneBranchClient.updateBeneBank(request);
	}

	@RequestMapping(value = "/api/bene/update/cash_details", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> updateBeneCash(
			@RequestHeader(value = "nOtp", required = false) String nOtpHeader,
			@RequestParam(required = false) String nOtp,@RequestBody @Valid UpdateBeneCashRequest request) {
		JaxAuthContext.contactType(ContactType.NOTP_APP);
		nOtp = JaxAuthContext.nOtp(ArgUtil.ifNotEmpty(nOtp, nOtpHeader));
		JaxAuthContext.nOtp(nOtp);
		return beneBranchClient.updateBeneCash(request);
	}


	/*
	 * @RequestMapping(value = "/api/bene/relations/list", method =
	 * RequestMethod.GET) public ApiResponse<BeneRelationsDescriptionDto>
	 * getBeneficiaryRelations() { return beneClient.getBeneficiaryRelations(); }
	 */

}
