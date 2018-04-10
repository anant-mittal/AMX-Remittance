package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.RoutingBankMasterDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentBranchParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceProviderParam;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.TenantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Meta APIs")
public class MetaController {

	@Autowired
	private JaxService jaxService;

	@Autowired
	private TenantService tenantContext;

	@RequestMapping(value = "/api/meta/income_sources", method = { RequestMethod.POST })
	public ResponseWrapper<List<SourceOfIncomeDto>> fundSources() {
		return new ResponseWrapper<List<SourceOfIncomeDto>>(
				jaxService.setDefaults().getRemitClient().getSourceOfIncome().getResults());
	}

	@RequestMapping(value = "/api/meta/tranx_purpose", method = { RequestMethod.POST })
	public ResponseWrapper<List<SourceOfIncomeDto>> remittPurpose() {
		return new ResponseWrapper<List<SourceOfIncomeDto>>(
				jaxService.setDefaults().getRemitClient().getSourceOfIncome().getResults());
	}

	@RequestMapping(value = "/api/meta/services/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<ServiceGroupMasterDescDto>> servicesList() {
		return new ResponseWrapper<List<ServiceGroupMasterDescDto>>(
				jaxService.setDefaults().getMetaClient().getServiceGroupList().getResults());
	}

	@RequestMapping(value = "/api/meta/ccy/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<List<CurrencyMasterDTO>> ccyList() {
		return new ResponseWrapper<List<CurrencyMasterDTO>>(tenantContext.getOnlineCurrencies());
	}

	@RequestMapping(value = "/api/meta/country/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<CountryMasterDTO>> getListOfCountries() {
		return new ResponseWrapper<List<CountryMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getAllCountry().getResults());
	}

	@RequestMapping(value = "/api/meta/state/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewStateDto>> getListOfStatesForCountry(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<ViewStateDto>>(
				jaxService.setDefaults().getMetaClient().getStateList(countryId).getResults());
	}

	@RequestMapping(value = "/api/meta/district/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewDistrictDto>> getListOfDistrictsForState(@RequestParam BigDecimal stateId) {
		return new ResponseWrapper<List<ViewDistrictDto>>(
				jaxService.setDefaults().getMetaClient().getDistrictList(stateId).getResults());
	}

	@RequestMapping(value = "/api/meta/bnfcry/ccy", method = { RequestMethod.GET })
	public ResponseWrapper<List<CurrencyMasterDTO>> ccyBeneList(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<CurrencyMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getBeneficiaryCurrency(countryId).getResults());
	}

	@RequestMapping(value = "/api/meta/bnfcry/accounts", method = { RequestMethod.GET })
	public ResponseWrapper<List<AccountTypeDto>> getListOfAccountTypes(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<AccountTypeDto>>(
				jaxService.setDefaults().getBeneClient().getBeneficiaryAccountType(countryId).getResults());
	}

	@ApiOperation(value = "Get beneficiary Relations")
	@RequestMapping(value = "/api/meta/bnfcry/relations", method = { RequestMethod.GET })
	public ResponseWrapper<List<BeneRelationsDescriptionDto>> getBeneficiaryRelations() {
		return new ResponseWrapper<List<BeneRelationsDescriptionDto>>(
				jaxService.setDefaults().getBeneClient().getBeneficiaryRelations().getResults());
	}

	@RequestMapping(value = "/api/meta/bank/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<BankMasterDTO>> getListOfBanks(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<BankMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getBankListForCountry(countryId).getResults());
	}

	@RequestMapping(value = "/api/meta/bank_branch/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<BankBranchDto>> getListOfBankBranches(@RequestBody GetBankBranchRequest param) {
		return new ResponseWrapper<List<BankBranchDto>>(
				jaxService.setDefaults().getMetaClient().getBankBranchList(param).getResults());
	}

	@RequestMapping(value = "/api/meta/service_provider/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfSetviceProviders(
			@RequestBody RoutingBankMasterServiceProviderParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getServiceProvider(param).getResults());
	}

	@RequestMapping(value = "/api/meta/agent/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfAgents(@RequestBody RoutingBankMasterAgentParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getAgentMaster(param).getResults());
	}

	@RequestMapping(value = "/api/meta/agent_branch/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfAgentBranches(
			@RequestBody RoutingBankMasterAgentBranchParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getAgentBranch(param).getResults());
	}

}
