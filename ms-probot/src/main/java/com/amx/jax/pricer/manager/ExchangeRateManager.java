package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.CountryMasterDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.ExchRateUploadDao;
import com.amx.jax.pricer.dao.ExchangeRateDao;
import com.amx.jax.pricer.dao.GroupingMasterDao;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CountryMasterDescriptor;
import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.ExchRateUpload;
import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto.BuySellRateDetails;
import com.amx.jax.pricer.dto.RateUploadRuleDto;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.var.PricerServiceConstants.IS_ACTIVE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

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
				rateEnquiryReqDto.getServiceIndId(), rateEnquiryReqDto.getBranchId(), pageable);

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

			rateDetails.add(buySellRate);
		}

		respDto.setRateDetails(rateDetails);

		return respDto;

	}

	public Long rateUpoadRuleMaker(List<RateUploadRuleDto> rateUploadRules) {

		if (rateUploadRules == null || rateUploadRules.isEmpty()) {
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_RULES,
					"Invalid or Missing Valid Exchange Rate Update Rules.");
		}

		// Resolve All Branch Groups
		List<BigDecimal> groupIds = new ArrayList<BigDecimal>();

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
		}

		Map<BigDecimal, GroupingMaster> groupMasterMap;

		if (!groupIds.isEmpty()) {
			groupMasterMap = groupingMasterDao.getGroupById(groupIds);
		} else {
			groupMasterMap = new HashMap<BigDecimal, GroupingMaster>();
		}

		// Upload Rate
		List<ExchRateUpload> rateUploadMakerList = new ArrayList<ExchRateUpload>();

		for (RateUploadRuleDto rule : rateUploadRules) {

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
			rateUpload.setCreatedBy(rule.getCreatedBy());
			rateUpload.setCreatedDate(today);

			rateUpload.setModifiedBy(rule.getCreatedBy());
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
						newCloned.setCorBankId(sId);
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

		List<ExchRateUpload> saved = exchRateUploadDao.saveAll(rateUploadMakerList);

		return new Long(saved.size());
	}

}
