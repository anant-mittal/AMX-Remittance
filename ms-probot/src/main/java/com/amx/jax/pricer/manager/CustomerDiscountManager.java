package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.CustomerExtendedDao;
import com.amx.jax.pricer.dao.DiscountMasterDao;
import com.amx.jax.pricer.dao.GroupingMasterDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.CustomerExtended;
import com.amx.jax.pricer.dbmodel.DiscountMaster;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.util.PricingRateDetailsDTO;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

@Component
public class CustomerDiscountManager {

	@Autowired
	PipsMasterDao pipsMasterDao;

	@Autowired
	CustCatDiscountDao custCatDiscountDao;

	@Autowired
	ChannelDiscountDao channelDiscountDao;

	@Autowired
	CustomerExtendedDao customerExtendedDao;

	@Autowired
	CurrencyMasterDao currencyMasterDao;

	@Autowired
	GroupingMasterDao groupingMasterDao;

	@Autowired
	DiscountMasterDao discountMasterDao;

	@Resource
	PricingRateDetailsDTO pricingRateDetailsDTO;

	private static BigDecimal PIPS_BANK_ID = new BigDecimal(78);

	private static BigDecimal BIGD_ZERO = new BigDecimal(0);

	public void getDiscountedRates(PricingRequestDTO pricingRequestDTO, Customer customer,
			CUSTOMER_CATEGORY customerCategory) {

		// Find the Currency Group to which the currency belongs
		// TODO: Optimize this to Save it in the Cache and retrieve it from thr
		CurrencyMasterModel currencyMasterModel = currencyMasterDao
				.getByCurrencyId(pricingRequestDTO.getForeignCurrencyId());

		if (currencyMasterModel.getCurrGroupId() == null) {
			// TODO Throw Error
		}

		GroupingMaster curGroup = groupingMasterDao.getGroupById(currencyMasterModel.getCurrGroupId());

		if (curGroup == null) {
			// TODO : throw error
		}

		// Compute Channel Discount
		ChannelDiscount channelDiscount = channelDiscountDao.getDiscountByChannel(pricingRequestDTO.getChannel());

		DiscountMaster channelDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
				DISCOUNT_TYPE.CHANNEL.getTypeKey(), channelDiscount.getId(), curGroup.getId());

		BigDecimal channelDiscountPips = (null != channelDiscount ? channelDiscountMaster.getDiscountPips()
				: BIGD_ZERO);

		// Channel Info
		ExchangeDiscountInfo channelInfo = new ExchangeDiscountInfo();
		channelInfo.setId(channelDiscount.getId());
		channelInfo.setDiscountType(DISCOUNT_TYPE.CHANNEL);
		channelInfo.setDiscountTypeValue(pricingRequestDTO.getChannel().name());
		channelInfo.setDiscountPipsValue(channelDiscountPips);

		// Compute Customer category Discount
		BigDecimal ccDiscountPips;
		ExchangeDiscountInfo custCategoryInfo = new ExchangeDiscountInfo();

		if (customer != null) {
			CustomerExtended customerExtended = customerExtendedDao
					.getCustomerExtendedByCustomerId(customer.getCustomerId());

			CustomerCategoryDiscount ccDiscount = customerExtended.getCustomerCategoryDiscount();

			DiscountMaster ccDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
					DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), ccDiscount.getId(), curGroup.getId());

			ccDiscountPips = (null != ccDiscount ? ccDiscountMaster.getDiscountPips() : BIGD_ZERO);

			// Customer Category Info
			custCategoryInfo.setId(ccDiscount.getId());
			custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
			custCategoryInfo.setDiscountTypeValue(ccDiscount.getCustomerCategory().toString());
			custCategoryInfo.setDiscountPipsValue(ccDiscountPips);

			// TODO: Dirty Code --- REMOVE ---
			customer.setRemarks(ccDiscount.getCustomerCategory().toString());

		} else {
			CustomerCategoryDiscount ccDiscount = custCatDiscountDao.getDiscountByCustomerCategory(customerCategory);

			DiscountMaster ccDiscountMaster = discountMasterDao.getByDiscountTypeAndDiscountTypeIdAndGroupId(
					DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), ccDiscount.getId(), curGroup.getId());

			ccDiscountPips = (null != ccDiscount ? ccDiscountMaster.getDiscountPips() : BIGD_ZERO);

			custCategoryInfo.setId(ccDiscount.getId());
			custCategoryInfo.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
			custCategoryInfo.setDiscountTypeValue(ccDiscount.getCustomerCategory().toString());
			custCategoryInfo.setDiscountPipsValue(ccDiscountPips);

		}

		List<BigDecimal> validBankIds = new ArrayList<BigDecimal>(pricingRateDetailsDTO.getBankDetails().keySet());

		List<PipsMaster> pipsList = pipsMasterDao.getPipsForFcCurAndBank(pricingRequestDTO.getForeignCurrencyId(),
				PIPS_BANK_ID, pricingRequestDTO.getForeignCountryId(), validBankIds);

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

				// System.out.println(" Pips Master Discount ==> "
				// +
				// bankAmountSlabDiscounts.get(pipsMaster.getBankMaster().getBankId().longValue()));

			}
		}

		// List<BankRateDetailsDTO> discountedRatesNPrices = new
		// ArrayList<BankRateDetailsDTO>();

		BigDecimal margin = pricingRateDetailsDTO.getMargin() != null
				? pricingRateDetailsDTO.getMargin().getMarginMarkup()
				: BIGD_ZERO;

		for (ExchangeRateDetails bankExRateDetail : pricingRateDetailsDTO.getSellRateDetails()) {

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

						break;
					} // if

				} // for
			}

			BigDecimal discountedSellRate;

			if (bankExRateDetail.isCostRateLimitReached()) {
				// No further Discounts If the Cost+Margin Limit is Reached at Base Sell Rate
				// Level.
				discountedSellRate = bankExRateDetail.getSellRateBase().getInverseRate();
				bankExRateDetail.setDiscountAvailed(false);

			} else {

				BigDecimal totalDiscountPips = amountSlabPips.add(channelDiscountPips).add(ccDiscountPips);

				discountedSellRate = bankExRateDetail.getSellRateBase().getInverseRate().subtract(totalDiscountPips);

				bankExRateDetail.setDiscountAvailed(true);

				/**
				 * Compute Base Sell rate : Cost + Margin
				 */
				BigDecimal adjustedBaseSellRate = BIGD_ZERO;

				if (pricingRateDetailsDTO.getAvgRateGLCForBank(bankExRateDetail.getBankId()) != null) {

					// New Logic
					adjustedBaseSellRate = pricingRateDetailsDTO.getAvgRateGLCForBank(bankExRateDetail.getBankId())
							.add(margin);

				}

				if (discountedSellRate.compareTo(adjustedBaseSellRate) <= 0) {

					discountedSellRate = adjustedBaseSellRate;

					bankExRateDetail.setDiscountAvailed(true);
					bankExRateDetail.setCostRateLimitReached(true);

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

}
