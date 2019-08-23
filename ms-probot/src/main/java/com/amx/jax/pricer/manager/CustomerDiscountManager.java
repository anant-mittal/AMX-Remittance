package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.ExchRateAndRoutingTransientDataCache;
import com.amx.jax.partner.dto.CustomerDiscountReqDTO;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.CustomerDiscountDao;
import com.amx.jax.pricer.dao.CustomerExtendedDao;
import com.amx.jax.pricer.dao.DiscountMasterDao;
import com.amx.jax.pricer.dao.GroupingMasterDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.CustomerDiscountsView;
import com.amx.jax.pricer.dbmodel.CustomerExtended;
import com.amx.jax.pricer.dbmodel.DiscountMaster;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.util.DbValueUtil;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.utils.JsonUtil;

@Component
public class CustomerDiscountManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDiscountManager.class);

	@Autowired
	PipsMasterDao pipsMasterDao;

	@Autowired
	CustCatDiscountDao custCatDiscountDao;

	@Autowired
	ChannelDiscountDao channelDiscountDao;

	@Autowired
	CustomerExtendedDao customerExtendedDao;

	@Autowired
	CountryBranchDao countryBranchDao;

	@Autowired
	CurrencyMasterDao currencyMasterDao;

	@Autowired
	GroupingMasterDao groupingMasterDao;

	@Autowired
	DiscountMasterDao discountMasterDao;

	@Autowired
	CustomerDiscountDao customerDiscountDao;

	@Resource
	ExchRateAndRoutingTransientDataCache exchRateAndRoutingTransientDataCache;

	// private static BigDecimal PIPS_BANK_ID = new BigDecimal(78);
	private static BigDecimal OnlineCountryBranchId;

	private static BigDecimal BIGD_ZERO = new BigDecimal(0);

	public void getDiscountedRates(PricingRequestDTO pricingRequestDTO, Customer customer,
			CUSTOMER_CATEGORY customerCategory) {

		// Find the Currency Group to which the currency belongs
		CurrencyMasterModel currencyMasterModel = currencyMasterDao
				.getByCurrencyId(pricingRequestDTO.getForeignCurrencyId());

		GroupingMaster curGroup = null;

		if (currencyMasterModel.getCurrGroupId() != null) {
			curGroup = groupingMasterDao.getGroupById(currencyMasterModel.getCurrGroupId());

			if (curGroup == null) {
				LOGGER.warn(" ****** MAJOR : Currency Group is Null for Currency Group Id : "
						+ currencyMasterModel.getCurrGroupId() + " and Currency Id :"
						+ currencyMasterModel.getCurrencyId());
			}
		} else {
			LOGGER.warn(
					" ****** MAJOR : Currency Group is Null for Currency Id : " + currencyMasterModel.getCurrencyId());
		}

		// Compute Channel Discount
		BigDecimal channelDiscountPips = BigDecimal.ZERO;
		ChannelDiscount channelDiscount = channelDiscountDao.getDiscountByChannel(pricingRequestDTO.getChannel());

		if (channelDiscount != null && curGroup != null && DbValueUtil.isActive(channelDiscount.getIsActive())) {
			DiscountMaster channelDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
					DISCOUNT_TYPE.CHANNEL.getTypeKey(), channelDiscount.getId(), curGroup.getId());

			channelDiscountPips = ((null != channelDiscountMaster
					&& DbValueUtil.isActive(channelDiscountMaster.getIsActive()))
							? channelDiscountMaster.getDiscountPips()
							: BigDecimal.ZERO);
		}

		// Channel Info
		ExchangeDiscountInfo channelInfo = new ExchangeDiscountInfo();
		channelInfo.setId(channelDiscount.getId());
		channelInfo.setDiscountType(DISCOUNT_TYPE.CHANNEL);
		channelInfo.setDiscountTypeValue(pricingRequestDTO.getChannel().name());
		channelInfo.setDiscountPipsValue(channelDiscountPips);

		// Compute Customer category Discount
		BigDecimal ccDiscountPips = BigDecimal.ZERO;
		ExchangeDiscountInfo custCategoryInfo = new ExchangeDiscountInfo();

		if (customer != null) {
			CustomerExtended customerExtended = customerExtendedDao
					.getCustomerExtendedByCustomerId(customer.getCustomerId());

			CustomerCategoryDiscount ccDiscount = customerExtended != null
					? customerExtended.getCustomerCategoryDiscount()
					: null;

			if (ccDiscount != null && ccDiscount.getId() != null && curGroup != null
					&& DbValueUtil.isActive(ccDiscount.getIsActive())) {

				DiscountMaster ccDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
						DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), ccDiscount.getId(), curGroup.getId());

				ccDiscountPips = ((null != ccDiscountMaster && DbValueUtil.isActive(ccDiscountMaster.getIsActive()))
						? ccDiscountMaster.getDiscountPips()
						: BigDecimal.ZERO);

			}

			// Customer Category Info
			custCategoryInfo.setId(ccDiscount.getId());
			custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
			custCategoryInfo.setDiscountTypeValue(ccDiscount.getCustomerCategory().toString());
			custCategoryInfo.setDiscountPipsValue(ccDiscountPips);

			// Updated Customer Category
			exchRateAndRoutingTransientDataCache.setCustomerCategory(ccDiscount.getCustomerCategory());

		} else {
			CustomerCategoryDiscount ccDiscount = custCatDiscountDao.getDiscountByCustomerCategory(customerCategory);

			if (ccDiscount != null && curGroup != null && DbValueUtil.isActive(ccDiscount.getIsActive())) {
				DiscountMaster ccDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
						DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), ccDiscount.getId(), curGroup.getId());

				ccDiscountPips = ((null != ccDiscount && DbValueUtil.isActive(ccDiscountMaster.getIsActive()))
						? ccDiscountMaster.getDiscountPips()
						: BigDecimal.ZERO);
			}

			custCategoryInfo.setId(ccDiscount.getId());
			custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
			custCategoryInfo.setDiscountTypeValue(ccDiscount.getCustomerCategory().toString());
			custCategoryInfo.setDiscountPipsValue(ccDiscountPips);

		}

		List<BigDecimal> validBankIds = null;
		if (exchRateAndRoutingTransientDataCache.getBankDetails() != null) {
			validBankIds = new ArrayList<BigDecimal>(exchRateAndRoutingTransientDataCache.getBankDetails().keySet());
		}

		if (OnlineCountryBranchId == null) {
			CountryBranch cb = countryBranchDao.getOnlineCountryBranch();
			OnlineCountryBranchId = cb.getCountryBranchId();
		}

		List<PipsMaster> pipsList = null;
		if (validBankIds != null) {
			pipsList = pipsMasterDao.getPipsForFcCurAndBank(pricingRequestDTO.getForeignCurrencyId(),
					OnlineCountryBranchId, validBankIds);
		}

		Map<Long, TreeMap<BigDecimal, PipsMaster>> bankAmountSlabDiscounts = new HashMap<Long, TreeMap<BigDecimal, PipsMaster>>();

		if (null != pipsList) {
			for (PipsMaster pipsMaster : pipsList) {
				if (bankAmountSlabDiscounts.containsKey(pipsMaster.getBankMaster().getBankId().longValue())) {

					bankAmountSlabDiscounts.get(pipsMaster.getBankMaster().getBankId().longValue())
							.put(pipsMaster.getToAmount(), pipsMaster);

				} else {

					TreeMap<BigDecimal, PipsMaster> slabPipsMap = new TreeMap<BigDecimal, PipsMaster>();
					slabPipsMap.put(pipsMaster.getToAmount(), pipsMaster);
					bankAmountSlabDiscounts.put(pipsMaster.getBankMaster().getBankId().longValue(), slabPipsMap);
				}

			}
		}

		// List<BankRateDetailsDTO> discountedRatesNPrices = new
		// ArrayList<BankRateDetailsDTO>();

		// Old
		// BigDecimal margin =
		// exchRateAndRoutingTransientDataCache.getMarginForBank(bankId)) != null
		// ? exchRateAndRoutingTransientDataCache.getMargin().getMarginMarkup()
		// : BIGD_ZERO;

		for (ExchangeRateDetails bankExRateDetail : exchRateAndRoutingTransientDataCache.getSellRateDetails()) {

			// // Check if discount is already applied
			// // Avoid Double Discount Application
			// if (bankExRateDetail.isDiscountAvailed() == true) {
			// continue;
			// }

			BigDecimal amountSlabPips = BIGD_ZERO;
			ExchangeDiscountInfo amountSlabPipsInfo = new ExchangeDiscountInfo();

			if (bankAmountSlabDiscounts.containsKey(bankExRateDetail.getBankId().longValue())) {
				TreeMap<BigDecimal, PipsMaster> pipsMap = bankAmountSlabDiscounts
						.get(bankExRateDetail.getBankId().longValue());

				for (Entry<BigDecimal, PipsMaster> entry : pipsMap.entrySet()) {

					if (bankExRateDetail.getSellRateBase().getConvertedFCAmount().compareTo(entry.getKey()) <= 0) {
						amountSlabPips = entry.getValue().getPipsNo();

						amountSlabPipsInfo.setId(entry.getValue().getPipsMasterId());
						amountSlabPipsInfo.setDiscountType(DISCOUNT_TYPE.AMOUNT_SLAB);
						amountSlabPipsInfo.setDiscountTypeValue(entry.getValue().getFromAmount().longValue() + "-"
								+ entry.getValue().getToAmount().longValue());
						amountSlabPipsInfo.setDiscountPipsValue(amountSlabPips);

						Entry<BigDecimal, PipsMaster> nextEntry = pipsMap.higherEntry(entry.getKey());

						if (nextEntry != null) {
							bankExRateDetail.setBetterRateAvailable(true);
							bankExRateDetail.setBetterRateAmountSlab(nextEntry.getValue().getFromAmount());
						}
						break;
					} // if

				} // for
			}

			// Check if discount is already applied
			// Avoid Double Discount Application
			// Shifted place for Next Amount Slab Calculations
			if (bankExRateDetail.isDiscountAvailed() == true) {
				continue;
			}

			BigDecimal discountedSellRate;

			if (bankExRateDetail.isCostRateLimitReached()) {
				// No further Discounts If the Cost+Margin Limit is Reached at Base Sell Rate
				// Level.
				discountedSellRate = bankExRateDetail.getSellRateBase().getInverseRate();
				bankExRateDetail.setDiscountAvailed(false);
				
				// Set Better Rate Availability to false
				bankExRateDetail.setBetterRateAvailable(false);
				bankExRateDetail.setBetterRateAmountSlab(null);

			} else {

				BigDecimal totalDiscountPips = amountSlabPips.add(channelDiscountPips).add(ccDiscountPips);

				discountedSellRate = bankExRateDetail.getSellRateBase().getInverseRate().subtract(totalDiscountPips);

				bankExRateDetail.setDiscountAvailed(true);

				BigDecimal marginVal = exchRateAndRoutingTransientDataCache
						.getMarginForBank(bankExRateDetail.getBankId()).getMarginMarkup();

				/**
				 * Compute Base Sell rate : Cost + Margin
				 */
				BigDecimal adjustedBaseSellRate = BigDecimal.ZERO;

				if (exchRateAndRoutingTransientDataCache.getAvgRateGLCForBank(bankExRateDetail.getBankId()) != null) {

					// New Logic
					adjustedBaseSellRate = exchRateAndRoutingTransientDataCache
							.getAvgRateGLCForBank(bankExRateDetail.getBankId()).add(marginVal);

				}

				if (discountedSellRate.compareTo(adjustedBaseSellRate) <= 0) {

					discountedSellRate = adjustedBaseSellRate;

					bankExRateDetail.setDiscountAvailed(true);
					bankExRateDetail.setCostRateLimitReached(true);
					
					// Set Better Rate Availability to false
					bankExRateDetail.setBetterRateAvailable(false);
					bankExRateDetail.setBetterRateAmountSlab(null);

				}

			}

			bankExRateDetail.setCustomerDiscountDetails(new HashMap<DISCOUNT_TYPE, ExchangeDiscountInfo>());

			bankExRateDetail.getCustomerDiscountDetails().put(DISCOUNT_TYPE.CHANNEL, channelInfo);
			bankExRateDetail.getCustomerDiscountDetails().put(DISCOUNT_TYPE.CUSTOMER_CATEGORY, custCategoryInfo);
			bankExRateDetail.getCustomerDiscountDetails().put(DISCOUNT_TYPE.AMOUNT_SLAB, amountSlabPipsInfo);

			if (pricingRequestDTO.getLocalAmount() != null) {

				// Get Bank Wise Rates for Local Currency
				bankExRateDetail.setSellRateNet(RemitPriceManager.createBreakUpForLcCur(discountedSellRate,
						pricingRequestDTO.getLocalAmount()));

			} else {

				// Get Bank wise Rates for Foreign Currency
				bankExRateDetail.setSellRateNet(RemitPriceManager.createBreakUpForFcCur(discountedSellRate,
						pricingRequestDTO.getForeignAmount()));

			}

			// discountedRatesNPrices.add(discountedRateDetail);

			// System.out.println(" Discounted Rates ==> " + discountedRateDetail);

		} // for (Bank...

		// return discountedRatesNPrices;
	}

	// iterate the response of discount
	public ExchangeRateDetails fetchCustomerChannelDiscounts(CustomerDiscountReqDTO customerDiscountReqDTO) {

		GroupingMaster curGroup = null;
		BigDecimal OnlineCountryBranchId = null;
		BigDecimal ccDiscountPips = BigDecimal.ZERO;
		BigDecimal amountSlabPips = BigDecimal.ZERO;
		BigDecimal channelDiscountPips = BigDecimal.ZERO;

		ExchangeRateDetails bankExRateDetail = new ExchangeRateDetails();
		ExchangeDiscountInfo custCategoryInfo = new ExchangeDiscountInfo();
		ExchangeDiscountInfo amountSlabPipsInfo = new ExchangeDiscountInfo();
		ExchangeDiscountInfo channelInfo = new ExchangeDiscountInfo();

		// Find the Currency Group to which the currency belongs
		CurrencyMasterModel currencyMasterModel = currencyMasterDao
				.getByCurrencyId(customerDiscountReqDTO.getForeignCurrencyId());

		if (currencyMasterModel != null) {
			if (currencyMasterModel.getCurrGroupId() != null) {
				curGroup = groupingMasterDao.getGroupById(currencyMasterModel.getCurrGroupId());
				if (curGroup == null) {
					LOGGER.warn(" ****** MAJOR : Currency Group is Null for Currency Group Id : "
							+ currencyMasterModel.getCurrGroupId() + " and Currency Id :"
							+ currencyMasterModel.getCurrencyId());
				}
			} else {
				LOGGER.warn(
						" ****** MAJOR : Currency Group is Null for Currency Id : " + currencyMasterModel.getCurrencyId());
			}
		}

		// Compute Channel Discount
		ChannelDiscount channelDiscount = channelDiscountDao.getDiscountByChannel(customerDiscountReqDTO.getChannel());

		if (channelDiscount != null && curGroup != null && DbValueUtil.isActive(channelDiscount.getIsActive())) {
			DiscountMaster channelDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
					DISCOUNT_TYPE.CHANNEL.getTypeKey(), channelDiscount.getId(), curGroup.getId());
			channelDiscountPips = ((null != channelDiscountMaster
					&& DbValueUtil.isActive(channelDiscountMaster.getIsActive()))
							? channelDiscountMaster.getDiscountPips()
							: BigDecimal.ZERO);
		}

		// Channel Info
		channelInfo.setId(channelDiscount.getId());
		channelInfo.setDiscountType(DISCOUNT_TYPE.CHANNEL);
		channelInfo.setDiscountTypeValue(customerDiscountReqDTO.getChannel().name());
		channelInfo.setDiscountPipsValue(channelDiscountPips);

		LOGGER.warn("channelInfo : " + JsonUtil.toJson(channelInfo));

		// Compute Customer category Discount
		CustomerDiscountsView customerDiscountsView = customerDiscountDao.fetchCustomerDiscount(
				customerDiscountReqDTO.getCustomerId(), DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), curGroup.getId());

		if (customerDiscountsView != null && customerDiscountsView.getDiscountType() != null && customerDiscountsView.getDiscountType()
				.equalsIgnoreCase(DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey())) {
			ccDiscountPips = (null != customerDiscountsView.getDiscountPips() ? customerDiscountsView.getDiscountPips()
					: BigDecimal.ZERO);
			// Customer Category Info
			custCategoryInfo.setId(customerDiscountsView.getDiscountTypeId());
			custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
			custCategoryInfo.setDiscountTypeValue(customerDiscountsView.getCustomerCategory().toString());
			custCategoryInfo.setDiscountPipsValue(ccDiscountPips);
		}

		/*
		 * CustomerExtended customerExtended =
		 * customerExtendedDao.getCustomerExtendedByCustomerId(customerDiscountReqDTO.
		 * getCustomerId()); CustomerCategoryDiscount ccDiscount = customerExtended !=
		 * null ? customerExtended.getCustomerCategoryDiscount() : null;
		 * 
		 * if (ccDiscount != null && ccDiscount.getId() != null && curGroup != null &&
		 * DbValueUtil.isActive(ccDiscount.getIsActive())) { DiscountMaster
		 * ccDiscountMaster =
		 * discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(DISCOUNT_TYPE.
		 * CUSTOMER_CATEGORY.getTypeKey(), ccDiscount.getId(), curGroup.getId());
		 * ccDiscountPips = ((null != ccDiscountMaster &&
		 * DbValueUtil.isActive(ccDiscountMaster.getIsActive())) ?
		 * ccDiscountMaster.getDiscountPips() : BigDecimal.ZERO); }
		 * 
		 * // Customer Category Info custCategoryInfo.setId(ccDiscount.getId());
		 * custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
		 * custCategoryInfo.setDiscountTypeValue(ccDiscount.getCustomerCategory().
		 * toString()); custCategoryInfo.setDiscountPipsValue(ccDiscountPips);
		 */

		LOGGER.warn("custCategoryInfo : " + JsonUtil.toJson(custCategoryInfo));

		if (OnlineCountryBranchId == null) {
			CountryBranch cb = countryBranchDao.getOnlineCountryBranch();
			OnlineCountryBranchId = cb.getCountryBranchId();
		}

		List<BigDecimal> validBankIds = new ArrayList<BigDecimal>();
		validBankIds.add(customerDiscountReqDTO.getBankId());

		List<PipsMaster> pipsList = pipsMasterDao.getPipsForFcCurAndBank(customerDiscountReqDTO.getForeignCurrencyId(),
				OnlineCountryBranchId, validBankIds);
		Map<Long, TreeMap<BigDecimal, PipsMaster>> bankAmountSlabDiscounts = new HashMap<Long, TreeMap<BigDecimal, PipsMaster>>();

		if (null != pipsList) {
			for (PipsMaster pipsMaster : pipsList) {
				if (bankAmountSlabDiscounts.containsKey(pipsMaster.getBankMaster().getBankId().longValue())) {
					bankAmountSlabDiscounts.get(pipsMaster.getBankMaster().getBankId().longValue())
							.put(pipsMaster.getToAmount(), pipsMaster);
				} else {
					TreeMap<BigDecimal, PipsMaster> slabPipsMap = new TreeMap<BigDecimal, PipsMaster>();
					slabPipsMap.put(pipsMaster.getToAmount(), pipsMaster);
					bankAmountSlabDiscounts.put(pipsMaster.getBankMaster().getBankId().longValue(), slabPipsMap);
				}
			}
		}

		if (bankAmountSlabDiscounts.containsKey(customerDiscountReqDTO.getBankId().longValue())) {
			TreeMap<BigDecimal, PipsMaster> pipsMap = bankAmountSlabDiscounts
					.get(customerDiscountReqDTO.getBankId().longValue());
			for (Entry<BigDecimal, PipsMaster> entry : pipsMap.entrySet()) {

				if (customerDiscountReqDTO.getForeignAmount().compareTo(entry.getKey()) <= 0) {
					amountSlabPips = entry.getValue().getPipsNo();

					amountSlabPipsInfo.setId(entry.getValue().getPipsMasterId());
					amountSlabPipsInfo.setDiscountType(DISCOUNT_TYPE.AMOUNT_SLAB);
					amountSlabPipsInfo.setDiscountTypeValue(entry.getValue().getFromAmount().toPlainString() + "-"
							+ entry.getValue().getToAmount().toPlainString());
					amountSlabPipsInfo.setDiscountPipsValue(amountSlabPips == null ? BigDecimal.ZERO : amountSlabPips);

					break;
				}
			}
		}

		LOGGER.warn("amountSlabPipsInfo : " + JsonUtil.toJson(amountSlabPipsInfo));

		bankExRateDetail.setBankId(customerDiscountReqDTO.getBankId());
		bankExRateDetail.setCustomerDiscountDetails(new HashMap<DISCOUNT_TYPE, ExchangeDiscountInfo>());
		bankExRateDetail.getCustomerDiscountDetails().put(DISCOUNT_TYPE.CHANNEL, channelInfo);
		bankExRateDetail.getCustomerDiscountDetails().put(DISCOUNT_TYPE.CUSTOMER_CATEGORY, custCategoryInfo);
		bankExRateDetail.getCustomerDiscountDetails().put(DISCOUNT_TYPE.AMOUNT_SLAB, amountSlabPipsInfo);

		return bankExRateDetail;
	}
}
