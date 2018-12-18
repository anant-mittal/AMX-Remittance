package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dto.BankRateDetailsDTO;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.util.PricingRateDetailsDTO;
import com.amx.utils.JsonUtil;

@Component
public class CustomerDiscountManager {

	@Autowired
	PipsMasterDao pipsMasterDao;

	@Autowired
	CustCatDiscountDao custCatDiscountDao;

	@Autowired
	ChannelDiscountDao channelDiscountDao;

	@Resource
	PricingRateDetailsDTO pricingRateDetailsDTO;

	private static BigDecimal PIPS_BANK_ID = new BigDecimal(78);

	public List<BankRateDetailsDTO> getDiscountedRates(PricingReqDTO pricingReqDTO, List<BankRateDetailsDTO> bankRates,
			Channel channel, Customer customer) {

		ChannelDiscount channelDiscountPips = channelDiscountDao.getDiscountByChannel(channel);

		CustomerCategoryDiscount ccDiscount = custCatDiscountDao.getDiscountByCustomerCategory("PLATINUM");

		System.out.println(" Channel Discount ==>  " + JsonUtil.toJson(channelDiscountPips));

		System.out.println(" Customer cat Discount ==>  " + JsonUtil.toJson(ccDiscount));

		List<BigDecimal> validBankIds = new ArrayList<BigDecimal>();

		HashSet<Long> bankIdSet = new HashSet<Long>();

		for (BankRateDetailsDTO bankRate : bankRates) {
			if (!bankIdSet.contains(bankRate.getBankId().longValue())) {
				bankIdSet.add(bankRate.getBankId().longValue());
				validBankIds.add(bankRate.getBankId());
			}
		}

		List<PipsMaster> pipsList = pipsMasterDao.getPipsForFcCurAndBank(pricingReqDTO.getForeignCurrencyId(),
				PIPS_BANK_ID, pricingReqDTO.getForeignCountryId(), validBankIds);

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

				System.out.println(" Pips Master Discount ==> "
						+ bankAmountSlabDiscounts.get(pipsMaster.getBankMaster().getBankId().longValue()));

			}
		}

		List<BankRateDetailsDTO> discountedRatesNPrices = new ArrayList<BankRateDetailsDTO>();

		BigDecimal margin = pricingRateDetailsDTO.getMargin() != null
				? pricingRateDetailsDTO.getMargin().getMarginMarkup()
				: new BigDecimal(0);

		for (BankRateDetailsDTO bankRate : bankRates) {

			BigDecimal amountSlabPips = new BigDecimal(0);

			if (bankAmountSlabDiscounts.containsKey(bankRate.getBankId().longValue())) {
				TreeMap<BigDecimal, PipsMaster> pipsMap = bankAmountSlabDiscounts.get(bankRate.getBankId().longValue());
				for (Entry<BigDecimal, PipsMaster> entry : pipsMap.entrySet()) {

					System.out.println("For PIPS Iterations Bank ==> " + entry.getValue().getBankMaster().getBankCode()
							+ " pips No ==> " + entry.getKey());

					if (bankRate.getExRateBreakup().getConvertedFCAmount().compareTo(entry.getKey()) <= 0) {
						amountSlabPips = entry.getValue().getPipsNo();
						break;
					} // if

				} // for
			}

			BigDecimal totalDiscountPips = amountSlabPips;

			totalDiscountPips
					.add(null != channelDiscountPips ? channelDiscountPips.getDiscountPips() : new BigDecimal(0));

			totalDiscountPips.add(null != ccDiscount ? ccDiscount.getDiscountPips() : new BigDecimal(0));

			BigDecimal discountedSellRate = bankRate.getExRateBreakup().getInverseRate().subtract(totalDiscountPips);

			/**
			 * Compute Base Sell rate : Cost + Margin
			 */
			BigDecimal adjustedBaseSellRate = new BigDecimal(0);

			if (pricingRateDetailsDTO.getBankGlcBalMap() != null) {

				ViewExGLCBAL viewExGLCBAL = pricingRateDetailsDTO.getBankGlcBalMap().get(bankRate.getBankId());

				adjustedBaseSellRate = viewExGLCBAL.getRateAvgRate().add(margin);

			}

			if (discountedSellRate.compareTo(adjustedBaseSellRate) < 0) {
				discountedSellRate = adjustedBaseSellRate;
			}

			BankRateDetailsDTO discountedRateDetail = new BankRateDetailsDTO();

			try {
				BeanUtils.copyProperties(discountedRateDetail, bankRate);
			} catch (IllegalAccessException | InvocationTargetException e) {
				System.out.println("error in convert of bankmaster");
			}

			discountedRateDetail.setBankCode(" Discounted : " + discountedRateDetail.getBankCode());

			if (pricingReqDTO.getLocalAmount() != null) {

				// Get Bank Wise Rates for Local Currency
				discountedRateDetail.setExRateBreakup(
						RemitPriceManager.createBreakUpForLcCur(discountedSellRate, pricingReqDTO.getLocalAmount()));

			} else {

				// Get Bank wise Rates for Foreign Currency
				discountedRateDetail.setExRateBreakup(
						RemitPriceManager.createBreakUpForFcCur(discountedSellRate, pricingReqDTO.getLocalAmount()));

			}

			discountedRatesNPrices.add(discountedRateDetail);

			System.out.println(" Discounted Rates ==> " + discountedRateDetail);

		} // for (Bank...

		return discountedRatesNPrices;
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
