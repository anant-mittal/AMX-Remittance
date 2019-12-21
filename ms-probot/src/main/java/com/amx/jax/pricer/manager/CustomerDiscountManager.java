package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
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
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.util.DbValueUtil;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.utils.JsonUtil;

@Component
public class CustomerDiscountManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDiscountManager.class);

	/**
	 * Changed to 50% Tolerance : 17th Dec 2019.
	 */
	private static final BigDecimal BtrRateIndicatorMarginPercent = new BigDecimal(0.50).setScale(2,
			RoundingMode.HALF_EVEN);

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

	private static BigDecimal BIGD_ZERO = BigDecimal.ZERO;

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
					: custCatDiscountDao.getDiscountByCustomerCategory(CUSTOMER_CATEGORY.BRONZE);

			if (ccDiscount != null && ccDiscount.getId() != null && curGroup != null
					&& DbValueUtil.isActive(ccDiscount.getIsActive())) {

				DiscountMaster ccDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
						DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), ccDiscount.getId(), curGroup.getId());

				ccDiscountPips = ((null != ccDiscountMaster && DbValueUtil.isActive(ccDiscountMaster.getIsActive()))
						? ccDiscountMaster.getDiscountPips()
						: BigDecimal.ZERO);

				// Customer Category Info
				custCategoryInfo.setId(ccDiscount.getId());
				custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
				custCategoryInfo.setDiscountTypeValue(ccDiscount.getCustomerCategory().toString());
				custCategoryInfo.setDiscountPipsValue(ccDiscountPips);

				// Updated Customer Category
				exchRateAndRoutingTransientDataCache.setCustomerCategory(ccDiscount.getCustomerCategory());
			}
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
							.put(pipsMaster.getFromAmount(), pipsMaster);

				} else {

					TreeMap<BigDecimal, PipsMaster> slabPipsMap = new TreeMap<BigDecimal, PipsMaster>(
							Collections.reverseOrder());
					// New - Check Above From Range : 11/10/2019
					slabPipsMap.put(pipsMaster.getFromAmount(), pipsMaster);
					bankAmountSlabDiscounts.put(pipsMaster.getBankMaster().getBankId().longValue(), slabPipsMap);
				}

			}
		}

		for (ExchangeRateDetails bankExRateDetail : exchRateAndRoutingTransientDataCache.getSellRateDetails()) {

			BigDecimal amountSlabPips = BIGD_ZERO;
			ExchangeDiscountInfo amountSlabPipsInfo = new ExchangeDiscountInfo();

			ExchangeDiscountInfo nextSlabPipsInfo = new ExchangeDiscountInfo();

			if (bankAmountSlabDiscounts.containsKey(bankExRateDetail.getBankId().longValue())) {
				TreeMap<BigDecimal, PipsMaster> pipsMap = bankAmountSlabDiscounts
						.get(bankExRateDetail.getBankId().longValue());

				for (Entry<BigDecimal, PipsMaster> entry : pipsMap.entrySet()) {

					BigDecimal discountedFcAmount;

					if (pricingRequestDTO.getLocalAmount() != null) {

						BigDecimal tempAmtSlabPips = entry.getValue().getPipsNo() != null ? entry.getValue().getPipsNo()
								: BIGD_ZERO;

						BigDecimal totalDiscountPips = tempAmtSlabPips.add(channelDiscountPips).add(ccDiscountPips);

						BigDecimal estmdSellRate = bankExRateDetail.getSellRateBase().getInverseRate()
								.subtract(totalDiscountPips);

						// Get Bank Wise Rates for Local Currency
						ExchangeRateBreakup netBreakUp = RemitPriceManager.createBreakUpForLcCur(estmdSellRate,
								pricingRequestDTO.getLocalAmount());

						discountedFcAmount = netBreakUp.getConvertedFCAmount();

					} else {

						// Get Bank wise Rates for Foreign Currency
						discountedFcAmount = bankExRateDetail.getSellRateBase().getConvertedFCAmount();

					}

					// New Logic
					if (discountedFcAmount.compareTo(entry.getKey()) >= 0) {
						amountSlabPips = entry.getValue().getPipsNo();

						convertPipsMaster(amountSlabPipsInfo, entry.getValue());

						// IMP: Since the Tree Map is REVERSE SORTED
						Entry<BigDecimal, PipsMaster> nextEntry = pipsMap.lowerEntry(entry.getKey());

						if (nextEntry != null) {

							convertPipsMaster(nextSlabPipsInfo, nextEntry.getValue());

							// Check if Next Slab falls within the tolerance limit of the Current Base Fc
							// Amount
					
							/**
							 * Calculate the Required Bumped value as per the Discounted FC amount.
							 */
							BigDecimal bumpedFcVal = discountedFcAmount
									.add(discountedFcAmount.multiply(BtrRateIndicatorMarginPercent));

							if ((bumpedFcVal.compareTo(nextEntry.getValue().getFromAmount()) >= 0)
									&& amountSlabPips.compareTo(nextEntry.getValue().getPipsNo()) < 0) {

								bankExRateDetail.setBetterRateAvailable(true);
								bankExRateDetail.setBetterRateAmountSlab(nextEntry.getValue().getFromAmount());
							}
						}
						break;
					} // if

				} // for
			}

			/**
			 * Case where discount is already applied
			 **/
			// Avoid Double Discount Application
			// Shifted place for Next Amount Slab Calculations
			if (bankExRateDetail.isDiscountAvailed() == true) {
				if (bankExRateDetail.isBetterRateAvailable()) {
					if (bankExRateDetail.getSellRateNet() != null
							&& bankExRateDetail.getSellRateNet().getConvertedFCAmount() != null) {

						BigDecimal diffAmt = bankExRateDetail.getBetterRateAmountSlab()
								.subtract(bankExRateDetail.getSellRateNet().getConvertedFCAmount())
								.setScale(0, RoundingMode.UP);

						// TODO: Check for Corner case : where base FCamount is lower than the required
						// slab
						// amount and Net FC-Amount is higher than the Required Amount. The difference
						// is shown negative.

						if (diffAmt != null && diffAmt.compareTo(BIGD_ZERO) > 0) {
							bankExRateDetail.setDiffInBetterRateFcAmount(diffAmt);
						} else {
							bankExRateDetail.setBetterRateAvailable(false);
							bankExRateDetail.setBetterRateAmountSlab(null);
						}

					} else {

						BigDecimal diffAmt = bankExRateDetail.getBetterRateAmountSlab()
								.subtract(bankExRateDetail.getSellRateBase().getConvertedFCAmount())
								.setScale(0, RoundingMode.UP);
						if (diffAmt != null && diffAmt.compareTo(BIGD_ZERO) > 0) {
							bankExRateDetail.setDiffInBetterRateFcAmount(diffAmt);
						} else {
							bankExRateDetail.setBetterRateAvailable(false);
							bankExRateDetail.setBetterRateAmountSlab(null);
						}
					}
				}
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
				bankExRateDetail.setDiffInBetterRateFcAmount(null);

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
					bankExRateDetail.setDiffInBetterRateFcAmount(null);

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

			// Set the better Rate diff - Round to Next Int Val

			if (bankExRateDetail.isBetterRateAvailable()) {
				// TODO: Check for Corner case : where base FCamount is lower than the required
				// slab
				// amount and Net FC-Amount is higher than the Required Amount. The difference
				// is shown negative.

				BigDecimal diffAmt = bankExRateDetail.getBetterRateAmountSlab()
						.subtract(bankExRateDetail.getSellRateNet().getConvertedFCAmount())
						.setScale(0, RoundingMode.UP);
				// Corrected Logic - 11/10/2019
				bankExRateDetail.setDiffInBetterRateFcAmount(diffAmt);
				
			}
			
		} // for (Bank...

		// return discountedRatesNPrices;
	}

	private void convertPipsMaster(ExchangeDiscountInfo amountSlabPipsInfo, PipsMaster master) {
		amountSlabPipsInfo.setId(master.getPipsMasterId());
		amountSlabPipsInfo.setDiscountType(DISCOUNT_TYPE.AMOUNT_SLAB);
		amountSlabPipsInfo
				.setDiscountTypeValue(master.getFromAmount().longValue() + "-" + master.getToAmount().longValue());
		amountSlabPipsInfo.setDiscountPipsValue(master.getPipsNo());
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
				LOGGER.warn(" ****** MAJOR : Currency Group is Null for Currency Id : "
						+ currencyMasterModel.getCurrencyId());
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

		if (customerDiscountsView != null && customerDiscountsView.getDiscountType() != null && customerDiscountsView
				.getDiscountType().equalsIgnoreCase(DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey())) {
			ccDiscountPips = (null != customerDiscountsView.getDiscountPips() ? customerDiscountsView.getDiscountPips()
					: BigDecimal.ZERO);
			// Customer Category Info
			custCategoryInfo.setId(customerDiscountsView.getDiscountTypeId());
			custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
			custCategoryInfo.setDiscountTypeValue(customerDiscountsView.getCustomerCategory().toString());
			custCategoryInfo.setDiscountPipsValue(ccDiscountPips);
		}
		

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
