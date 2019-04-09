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
import com.amx.amxlib.meta.model.AnnualIncomeRangeDTO;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.BranchDetailDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.DeclarationDTO;
import com.amx.amxlib.meta.model.PrefixDTO;
import com.amx.amxlib.meta.model.RoutingBankMasterDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.ViewAreaDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewGovernateAreaDto;
import com.amx.amxlib.meta.model.ViewGovernateDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentBranchParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceProviderParam;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.def.CacheForTenant;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.TenantService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class MetaController.
 */
@RestController
@Api(value = "Meta APIs")
public class MetaController {

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/** The tenant context. */
	@Autowired
	private TenantService tenantContext;

	@Autowired
	private OffsiteCustRegClient offsiteCustRegClient;

	/**
	 * Fund sources.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/meta/income_sources", method = { RequestMethod.POST })
	public ResponseWrapper<List<SourceOfIncomeDto>> fundSources() {
		return new ResponseWrapper<List<SourceOfIncomeDto>>(
				jaxService.setDefaults().getRemitClient().getSourceOfIncome().getResults());
	}

	/**
	 * Remitt purpose.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/meta/tranx_purpose", method = { RequestMethod.POST })
	public ResponseWrapper<List<SourceOfIncomeDto>> remittPurpose() {
		return new ResponseWrapper<List<SourceOfIncomeDto>>(
				jaxService.setDefaults().getRemitClient().getSourceOfIncome().getResults());
	}

	/**
	 * Services list.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/meta/services/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<ServiceGroupMasterDescDto>> servicesList() {
		return new ResponseWrapper<List<ServiceGroupMasterDescDto>>(
				jaxService.setDefaults().getMetaClient().getServiceGroupList().getResults());
	}

	/**
	 * Ccy list.
	 *
	 * @param xrate the xrate
	 * @return the response wrapper
	 */
	@RequestMapping(value = { "/api/meta/ccy/list" }, method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<List<CurrencyMasterDTO>> ccyList(@RequestParam(required = false) Boolean xrate) {
		if (xrate != null && xrate == true) {
			return new ResponseWrapper<List<CurrencyMasterDTO>>(
					jaxService.setDefaults().getMetaClient().getAllExchangeRateCurrencyList().getResults());
		}
		return new ResponseWrapper<List<CurrencyMasterDTO>>(tenantContext.getOnlineCurrencies());
	}

	/**
	 * Gets the name prefix list.
	 *
	 * @return the name prefix list
	 */
	@CacheForTenant
	@RequestMapping(value = { "/pub/meta/name_prefix/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<PrefixDTO>> getNamePrefixList() {
		return new ResponseWrapper<List<PrefixDTO>>(
				jaxService.setDefaults().getMetaClient().getAllPrefix().getResults());
	}

	/**
	 * Gets the list of countries.
	 *
	 * @param bene the bene
	 * @return the list of countries
	 */
	@CacheForTenant
	public ResponseWrapper<List<CountryMasterDTO>> getListOfCountries(Boolean bene) {
		if (bene == true) {
			return new ResponseWrapper<List<CountryMasterDTO>>(
					jaxService.setDefaults().getBeneClient().getBeneficiaryCountryList().getResults());
		}
		return new ResponseWrapper<List<CountryMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getAllCountry().getResults());
	}

	/**
	 * Gets the list of countries pub.
	 *
	 * @param bene the bene
	 * @return the list of countries pub
	 */
	@RequestMapping(value = { "/pub/meta/country/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<CountryMasterDTO>> getListOfCountriesPub(@RequestParam(required = false) Boolean bene) {
		return getListOfCountries(ArgUtil.parseAsBoolean(bene, false));
	}

	/**
	 * Gets the list of countries pri.
	 *
	 * @param bene the bene
	 * @return the list of countries pri
	 */
	@RequestMapping(value = { "/api/meta/country/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<CountryMasterDTO>> getListOfCountriesPri(@RequestParam(required = false) Boolean bene) {
		return getListOfCountries(ArgUtil.parseAsBoolean(bene, true));
	}

	/**
	 * Gets the list of states for country.
	 *
	 * @param countryId the country id
	 * @return the list of states for country
	 */
	@CacheForTenant
	@RequestMapping(value = { "/api/meta/state/list", "/pub/meta/state/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewStateDto>> getListOfStatesForCountry(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<ViewStateDto>>(
				jaxService.setDefaults().getMetaClient().getStateList(countryId).getResults());
	}

	/**
	 * Gets the list of districts for state.
	 *
	 * @param stateId the state id
	 * @return the list of districts for state
	 */
	@RequestMapping(value = { "/api/meta/district/list", "/pub/meta/district/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewDistrictDto>> getListOfDistrictsForState(@RequestParam BigDecimal stateId) {
		return new ResponseWrapper<List<ViewDistrictDto>>(
				jaxService.setDefaults().getMetaClient().getDistrictList(stateId).getResults());
	}

	@RequestMapping(value = { "/api/meta/city/list", "/pub/meta/city/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewCityDto>> getListOfCitiesForDistrict(@RequestParam BigDecimal districtId) {
		return new ResponseWrapper<List<ViewCityDto>>(
				jaxService.setDefaults().getMetaClient().getCitytList(districtId).getResults());
	}

	@RequestMapping(value = { "/pub/meta/v2/governate/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewGovernateDto>> getGovernateList() {
		return new ResponseWrapper<List<ViewGovernateDto>>(
				jaxService.setDefaults().getMetaClient().getGovernateList().getResults());
	}

	@RequestMapping(value = { "/pub/meta/v2/area/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewGovernateAreaDto>> getGovernateAreaList(@RequestParam BigDecimal governateId) {
		return new ResponseWrapper<List<ViewGovernateAreaDto>>(
				jaxService.setDefaults().getMetaClient().getGovernateAreaList(governateId).getResults());
	}

	/**
	 * @deprecated
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = { "/api/meta/area/list", "/pub/meta/area/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<ViewAreaDto>> getAreaList() {
		return new ResponseWrapper<List<ViewAreaDto>>(
				jaxService.setDefaults().getMetaClient().getAreaList().getResults());
	}

	/**
	 * Ccy bene list.
	 *
	 * @param countryId      the country id
	 * @param serviceGroupId the service group id
	 * @param routingBankId  the routing bank id
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/meta/bnfcry/ccy", method = { RequestMethod.GET })
	public ResponseWrapper<List<CurrencyMasterDTO>> ccyBeneList(@RequestParam BigDecimal countryId,
			@RequestParam(required = false) BigDecimal serviceGroupId,
			@RequestParam(required = false) BigDecimal routingBankId) {
		return new ResponseWrapper<List<CurrencyMasterDTO>>(jaxService.setDefaults().getMetaClient()
				.getBeneficiaryCurrency(countryId, serviceGroupId, routingBankId).getResults());
	}

	/**
	 * Gets the list of account types.
	 *
	 * @param countryId the country id
	 * @return the list of account types
	 */
	@RequestMapping(value = "/api/meta/bnfcry/accounts", method = { RequestMethod.GET })
	public ResponseWrapper<List<AccountTypeDto>> getListOfAccountTypes(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<AccountTypeDto>>(
				jaxService.setDefaults().getBeneClient().getBeneficiaryAccountType(countryId).getResults());
	}

	/**
	 * Gets the beneficiary relations.
	 *
	 * @return the beneficiary relations
	 */
	@ApiOperation(value = "Get beneficiary Relations")
	@RequestMapping(value = "/api/meta/bnfcry/relations", method = { RequestMethod.GET })
	public ResponseWrapper<List<BeneRelationsDescriptionDto>> getBeneficiaryRelations() {
		return new ResponseWrapper<List<BeneRelationsDescriptionDto>>(
				jaxService.setDefaults().getBeneClient().getBeneficiaryRelations().getResults());
	}

	/**
	 * Gets the list of banks.
	 *
	 * @param countryId the country id
	 * @return the list of banks
	 */
	@RequestMapping(value = "/api/meta/bank/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<BankMasterDTO>> getListOfBanks(@RequestParam BigDecimal countryId) {
		return new ResponseWrapper<List<BankMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getBankListForCountry(countryId).getResults());
	}

	/**
	 * Gets the list of bank branches.
	 *
	 * @param param the param
	 * @return the list of bank branches
	 */
	@RequestMapping(value = "/api/meta/bank_branch/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<BankBranchDto>> getListOfBankBranches(@RequestBody GetBankBranchRequest param) {
		return new ResponseWrapper<List<BankBranchDto>>(
				jaxService.setDefaults().getMetaClient().getBankBranchList(param).getResults());
	}

	/**
	 * Gets the list of setvice providers.
	 *
	 * @param param the param
	 * @return the list of setvice providers
	 */
	@RequestMapping(value = "/api/meta/service_provider/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfSetviceProviders(
			@RequestBody RoutingBankMasterServiceProviderParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getServiceProvider(param).getResults());
	}

	/**
	 * Gets the list of agents.
	 *
	 * @param param the param
	 * @return the list of agents
	 */
	@RequestMapping(value = "/api/meta/agent/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfAgents(@RequestBody RoutingBankMasterAgentParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getAgentMaster(param).getResults());
	}

	/**
	 * Gets the list of agent branches.
	 *
	 * @param param the param
	 * @return the list of agent branches
	 */
	@RequestMapping(value = "/api/meta/agent_branch/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RoutingBankMasterDTO>> getListOfAgentBranches(
			@RequestBody RoutingBankMasterAgentBranchParam param) {
		return new ResponseWrapper<List<RoutingBankMasterDTO>>(
				jaxService.setDefaults().getBeneClient().getAgentBranch(param).getResults());
	}

	/**
	 * Gets the exchange branches.
	 *
	 * @return the exchange branches
	 */
	// @CacheForTenant
	@RequestMapping(value = { "/pub/meta/branch/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<BranchDetailDTO>> getExchangeBranches() {
		return new ResponseWrapper<List<BranchDetailDTO>>(
				jaxService.setDefaults().getMetaClient().getAllBranchDetail().getResults());
	}

	@RequestMapping(value = { "/pub/meta/declaration/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<DeclarationDTO>> getDeclaration() {
		return ResponseWrapper.buildList(jaxService.setDefaults().getMetaClient().getDeclaration());
	}

	@RequestMapping(value = { "/pub/meta/designation/list" }, method = { RequestMethod.GET })
	public ResponseWrapper<List<ResourceDTO>> getDesignationList() {
		return ResponseWrapper.buildList(offsiteCustRegClient.getDesignationList());
	}

	@RequestMapping(value = "/pub/meta/income_range/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<AnnualIncomeRangeDTO>> getAnnualIncome() {
		return ResponseWrapper.buildList(jaxService.setDefaults().getUserclient().getIncome());
	}

}
