package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentBranchParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceProviderParam;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.bene.BeneBranchClient;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.request.benebranch.ListBeneRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneBankRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneCashRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.benebranch.BeneStatusDto;

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

	@RequestMapping(value = "/api/bene/bank/list", method = RequestMethod.POST)
	@ApiOperation("List bank by country and currency")
	public AmxApiResponse<BankMasterDTO, Object> listBeneBank(@RequestBody ListBeneBankOrCashRequest request) {
		return beneBranchClient.listBeneBank(request);
	}

	@RequestMapping(value = "/api/bene/cash_agent/list", method = RequestMethod.POST)
	@ApiOperation("List cash agents by country and currency")
	public AmxApiResponse<RoutingBankMasterDTO, Object> listBeneCashAgents(
			@RequestBody @Valid ListBeneBankOrCashRequest request) {
		return beneBranchClient.listBeneCashAgents(request);
	}

	@RequestMapping(value = "/api/bene/add_bene_bank", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> addBeneBank(@RequestBody @Valid AddBeneBankRequest request) {
		return beneBranchClient.addBeneBank(request);
	}

	@RequestMapping(value = "/api/bene/add_bene_cash", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> addBenecash(@RequestBody @Valid AddBeneCashRequest request) {
		return beneBranchClient.addBenecash(request);
	}

	@RequestMapping(value = "/api/bene/add_new_branch", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> addNewBankBranchRequest(
			@RequestBody @Valid AddNewBankBranchRequest request) {
		return beneBranchClient.addNewBankBranchRequest(request);
	}

	@RequestMapping(value = "/api/bene/status/list", method = RequestMethod.GET)
	public AmxApiResponse<BeneStatusDto, Object> getBeneListStatuses() {
		return beneBranchClient.getBeneListStatuses();
	}

	@RequestMapping(value = "/api/bene/list", method = RequestMethod.POST)
	public AmxApiResponse<BeneficiaryListDTO, Object> listBene(ListBeneRequest request) {
		return beneBranchClient.listBene(request);
	}

	@RequestMapping(value = "/api/bene/update_status", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> updateBeneStatus(UpdateBeneStatusRequest request) {
		return beneBranchClient.updateBeneStatus(request);
	}

	@RequestMapping(value = "/api/bene/update_bank", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> updateBeneBank(@RequestBody @Valid UpdateBeneBankRequest request) {
		return beneBranchClient.updateBeneBank(request);
	}

	@RequestMapping(value = "/api/bene/update_cash", method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> updateBeneCash(@RequestBody @Valid UpdateBeneCashRequest request) {
		return beneBranchClient.updateBeneCash(request);
	}
//1
	@RequestMapping(value = "/api/bene/country/list", method = RequestMethod.GET)
	public ApiResponse<CountryMasterDTO> getBeneficiaryCountryList() {
		return beneClient.getBeneficiaryCountryList();
	}

	@RequestMapping(value = "/api/bene/agent_branch/list", method = RequestMethod.POST)
	public ApiResponse<RoutingBankMasterDTO> getAgentBranch(@RequestBody RoutingBankMasterAgentBranchParam param) {
		return beneClient.getAgentBranch(param);
	}

	@RequestMapping(value = "/api/bene/relations/list", method = RequestMethod.GET)
	public ApiResponse<BeneRelationsDescriptionDto> getBeneficiaryRelations() {
		return beneClient.getBeneficiaryRelations();
	}

	// TODO needs to check add it into meta or not
	@RequestMapping(value = "/api/bene/service_provider/list", method = RequestMethod.POST)
	public ApiResponse<RoutingBankMasterDTO> getServiceProvider(
			@RequestBody RoutingBankMasterServiceProviderParam param) {
		return beneClient.getServiceProvider(param);
	}
	
	@RequestMapping(value = "/api/bene/currency/list", method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> getBeneficiaryCurrency(@RequestParam BigDecimal countryId,
			@RequestParam(required = false) BigDecimal serviceGroupId,
			@RequestParam(required = false) BigDecimal routingBankId) {
		return metaClient.getBeneficiaryCurrency(countryId, serviceGroupId, routingBankId);
	}
	
	//service group api needs to be added

}
