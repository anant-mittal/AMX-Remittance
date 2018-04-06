package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.RoutingBankMasterDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.amxlib.model.RoutingBankMasterParam;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.TenantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.rossillo.spring.web.mvc.CacheControl;

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

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/ccy/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<List<CurrencyMasterDTO>> ccyList() {
		return new ResponseWrapper<List<CurrencyMasterDTO>>(tenantContext.getOnlineCurrencies());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/country/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<CountryMasterDTO>> getListOfCountries() {
		return new ResponseWrapper<List<CountryMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getAllCountry().getResults());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/state/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewStateDto>> getListOfStatesForCountry(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<ViewStateDto>>(
				jaxService.setDefaults().getMetaClient().getStateList(countryId).getResults());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/district/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewDistrictDto>> getListOfDistrictsForState(@RequestParam BigDecimal stateId) {
		return new ResponseWrapper<List<ViewDistrictDto>>(
				jaxService.setDefaults().getMetaClient().getDistrictList(stateId).getResults());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/bank/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<BankMasterDTO>> getListOfBanks(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<BankMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getBankListForCountry(countryId).getResults());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/bank_branch/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<BankBranchDto>> getListOfBankBranches(
			@RequestParam(required = false) BigDecimal countryId, @RequestParam(required = false) BigDecimal bankId,
			@RequestParam(required = false) String ifscCode, @RequestParam(required = false) String swift,
			@RequestParam(required = false) String branchName) {
		return new ResponseWrapper<List<BankBranchDto>>(jaxService.setDefaults().getMetaClient()
				.getBankBranchList(new GetBankBranchRequest(bankId, countryId, ifscCode, swift, branchName))
				.getResults());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/service_provider/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfSetviceProviders(
			@RequestBody RoutingBankMasterParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getServiceProvider(param).getResults());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/agent/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfAgents(@RequestBody RoutingBankMasterParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getAgentMaster(param).getResults());
	}

	@CacheControl(maxAge = UIConstants.CACHE_TIME)
	@RequestMapping(value = "/api/meta/agent_branch/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfAgentBranches(
			@RequestBody RoutingBankMasterParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getAgentBranch(param).getResults());
	}

}
