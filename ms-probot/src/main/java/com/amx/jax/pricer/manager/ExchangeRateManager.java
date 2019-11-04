package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.CountryMasterDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.ExchangeRateDao;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CountryMasterDescriptor;
import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;
import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto.BuySellRateDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;

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

	/*
	 * @Autowired BankMasterDao bankMasterDao;
	 */

	@Autowired
	ServiceMasterDescDao serviceMasterDescDao;

	@Autowired
	CountryBranchDao countryBranchDao;

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
			LOGGER.error("No Exchnage Rate found for Given Filter");
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
					"Invalid or Missing Valid Exchange Rates for Selected Filters.");
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

		/***
		 * Not Required Map<BigDecimal, BankMasterModel> bankMasters =
		 * bankMasterDao.getBankByIdIn(bankIds);
		 * 
		 * if (bankMasters == null) { bankMasters = new HashMap<BigDecimal,
		 * BankMasterModel>(); }
		 */

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

}
