package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.ComputeRequestTransientDataCache;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.CustomerExtendedDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.CustomerExtended;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
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

	@Resource
	ComputeRequestTransientDataCache computeRequestTransientDataCache;

	private static BigDecimal PIPS_BANK_ID = new BigDecimal(78);

	private static BigDecimal BIGD_ZERO = new BigDecimal(0);

	public void getDiscountedRates(PricingRequestDTO pricingRequestDTO, Customer customer,
			CUSTOMER_CATEGORY customerCategory) {

		// Compute Channel Discount
		ChannelDiscount channelDiscount = channelDiscountDao.getDiscountByChannel(pricingRequestDTO.getChannel());
		BigDecimal channelDiscountPips = (null != channelDiscount ? channelDiscount.getDiscountPips() : BIGD_ZERO);

		// Compute Customer category Discount
		BigDecimal ccDiscountPips;
		if (customer != null) {
			CustomerExtended customerExtended = customerExtendedDao
					.getCustomerExtendedByCustomerId(customer.getCustomerId());

			CustomerCategoryDiscount ccDiscount = customerExtended.getCustomerCategoryDiscount();

			ccDiscountPips = (null != ccDiscount ? ccDiscount.getDiscountPips() : BIGD_ZERO);
		} else {
			CustomerCategoryDiscount ccDiscount = custCatDiscountDao
					.getDiscountByCustomerCategory(customerCategory.name());
			ccDiscountPips = (null != ccDiscount ? ccDiscount.getDiscountPips() : BIGD_ZERO);
		}

		List<BigDecimal> validBankIds = new ArrayList<BigDecimal>(computeRequestTransientDataCache.getBankDetails().keySet());

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

		BigDecimal margin = computeRequestTransientDataCache.getMargin() != null
				? computeRequestTransientDataCache.getMargin().getMarginMarkup()
				: BIGD_ZERO;

		for (ExchangeRateDetails bankExRateDetail : computeRequestTransientDataCache.getSellRateDetails()) {

			BigDecimal amountSlabPips = BIGD_ZERO;

			if (bankAmountSlabDiscounts.containsKey(bankExRateDetail.getBankId().longValue())) {
				TreeMap<BigDecimal, PipsMaster> pipsMap = bankAmountSlabDiscounts
						.get(bankExRateDetail.getBankId().longValue());
				for (Entry<BigDecimal, PipsMaster> entry : pipsMap.entrySet()) {

					if (bankExRateDetail.getSellRateBase().getConvertedFCAmount().compareTo(entry.getKey()) <= 0) {
						amountSlabPips = entry.getValue().getPipsNo();
						break;
					} // if

				} // for
			}

			BigDecimal totalDiscountPips = amountSlabPips.add(channelDiscountPips).add(ccDiscountPips);

			BigDecimal discountedSellRate = bankExRateDetail.getSellRateBase().getInverseRate()
					.subtract(totalDiscountPips);

			/**
			 * Compute Base Sell rate : Cost + Margin
			 */
			BigDecimal adjustedBaseSellRate = BIGD_ZERO;

			if (computeRequestTransientDataCache.getAvgRateGLCForBank(bankExRateDetail.getBankId()) != null) {

				// Old Logic
				// ViewExGLCBAL viewExGLCBAL =
				// computeRequestTransientDataCache.getBankGlcBalMap().get(bankExRateDetail.getBankId());

				// adjustedBaseSellRate = viewExGLCBAL.getRateAvgRate().add(margin);

				// New Logic
				adjustedBaseSellRate = computeRequestTransientDataCache.getAvgRateGLCForBank(bankExRateDetail.getBankId())
						.add(margin);

			}

			if (discountedSellRate.compareTo(adjustedBaseSellRate) < 0) {
				discountedSellRate = adjustedBaseSellRate;
			}

			bankExRateDetail.setDiscountPipsDetails(new HashMap<DISCOUNT_TYPE, String>());

			bankExRateDetail.getDiscountPipsDetails().put(DISCOUNT_TYPE.CHANNEL, channelDiscountPips.toPlainString());
			bankExRateDetail.getDiscountPipsDetails().put(DISCOUNT_TYPE.CUSTOMER_CATEGORY,
					ccDiscountPips.toPlainString());
			bankExRateDetail.getDiscountPipsDetails().put(DISCOUNT_TYPE.AMOUNT_SLAB, amountSlabPips.toPlainString());

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

	@SuppressWarnings("unused")
	private void saveNewDiscountType() {

		ChannelDiscount channelDiscountPipsNew = new ChannelDiscount();

		channelDiscountPipsNew.setChannel(Channel.BRANCH);
		channelDiscountPipsNew.setDiscountPips(new BigDecimal(0.000005));
		channelDiscountPipsNew.setIsActive("Y");
		channelDiscountPipsNew.setInfo("{}");
		channelDiscountPipsNew.setFlags(new BigDecimal(0));

		channelDiscountPipsNew.setCreatedBy("Kanmani");
		channelDiscountPipsNew.setCreatedDate(new Date());

		channelDiscountPipsNew.setModifiedBy("Kanmani");
		channelDiscountPipsNew.setModifiedDate(new Date());

		channelDiscountPipsNew.setApprovedBy("Kanmani");
		channelDiscountPipsNew.setApprovedDate(new Date());

		CustomerCategoryDiscount ccDiscountNew = new CustomerCategoryDiscount();

		ccDiscountNew.setCustomerCategory("PLATINUM");
		ccDiscountNew.setDiscountPips(new BigDecimal(0.000005));
		ccDiscountNew.setIsActive("Y");
		ccDiscountNew.setInfo("{}");
		ccDiscountNew.setFlags(new BigDecimal(0));

		ccDiscountNew.setCreatedBy("Kanmani");
		ccDiscountNew.setCreatedDate(new Date());

		ccDiscountNew.setModifiedBy("Kanmani");
		ccDiscountNew.setModifiedDate(new Date());

		ccDiscountNew.setApprovedBy("Kanmani");
		ccDiscountNew.setApprovedDate(new Date());

		channelDiscountDao.saveDiscountForChannel(channelDiscountPipsNew);

		custCatDiscountDao.saveDiscountForCustomerCategory(ccDiscountNew);

	}

}
