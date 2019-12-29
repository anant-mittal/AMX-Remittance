package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.amx.jax.multitenant.MultiTenantConnectionProviderImpl;
import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.CountryMasterDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.CurrencyOtherInfoDao;
import com.amx.jax.pricer.dao.ExchRateUploadDao;
import com.amx.jax.pricer.dao.ExchangeRateDao;
import com.amx.jax.pricer.dao.GroupingMasterDao;
import com.amx.jax.pricer.dao.RoutingProdStatusDao;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CountryMasterDescriptor;
import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.CurrencyOtherInfo;
import com.amx.jax.pricer.dbmodel.ExchRateUpload;
import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.dbmodel.VwExRoutingProductStatus;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.CountryMasterDTO;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto.BuySellRateDetails;
import com.amx.jax.pricer.dto.RateUploadRequestDto;
import com.amx.jax.pricer.dto.RateUploadRuleDto;
import com.amx.jax.pricer.dto.RoutingCountryBankInfo;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.repository.custom.ExchRatePopulateProcDao;
import com.amx.jax.pricer.repository.custom.ExchRatePopulateProcedure;
import com.amx.jax.pricer.var.PricerServiceConstants.IS_ACTIVE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;
import com.amx.utils.ArgUtil;

@Component
public class ExchangeRateManager {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateManager.class);

	private static final BigDecimal DEF_PAGE_NO = BigDecimal.ZERO;

	private static final BigDecimal DEF_PAGE_SIZE = new BigDecimal(15);

	@Autowired
	ExchangeRateDao exchangeRateDao;

	@Autowired
	CountryMasterDao countryMasterDao;

	@Autowired
	CurrencyMasterDao currencyMasterDao;

	@Autowired
	ServiceMasterDescDao serviceMasterDescDao;

	@Autowired
	CountryBranchDao countryBranchDao;

	@Autowired
	GroupingMasterDao groupingMasterDao;

	@Autowired
	ExchRateUploadDao exchRateUploadDao;

	@Autowired
	BankMasterDao bankMasterDao;

	@Autowired
	RoutingProdStatusDao routingProdStatusDao;

	@Autowired
	CurrencyOtherInfoDao currencyOtherInfoDao;

	@Autowired
	MultiTenantConnectionProviderImpl connectionProvider;

	@Autowired
	ExchRatePopulateProcDao ratePopulateProcDao;

	public ExchangeRateEnquiryRespDto enquireExchRate(ExchRateEnquiryReqDto rateEnquiryReqDto) {

		if (rateEnquiryReqDto.getCountryId() == null) {
			LOGGER.error("Missing Country Id");
			throw new PricerServiceException(PricerServiceError.INVALID_COUNTRY, "Invalid or Missing Country Id");
		}

		if (rateEnquiryReqDto.getCurrencyId() == null) {
			LOGGER.error("Missing Currency Id");
			throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY, "Invalid or Missing Currency Id");
		}

		if (rateEnquiryReqDto.getPageNo() == null) {
			rateEnquiryReqDto.setPageNo(DEF_PAGE_NO);
		}

		if (rateEnquiryReqDto.getPageSize() == null) {
			rateEnquiryReqDto.setPageSize(DEF_PAGE_SIZE);
		}

		Pageable pageable = new PageRequest(rateEnquiryReqDto.getPageNo().intValue(),
				rateEnquiryReqDto.getPageSize().intValue());

		List<ExchangeRateMasterApprovalDet> exchangeRateList = exchangeRateDao.getExchangeRatesForPredicates(
				rateEnquiryReqDto.getCountryId(), rateEnquiryReqDto.getCurrencyId(), rateEnquiryReqDto.getBankId(),
				rateEnquiryReqDto.getServiceIndId(), rateEnquiryReqDto.getCountryBranchId(), pageable);

		if (exchangeRateList == null || exchangeRateList.isEmpty()) {
			LOGGER.debug("No Exchnage Rate found for Given Filter");
			// throw new
			// PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
			// "Invalid or Missing Valid Exchange Rates for Selected Filters.");

			ExchangeRateEnquiryRespDto respDto = new ExchangeRateEnquiryRespDto();

			respDto.setPageNo(rateEnquiryReqDto.getPageNo());
			respDto.setPageSize(rateEnquiryReqDto.getPageSize());

			respDto.setRateDetails(new ArrayList<BuySellRateDetails>());

			return respDto;
		}

		List<BigDecimal> bankIds = new ArrayList<BigDecimal>();
		List<BigDecimal> serviceIds = new ArrayList<BigDecimal>();
		List<BigDecimal> branchIds = new ArrayList<BigDecimal>();

		for (ExchangeRateMasterApprovalDet aprdet : exchangeRateList) {

			if (!bankIds.contains(aprdet.getBankMaster().getBankId())) {
				bankIds.add(aprdet.getBankMaster().getBankId());
			}

			if (!serviceIds.contains(aprdet.getServiceId())) {
				serviceIds.add(aprdet.getServiceId());
			}

			if (!branchIds.contains(aprdet.getCountryBranchId())) {
				branchIds.add(aprdet.getCountryBranchId());
			}
		}

		CountryMasterModel countryMaster = countryMasterDao.getByCountryId(rateEnquiryReqDto.getCountryId());

		CountryMasterDescriptor countryMasterDescriptor = null;

		for (CountryMasterDescriptor desc : countryMaster.getFsCountryMasterDescs()) {
			if (desc.getLanguageId() != null && desc.getLanguageId().compareTo(BigDecimal.ONE) == 0) {
				countryMasterDescriptor = desc;
				break;
			}
		}

		CurrencyMasterModel currencyMasterModel = currencyMasterDao.getByCurrencyId(rateEnquiryReqDto.getCurrencyId());

		Map<BigDecimal, ServiceMasterDesc> serviceMasters = serviceMasterDescDao.getByServiceIdIn(serviceIds);

		Map<BigDecimal, CountryBranch> cbMap = countryBranchDao.getByCountryBranchIds(branchIds);

		CurrencyOtherInfo curInfo = currencyOtherInfoDao.getByCurrencyId(rateEnquiryReqDto.getCurrencyId());

		if (curInfo == null) {
			curInfo = new CurrencyOtherInfo();
		}

		ExchangeRateEnquiryRespDto respDto = new ExchangeRateEnquiryRespDto();

		respDto.setPageNo(rateEnquiryReqDto.getPageNo());
		respDto.setPageSize(rateEnquiryReqDto.getPageSize());

		List<BuySellRateDetails> rateDetails = new ArrayList<BuySellRateDetails>();

		for (ExchangeRateMasterApprovalDet aprdet : exchangeRateList) {

			BuySellRateDetails buySellRate = new BuySellRateDetails();

			buySellRate.setCurrencyId(aprdet.getCurrencyId());
			buySellRate.setCurrencyQuote(currencyMasterModel.getQuoteName());
			buySellRate.setCurrencyCode(currencyMasterModel.getCurrencyCode());

			buySellRate.setCountryId(countryMaster.getCountryId());
			buySellRate.setCountryName(countryMasterDescriptor.getCountryName());
			buySellRate.setCountryCode(countryMaster.getCountryCode());

			buySellRate.setBankId(aprdet.getBankMaster().getBankId());
			buySellRate.setBankCode(aprdet.getBankMaster().getBankCode());

			buySellRate.setServiceIndId(aprdet.getServiceId());

			String serviceName = serviceMasters.containsKey(aprdet.getServiceId())
					? serviceMasters.get(aprdet.getServiceId()).getServiceDesc()
					: null;

			buySellRate.setServiceIndName(serviceName);

			buySellRate.setCountryBranchId(aprdet.getCountryBranchId());

			String branchName = cbMap.containsKey(aprdet.getCountryBranchId())
					? cbMap.get(aprdet.getCountryBranchId()).getBranchName()
					: null;

			buySellRate.setBranchName(branchName);

			buySellRate.setSellRate(aprdet.getSellRateMax());
			buySellRate.setBuyRate(aprdet.getBuyRateMax());

			buySellRate.setModifedDate(aprdet.getModifiedDate() != null ? aprdet.getModifiedDate().toString() : "");
			buySellRate.setApprovedDate(aprdet.getApprovedDate() != null ? aprdet.getApprovedDate().toString() : "");

			// Set Range
			buySellRate.setExchRateMin(curInfo.getFundMinRate());
			buySellRate.setExchRateMax(curInfo.getFundMaxRate());

			rateDetails.add(buySellRate);
		}

		respDto.setRateDetails(rateDetails);

		return respDto;

	}

	public Long rateUpoadRuleMaker(RateUploadRequestDto rateUploadRequestDto) {

		List<RateUploadRuleDto> rateUploadRules = rateUploadRequestDto.getRateUploadRules();

		if (rateUploadRules == null || rateUploadRules.isEmpty()) {
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_RULES,
					"Invalid or Missing Valid Exchange Rate Update Rules.");
		}

		// Resolve All Branch Groups
		List<BigDecimal> groupIds = new ArrayList<BigDecimal>();
		List<BigDecimal> currencyIds = new ArrayList<BigDecimal>();

		for (RateUploadRuleDto rule : rateUploadRules) {

			if (rule.getCurrencyId() == null || rule.getSellExchangeRate() == null
					|| rule.getBuyExchangeRate() == null) {
				throw new PricerServiceException(PricerServiceError.INVALID_OR_MISSING_PARAM,
						"Invalid or Missing Param, Currency: " + rule.getCurrencyId() + ", SellRate: "
								+ rule.getSellExchangeRate() + ", BuyRate: " + rule.getBuyExchangeRate());
			}

			Map<BigDecimal, String> groupIdNameMap = rule.getBranchGroupIdNameMap();
			if (groupIdNameMap != null && !groupIdNameMap.isEmpty()) {
				for (Entry<BigDecimal, String> e : groupIdNameMap.entrySet()) {
					if (!groupIds.contains(e.getKey())) {
						groupIds.add(e.getKey());
					}
				}
			}

			if (!currencyIds.contains(rule.getCurrencyId())) {
				currencyIds.add(rule.getCurrencyId());
			}

		}

		Map<BigDecimal, CurrencyOtherInfo> curInfoMap = currencyOtherInfoDao
				.getByApplicationCountryIdAndCurrencyIdIn(rateUploadRequestDto.getApplicationCountryId(), currencyIds);

		Map<BigDecimal, GroupingMaster> groupMasterMap;

		if (!groupIds.isEmpty()) {
			groupMasterMap = groupingMasterDao.getGroupById(groupIds);
		} else {
			groupMasterMap = new HashMap<BigDecimal, GroupingMaster>();
		}

		// Upload Rate
		List<ExchRateUpload> rateUploadMakerList = new ArrayList<ExchRateUpload>();

		for (RateUploadRuleDto rule : rateUploadRules) {

			// Check for the Currency Range Limit
			if (curInfoMap.containsKey(rule.getCurrencyId())) {
				CurrencyOtherInfo curInfo = curInfoMap.get(rule.getCurrencyId());

				// check for lower and upper Limit
				BigDecimal fundRateMax = curInfo.getFundMaxRate();
				BigDecimal fundRateMin = curInfo.getFundMinRate();

				if ((fundRateMax != null && fundRateMax.compareTo(rule.getSellExchangeRate()) < 0)
						|| (fundRateMin != null && fundRateMin.compareTo(rule.getSellExchangeRate()) > 0)) {

					throw new PricerServiceException(PricerServiceError.SELL_RATE_MIN_MAX_RANGE_VIOLATED,
							"Sell Rate is out of bound, Min-Max Range is violated for Currency: " + rule.getCurrencyId()
									+ ", SellRate: " + rule.getSellExchangeRate() + " Expected Range - Min: "
									+ fundRateMin + ", Max: " + fundRateMax);

				}

				if ((fundRateMax != null && fundRateMax.compareTo(rule.getBuyExchangeRate()) < 0)
						|| (fundRateMin != null && fundRateMin.compareTo(rule.getBuyExchangeRate()) > 0)) {

					throw new PricerServiceException(PricerServiceError.BUY_RATE_MIN_MAX_RANGE_VIOLATED,
							"Sell Rate is out of bound, Min-Max Range is violated for Currency: " + rule.getCurrencyId()
									+ ", SellRate: " + rule.getSellExchangeRate() + " Expected Range - Min: "
									+ fundRateMin + ", Max: " + fundRateMax);

				}

			}

			// No Modify : Only Create New Rule

			List<ExchRateUpload> singleRuleRateUploadRows = new ArrayList<ExchRateUpload>();

			ExchRateUpload rateUpload = new ExchRateUpload();

			// Generate New UUId for creating new Rule
			rateUpload.setRuleId(UUID.randomUUID().toString());

			// Set Currency Id
			rateUpload.setCurrencyId(rule.getCurrencyId());

			// Set Mandatory Data
			rateUpload.setSellRate(rule.getSellExchangeRate());
			rateUpload.setBuyRate(rule.getBuyExchangeRate());

			Date today = new Date();
			rateUpload.setCreatedBy(rateUploadRequestDto.getUpdatedBy());
			rateUpload.setCreatedDate(today);

			rateUpload.setModifiedBy(rateUploadRequestDto.getUpdatedBy());
			rateUpload.setModifiedDate(today);

			rateUpload.setIsActive(IS_ACTIVE.Y);
			rateUpload.setStatus(RATE_UPLOAD_STATUS.CREATED);

			// Conditional : Set Country Id
			if (rule.getCountryId() != null) {
				rateUpload.setCountryId(rule.getCountryId());
			}

			singleRuleRateUploadRows.add(rateUpload);

			// Process for All Cor Bank Ids
			if (rule.getBankIdQuoteMap() != null && !rule.getBankIdQuoteMap().isEmpty()) {
				List<ExchRateUpload> swapRuleSet = new ArrayList<ExchRateUpload>();

				for (ExchRateUpload transientRate : singleRuleRateUploadRows) {

					for (BigDecimal bId : rule.getBankIdQuoteMap().keySet()) {
						ExchRateUpload newCloned = transientRate.clone();
						newCloned.setCorBankId(bId);
						swapRuleSet.add(newCloned);
					} // for
				} // for

				singleRuleRateUploadRows = swapRuleSet;

			} // if

			// Process for all Service Ids

			if (rule.getServiceIdNameMap() != null && !rule.getServiceIdNameMap().isEmpty()) {
				List<ExchRateUpload> swapRuleSet = new ArrayList<ExchRateUpload>();

				for (ExchRateUpload transientRate : singleRuleRateUploadRows) {

					for (BigDecimal sId : rule.getServiceIdNameMap().keySet()) {
						ExchRateUpload newCloned = transientRate.clone();
						newCloned.setServiceId(sId);
						swapRuleSet.add(newCloned);
					} // for
				} // for

				singleRuleRateUploadRows = swapRuleSet;

			} // if

			if (rule.getcBranchIdNameMap() == null) {
				rule.setcBranchIdNameMap(new HashMap<BigDecimal, String>());
			}

			Map<BigDecimal, String> branchIdNameMap = rule.getcBranchIdNameMap();

			if (groupMasterMap != null && !groupMasterMap.isEmpty()) {
				groupMasterMap.values().stream().forEach(val -> branchIdNameMap.put(val.getId(), null));
			}

			// Process for All Branch Ids
			if (!rule.getcBranchIdNameMap().isEmpty()) {
				List<ExchRateUpload> swapRuleSet = new ArrayList<ExchRateUpload>();

				for (ExchRateUpload transientRate : singleRuleRateUploadRows) {

					for (BigDecimal brId : rule.getcBranchIdNameMap().keySet()) {
						ExchRateUpload newCloned = transientRate.clone();
						newCloned.setCountryBranchId(brId);
						swapRuleSet.add(newCloned);
					} // for
				} // for

				singleRuleRateUploadRows = swapRuleSet;

			} // if

			rateUploadMakerList.addAll(singleRuleRateUploadRows);

		}

		List<ExchRateUpload> saved;

		try {
			saved = exchRateUploadDao.saveAll(rateUploadMakerList);
		} catch (DataIntegrityViolationException e) {
			throw new PricerServiceException(PricerServiceError.INVALID_DATA_IDS_FOR_RULES,
					"Invalid Data Ref Ids for the rule maker.");
		}

		return new Long(saved.size());
	}

	// @Transactional
	synchronized public Long rateUpoadRuleChecker(RateUploadRequestDto rateUploadRequestDto) {

		if (rateUploadRequestDto.getRuleStatusUpdateMap() == null
				|| rateUploadRequestDto.getRuleStatusUpdateMap().isEmpty()) {
			throw new PricerServiceException(PricerServiceError.MISSING_RULE_IDS, "Null or Missing Rule Ids");
		}

		Map<RATE_UPLOAD_STATUS, Set<String>> ruleStatusUpdateMap = new HashMap<RATE_UPLOAD_STATUS, Set<String>>();

		for (Entry<String, RATE_UPLOAD_STATUS> entry : rateUploadRequestDto.getRuleStatusUpdateMap().entrySet()) {

			RATE_UPLOAD_STATUS status = entry.getValue();

			if (!ArgUtil.presentIn(status, RATE_UPLOAD_STATUS.APPROVED, RATE_UPLOAD_STATUS.REJECTED)) {
				throw new PricerServiceException(PricerServiceError.INVALID_RULE_UPDATE_STATUS,
						"Invalid or Missing Rule Update Status, should be one of: " + RATE_UPLOAD_STATUS.APPROVED
								+ " OR " + RATE_UPLOAD_STATUS.REJECTED);
			}

			if (!ruleStatusUpdateMap.containsKey(status)) {
				ruleStatusUpdateMap.put(status, new HashSet<String>());
			}

			ruleStatusUpdateMap.get(status).add(entry.getKey());
		}

		// Validate Approved and Rejected Rules - for - No Intersections.
		Set<String> union = new HashSet<String>();
		int matcher = 0;

		if (ruleStatusUpdateMap.containsKey(RATE_UPLOAD_STATUS.APPROVED)) {
			union.addAll(ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.APPROVED));
			matcher += ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.APPROVED).size();
		}

		if (ruleStatusUpdateMap.containsKey(RATE_UPLOAD_STATUS.REJECTED)) {
			union.addAll(ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.REJECTED));
			matcher += ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.REJECTED).size();
		}

		if (matcher != union.size()) {
			// throw Conflicting Rule Status Change
			throw new PricerServiceException(PricerServiceError.CONFLICTING_RULE_UPDATE_STATUS,
					"Conflicting Rule Update Status for one of the Same Rule Id");

		}

		List<ExchRateUpload> createdRules = exchRateUploadDao.getByRuleIdIn(union);

		validateRateUploadRulesForApproval(createdRules);

		// Match That all the Entries for the Rules Exist and Valid.

		int totalRowsUpdated = 0;
		Date today = new Date();

		if (ruleStatusUpdateMap.containsKey(RATE_UPLOAD_STATUS.APPROVED)) {

			totalRowsUpdated += exchRateUploadDao.updateApprovalForRuleIdIn(
					ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.APPROVED), rateUploadRequestDto.getUpdatedBy(), today);

			String[] rulesApproved = new String[ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.APPROVED).size()];

			ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.APPROVED).toArray(rulesApproved);

			DataSource ds = connectionProvider.getDataSource();

			ExchRatePopulateProcedure proc = new ExchRatePopulateProcedure(ds);
			// ExchRatePopulateProcedure proc = new ExchRatePopulateProcedure(dataSource);

			proc.populateExchRates(rateUploadRequestDto.getApplicationCountryId(), rulesApproved,
					rateUploadRequestDto.getUpdatedBy());

			// proc.execute(rateUploadRequestDto.getApplicationCountryId(), rulesApproved,
			// rateUploadRequestDto.getUpdatedBy());

			/**
			 * Old Procedure Call via JdbcTemplate
			 */

			// Map<String, Object> output =
			// ratePopulateProcDao.callProcedurePopulateExchRate(
			// rateUploadRequestDto.getApplicationCountryId(), rulesApproved,
			// rateUploadRequestDto.getUpdatedBy());

			// System.out.println(" Output ==> " + output);

		}

		if (ruleStatusUpdateMap.containsKey(RATE_UPLOAD_STATUS.REJECTED)) {

			totalRowsUpdated += exchRateUploadDao.updateStatusForRuleIdIn(
					ruleStatusUpdateMap.get(RATE_UPLOAD_STATUS.REJECTED), RATE_UPLOAD_STATUS.REJECTED,
					rateUploadRequestDto.getUpdatedBy(), today);
		}

		return new Long(totalRowsUpdated);

	}

	public RoutingCountryBankInfo getRoutingCountryBanksForCurrency(BigDecimal currencyId) {

		if (currencyId == null) {
			throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY, "Invalid Currency");
		}

		List<VwExRoutingProductStatus> routingProdStatusList = routingProdStatusDao.getByCurrencyId(currencyId);

		RoutingCountryBankInfo routingCountryBankInfo = new RoutingCountryBankInfo();

		if (routingProdStatusList == null || routingProdStatusList.isEmpty()) {
			return routingCountryBankInfo;
		}

		Map<BigDecimal, List<BigDecimal>> banksForCountry = new HashMap<BigDecimal, List<BigDecimal>>();

		Map<BigDecimal, CountryMasterDTO> countries = new HashMap<BigDecimal, CountryMasterDTO>();

		Map<BigDecimal, BankDetailsDTO> routingBanks = new HashMap<BigDecimal, BankDetailsDTO>();

		for (VwExRoutingProductStatus status : routingProdStatusList) {

			if (!countries.containsKey(status.getCountryId())) {
				countries.put(status.getCountryId(), null);
			}

			if (!routingBanks.containsKey(status.getBankId())) {
				routingBanks.put(status.getBankId(), null);
			}

			if (!banksForCountry.containsKey(status.getCountryId())) {

				List<BigDecimal> banks = new ArrayList<BigDecimal>();
				banks.add(status.getBankId());

				banksForCountry.put(status.getCountryId(), banks);
			} else {

				if (!banksForCountry.get(status.getCountryId()).contains(status.getBankId())) {
					banksForCountry.get(status.getCountryId()).add(status.getBankId());
				}
			}
		}

		List<CountryMasterModel> countryMasters = countryMasterDao
				.getByCountryIdIn(new ArrayList<BigDecimal>(countries.keySet()));

		for (CountryMasterModel country : countryMasters) {
			if (countries.get(country.getCountryId()) == null) {

				CountryMasterDTO dto = new CountryMasterDTO();
				dto.setCountryId(country.getCountryId());
				dto.setCountryCode(country.getCountryCode());

				for (CountryMasterDescriptor descriptor : country.getFsCountryMasterDescs()) {
					if (descriptor != null && descriptor.getLanguageId() != null
							&& descriptor.getLanguageId().compareTo(BigDecimal.ONE) == 0) {
						dto.setCountryName(descriptor.getCountryName());
					}
				}

				countries.put(country.getCountryId(), dto);
			}
		}

		Map<BigDecimal, BankMasterModel> bankMasters = bankMasterDao
				.getBankByIdIn(new ArrayList<BigDecimal>(routingBanks.keySet()));

		for (BankMasterModel bankMaster : bankMasters.values()) {
			if (routingBanks.get(bankMaster.getBankId()) == null) {
				BankDetailsDTO dto = new BankDetailsDTO();
				dto.setBankId(bankMaster.getBankId());
				dto.setBankCode(bankMaster.getBankCode());
				dto.setBankCountryId(bankMaster.getBankCountryId());
				dto.setBankFullName(bankMaster.getBankFullName());
				dto.setBankShortName(bankMaster.getBankShortName());

				routingBanks.put(bankMaster.getBankId(), dto);
			}
		}

		routingCountryBankInfo.setBanksForCountry(banksForCountry);
		routingCountryBankInfo.setCountries(new ArrayList<CountryMasterDTO>(countries.values()));
		routingCountryBankInfo.setRoutingBanks(new ArrayList<BankDetailsDTO>(routingBanks.values()));

		return routingCountryBankInfo;
	}

	private boolean validateRateUploadRulesForApproval(List<ExchRateUpload> toBeApprovedList) {

		List<String> invalidRules = new ArrayList<String>();
		boolean isInactive = false;
		boolean isApproved = false;

		for (ExchRateUpload rateUpload : toBeApprovedList) {

			if (invalidRules.contains(rateUpload.getRuleId())) {
				continue;
			}

			if (rateUpload.getIsActive() == null || !IS_ACTIVE.Y.equals(rateUpload.getIsActive())) {
				invalidRules.add(rateUpload.getRuleId());
				isInactive = true;
			} else if (rateUpload.getStatus() == null || !rateUpload.getStatus().equals(RATE_UPLOAD_STATUS.CREATED)) {
				invalidRules.add(rateUpload.getRuleId());
				isApproved = true;
			}

		}

		if (isInactive && !invalidRules.isEmpty()) {
			throw new PricerServiceException(PricerServiceError.INACTIVE_RULES,
					"One or more Rule(s) are Not Active: " + invalidRules.toString());
		} else if (isApproved && !invalidRules.isEmpty()) {
			throw new PricerServiceException(PricerServiceError.RULE_STATUS_ALREADY_UPDATED,
					"Status of one or more Rule(s) are already Updated: " + invalidRules.toString());
		}

		return true;

	}

	public List<RateUploadRuleDto> getRateUploadRulesByStatus(RATE_UPLOAD_STATUS status, Boolean onlyActive) {

		IS_ACTIVE active = (onlyActive == null || onlyActive) ? IS_ACTIVE.Y : IS_ACTIVE.N;

		List<ExchRateUpload> exchRateUploads;

		if (IS_ACTIVE.Y.equals(active)) {
			exchRateUploads = exchRateUploadDao.getRulesByStatusAndActive(status, active);
		} else {
			exchRateUploads = exchRateUploadDao.getRulesByStatus(status);
		}

		Map<String, RateUploadRuleDto> rateUploadMap = new HashMap<String, RateUploadRuleDto>();

		List<String> ruleSequenceList = new ArrayList<String>();

		if (exchRateUploads != null && !exchRateUploads.isEmpty()) {

			List<BigDecimal> curIds = new ArrayList<BigDecimal>();
			List<BigDecimal> countryIds = new ArrayList<BigDecimal>();

			List<BigDecimal> bankIds = new ArrayList<BigDecimal>();
			List<BigDecimal> cBranchIds = new ArrayList<BigDecimal>();
			List<BigDecimal> serviceIds = new ArrayList<BigDecimal>();

			for (ExchRateUpload exchRateUpload : exchRateUploads) {

				if (exchRateUpload.getCurrencyId() != null && !curIds.contains(exchRateUpload.getCurrencyId())) {
					curIds.add(exchRateUpload.getCurrencyId());
				}

				if (exchRateUpload.getCountryId() != null && !countryIds.contains(exchRateUpload.getCountryId())) {
					countryIds.add(exchRateUpload.getCountryId());
				}

				if (exchRateUpload.getCorBankId() != null && !bankIds.contains(exchRateUpload.getCorBankId())) {
					bankIds.add(exchRateUpload.getCorBankId());
				}

				if (exchRateUpload.getCountryBranchId() != null
						&& !cBranchIds.contains(exchRateUpload.getCountryBranchId())) {
					cBranchIds.add(exchRateUpload.getCountryBranchId());
				}

				if (exchRateUpload.getServiceId() != null && !serviceIds.contains(exchRateUpload.getServiceId())) {
					serviceIds.add(exchRateUpload.getServiceId());
				}
			}

			Map<BigDecimal, CurrencyMasterModel> currencyMasters = currencyMasterDao.getCurrencyByIds(curIds).stream()
					.collect(Collectors.toMap(c -> c.getCurrencyId(), c -> c));

			Map<BigDecimal, CountryMasterModel> countryMasters = new HashMap<BigDecimal, CountryMasterModel>();
			Map<BigDecimal, CountryMasterDescriptor> countryDescriptors = new HashMap<BigDecimal, CountryMasterDescriptor>();

			if (countryIds != null && !countryIds.isEmpty()) {

				countryMasters = countryMasterDao.getByCountryIdIn(countryIds).stream()
						.collect(Collectors.toMap(c -> c.getCountryId(), c -> c));

				for (CountryMasterModel countryMaster : countryMasters.values()) {
					for (CountryMasterDescriptor desc : countryMaster.getFsCountryMasterDescs()) {
						if (desc.getLanguageId() != null && desc.getLanguageId().compareTo(BigDecimal.ONE) == 0) {
							countryDescriptors.put(countryMaster.getCountryId(), desc);
							break;
						}
					}
				}
			}

			Map<BigDecimal, BankMasterModel> bankMasters = new HashMap<>();
			if (!bankIds.isEmpty()) {
				bankMasters = bankMasterDao.getBankByIdIn(bankIds);
			}

			Map<BigDecimal, CountryBranch> cBranches = new HashMap<>();
			if (!cBranchIds.isEmpty()) {
				cBranches = countryBranchDao.getByCountryBranchIds(cBranchIds);
			}

			Map<BigDecimal, ServiceMasterDesc> sMasterDescs = new HashMap<>();
			if (!serviceIds.isEmpty()) {
				sMasterDescs = serviceMasterDescDao.getByServiceIdIn(serviceIds);
			}

			for (ExchRateUpload exchRateUpload : exchRateUploads) {

				RateUploadRuleDto rateDto;

				if (!rateUploadMap.containsKey(exchRateUpload.getRuleId())) {
					rateDto = new RateUploadRuleDto();
					rateDto.setRuleId(exchRateUpload.getRuleId());
					rateDto.setCurrencyId(exchRateUpload.getCurrencyId());

					CurrencyMasterModel currencyMasterModel = currencyMasters.get(exchRateUpload.getCurrencyId());

					rateDto.setCurDisplayName(
							currencyMasterModel.getCurrencyCode() + "-" + currencyMasterModel.getQuoteName());

					// Set Empty Structures
					rateDto.setBankIdQuoteMap(new HashMap<BigDecimal, String>());
					rateDto.setServiceIdNameMap(new HashMap<BigDecimal, String>());
					rateDto.setcBranchIdNameMap(new HashMap<BigDecimal, String>());

					// Set rates
					rateDto.setSellExchangeRate(exchRateUpload.getSellRate());
					rateDto.setBuyExchangeRate(exchRateUpload.getBuyRate());

					// Info
					rateDto.setIsActive(exchRateUpload.getIsActive());
					rateDto.setStatus(exchRateUpload.getStatus());
					rateDto.setCreatedBy(exchRateUpload.getCreatedBy());
					rateDto.setCreatedDate(exchRateUpload.getCreatedDate());
					rateDto.setModifiedBy(exchRateUpload.getModifiedBy());
					rateDto.setModifiedDate(exchRateUpload.getModifiedDate());
					rateDto.setApprovedBy(exchRateUpload.getApprovedBy());
					rateDto.setApprovedDate(exchRateUpload.getApprovedDate());

					if (exchRateUpload.getCountryId() != null) {
						rateDto.setCountryId(exchRateUpload.getCountryId());

						CountryMasterModel countryMasterModel = countryMasters.get(exchRateUpload.getCountryId());
						CountryMasterDescriptor countryMasterDescriptor = countryDescriptors
								.get(exchRateUpload.getCountryId());

						rateDto.setCountryDisplayName(
								countryMasterModel.getCountryCode() + "-" + countryMasterDescriptor.getCountryName());
					}

					rateUploadMap.put(rateDto.getRuleId(), rateDto);

					// Add to the Rule Processing Sequence
					ruleSequenceList.add(rateDto.getRuleId());

				} else {
					rateDto = rateUploadMap.get(exchRateUpload.getRuleId());
				}

				if (exchRateUpload.getCorBankId() != null) {

					BankMasterModel bankModel = bankMasters.get(exchRateUpload.getCorBankId());
					if (bankModel != null) {
						rateDto.getBankIdQuoteMap().put(exchRateUpload.getCorBankId(), bankModel.getBankCode());
					} else {
						rateDto.getBankIdQuoteMap().put(exchRateUpload.getCorBankId(), null);
					}
				}

				if (exchRateUpload.getServiceId() != null) {
					ServiceMasterDesc serviceDesc = sMasterDescs.get(exchRateUpload.getServiceId());
					if (serviceDesc != null) {
						rateDto.getServiceIdNameMap().put(exchRateUpload.getServiceId(), serviceDesc.getServiceDesc());
					} else {
						rateDto.getServiceIdNameMap().put(exchRateUpload.getServiceId(), null);
					}
				}

				if (exchRateUpload.getCountryBranchId() != null) {
					CountryBranch branch = cBranches.get(exchRateUpload.getCountryBranchId());
					if (branch != null) {
						rateDto.getcBranchIdNameMap().put(exchRateUpload.getCountryBranchId(), branch.getBranchName());
					} else {
						rateDto.getcBranchIdNameMap().put(exchRateUpload.getCountryBranchId(), null);
					}
				}

			}

		}

		ArrayList<RateUploadRuleDto> ruleDtoOrderedList = new ArrayList<RateUploadRuleDto>();
		
		for (String ruleId : ruleSequenceList) {
			ruleDtoOrderedList.add(rateUploadMap.get(ruleId));
		}

		return ruleDtoOrderedList;
	}

}
