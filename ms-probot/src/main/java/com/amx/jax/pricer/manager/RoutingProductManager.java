package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.DeliveryModeDescDao;
import com.amx.jax.pricer.dao.RoutingProdStatusDao;
import com.amx.jax.pricer.dao.TreasuryFTImpactDao;
import com.amx.jax.pricer.dao.ViewExGLCBALDao;
import com.amx.jax.pricer.dao.VwExGLCBalProvDao;
import com.amx.jax.pricer.dao.VwGlcbalProvProductDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.DeliveryModeDesc;
import com.amx.jax.pricer.dbmodel.TreasuryFundTimeImpact;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dbmodel.ViewExGLCBalProvisional;
import com.amx.jax.pricer.dbmodel.VwExGlcbalProvByProduct;
import com.amx.jax.pricer.dbmodel.VwExRoutingProductStatus;
import com.amx.jax.pricer.dto.DeliveryModeStatusInfo;
import com.amx.jax.pricer.dto.RemitModeStatusInfo;
import com.amx.jax.pricer.dto.RoutingProductStatusDetails;
import com.amx.jax.pricer.dto.RoutingProductStatusInfo;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.pricer.var.PricerServiceConstants.TREASURY_FUND_STATUS;
import com.amx.utils.ArgUtil;

@Component
public class RoutingProductManager {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RoutingProductManager.class);

	@Autowired
	private RoutingProdStatusDao routingProdStatusDao;

	@Autowired
	private ViewExGLCBALDao glcbalDao;

	@Autowired
	private VwExGLCBalProvDao vwGLCBalProvDao;

	@Autowired
	private BankMasterDao bankMasterDao;

	@Autowired
	private VwGlcbalProvProductDao vwGlcbalProvProductDao;

	@Autowired
	TreasuryFTImpactDao treasuryFTImpactDao;

	@Autowired
	CurrencyMasterDao currencyMasterDao;

	@Autowired
	DeliveryModeDescDao deliveryModeDescDao;

	public RoutingProductStatusDetails getRoutingProductStatus(BigDecimal beneCountryId, BigDecimal currencyId) {

		// Get valid Routing Product Status
		List<VwExRoutingProductStatus> viewRoutingProdsStatus = routingProdStatusDao
				.getByCurrencyIdAndDestinationCountryId(currencyId, beneCountryId);

		if (viewRoutingProdsStatus == null || viewRoutingProdsStatus.isEmpty()) {
			// throw Error
			LOGGER.error("No Routing Products available for countryId:" + beneCountryId + " currencyId: " + currencyId);

			throw new PricerServiceException(PricerServiceError.INVALID_REMIT_MODE_STATUS,
					"No Routing Products available for countryId:" + beneCountryId + " currencyId: " + currencyId);

		}

		List<BigDecimal> correspondentIds = viewRoutingProdsStatus.stream().map(h -> h.getBankId()).distinct()
				.collect(Collectors.toList());

		// Get Bank Details
		Map<BigDecimal, BankMasterModel> bankMasters = bankMasterDao.getBankByIdIn(correspondentIds);

		Map<BigDecimal, Map<BigDecimal, Map<BigDecimal, VwExRoutingProductStatus>>> bankRemitDeliveryModeProdStatus = new HashMap<BigDecimal, Map<BigDecimal, Map<BigDecimal, VwExRoutingProductStatus>>>();

		for (VwExRoutingProductStatus productStatus : viewRoutingProdsStatus) {
			if (!bankRemitDeliveryModeProdStatus.containsKey(productStatus.getBankId())) {
				Map<BigDecimal, VwExRoutingProductStatus> delModeProdStatusMap = new HashMap<BigDecimal, VwExRoutingProductStatus>();
				delModeProdStatusMap.put(productStatus.getDeliveryModeId(), productStatus);

				Map<BigDecimal, Map<BigDecimal, VwExRoutingProductStatus>> remitModeProdStatusMap = new HashMap<BigDecimal, Map<BigDecimal, VwExRoutingProductStatus>>();

				remitModeProdStatusMap.put(productStatus.getRemitModeId(), delModeProdStatusMap);
				bankRemitDeliveryModeProdStatus.put(productStatus.getBankId(), remitModeProdStatusMap);
			} else {
				Map<BigDecimal, Map<BigDecimal, VwExRoutingProductStatus>> remitModeProdStatusMap = bankRemitDeliveryModeProdStatus
						.get(productStatus.getBankId());

				if (remitModeProdStatusMap.containsKey(productStatus.getRemitModeId())) {

					Map<BigDecimal, VwExRoutingProductStatus> delModeProdStatusMap = remitModeProdStatusMap
							.get(productStatus.getRemitModeId());

					if (delModeProdStatusMap.containsKey(productStatus.getDeliveryModeId())) {
						// throw Exception of Duplicate Remit Mode Product

						LOGGER.error("Duplicate Remit Products for countryId:" + beneCountryId + " currencyId: "
								+ currencyId + " and correspondent bank Id#Name:" + productStatus.getBankId() + "#"
								+ productStatus.getBankName());

						/*
						 * * throw new
						 * PricerServiceException(PricerServiceError.DUPLICATE_REMIT_MODE_STATUS,
						 * "No Routing Products available for countryId:" + countryId + " currencyId: "
						 * + currencyId + " and correspondents:" + JsonUtil.toJson(correspondentIds));
						 */
					}

					delModeProdStatusMap.put(productStatus.getDeliveryModeId(), productStatus);

				} else {
					Map<BigDecimal, VwExRoutingProductStatus> delModeProdStatusMap = new HashMap<BigDecimal, VwExRoutingProductStatus>();
					delModeProdStatusMap.put(productStatus.getDeliveryModeId(), productStatus);

					remitModeProdStatusMap.put(productStatus.getRemitModeId(), delModeProdStatusMap);

				}

			}
		}

		// Get provisional Balance
		Map<BigDecimal, ViewExGLCBalProvisional> gLCBalProvisionalMap = vwGLCBalProvDao
				.getByCurrencyIdAndBankIdIn(currencyId, correspondentIds);

		String curCode = currencyMasterDao.getByCurrencyId(currencyId).getCurrencyCode();

		// Get Valid GLC Balances
		List<ViewExGLCBAL> glcBals = glcbalDao.getGLCBALforCurrencyAndBanks(curCode, correspondentIds);

		Map<BigDecimal, ViewExGLCBAL> glcBalMap = new HashMap<BigDecimal, ViewExGLCBAL>();

		if (glcBals != null && !glcBals.isEmpty()) {

			for (ViewExGLCBAL g : glcBals) {
				if (glcBalMap.containsKey(g.getBankId())) {
					ViewExGLCBAL old = glcBalMap.get(g.getBankId());

					g.setRateCurBal(ArgUtil.assignDefaultIfNull(g.getRateCurBal(), BigDecimal.ZERO)
							.add(ArgUtil.assignDefaultIfNull(old.getRateCurBal(), BigDecimal.ZERO)));

					g.setRateFcCurBal(ArgUtil.assignDefaultIfNull(g.getRateFcCurBal(), BigDecimal.ZERO)
							.add(ArgUtil.assignDefaultIfNull(old.getRateFcCurBal(), BigDecimal.ZERO)));
				} else {
					glcBalMap.put(g.getBankId(), g);
				}
			}

		}

		// Get Provisional Balances Product Wise
		List<VwExGlcbalProvByProduct> glcBalProvByProductList = vwGlcbalProvProductDao
				.getByCurrencyIdAndBankIdIn(currencyId, correspondentIds);

		Map<BigDecimal, Map<BigDecimal, VwExGlcbalProvByProduct>> glcBalProvProductMap = new HashMap<BigDecimal, Map<BigDecimal, VwExGlcbalProvByProduct>>();

		if (glcBalProvByProductList != null) {
			for (VwExGlcbalProvByProduct glcbalProduct : glcBalProvByProductList) {
				if (!glcBalProvProductMap.containsKey(glcbalProduct.getBankId())) {
					Map<BigDecimal, VwExGlcbalProvByProduct> productBalMap = new HashMap<BigDecimal, VwExGlcbalProvByProduct>();
					productBalMap.put(glcbalProduct.getRemittanceModeId(), glcbalProduct);

					glcBalProvProductMap.put(glcbalProduct.getBankId(), productBalMap);
				} else {
					glcBalProvProductMap.get(glcbalProduct.getBankId()).put(glcbalProduct.getRemittanceModeId(),
							glcbalProduct);

				}
			}
		}
		// Funding Currency and balance Details
		TreasuryFundTimeImpact ftImpact = treasuryFTImpactDao.findByCountryIdAndCurrencyIdAndFundStatus(beneCountryId,
				currencyId, TREASURY_FUND_STATUS.FUNDED.toString());

		Map<BigDecimal, ViewExGLCBAL> fundingGlBalMap = new HashMap<BigDecimal, ViewExGLCBAL>();
		CurrencyMasterModel fundCur = null;

		if (ftImpact != null && ftImpact.getFundingCurId() != null) {
			fundCur = currencyMasterDao.getByCurrencyId(ftImpact.getFundingCurId());
			String fundCurCode = fundCur.getCurrencyCode();

			List<ViewExGLCBAL> fundBals = glcbalDao.getGLCBALforCurrencyAndBanks(fundCurCode, correspondentIds);

			if (fundBals != null && !fundBals.isEmpty()) {
				fundingGlBalMap = fundBals.stream().collect(Collectors.toMap(f -> f.getBankId(), f -> f));
			}

		}

		Map<BigDecimal, DeliveryModeDesc> delModeDescMap = deliveryModeDescDao
				.getByLanguageId(PricerServiceConstants.DEF_LANGUAGE_ID);

		RoutingProductStatusDetails resp = new RoutingProductStatusDetails();
		resp.setRoutingProductsStatus(new ArrayList<RoutingProductStatusInfo>());

		Set<BigDecimal> processedCb = new HashSet<BigDecimal>();

		for (BigDecimal correspondentId : correspondentIds) {

			if (processedCb.contains(correspondentId)) {
				continue;
			}

			processedCb.add(correspondentId);

			RoutingProductStatusInfo info = new RoutingProductStatusInfo();

			info.setCorrespondentId(correspondentId);

			if (bankMasters.containsKey(correspondentId)) {
				info.setCorrespondentName(bankMasters.get(correspondentId).getBankShortName());
			}

			if (glcBalMap.containsKey(correspondentId)) {
				ViewExGLCBAL glcbal = glcBalMap.get(correspondentId);
				info.setGlcbalLocal(ArgUtil.assignDefaultIfNull(glcbal.getRateCurBal(), BigDecimal.ZERO));
				info.setGlcbalForeign(ArgUtil.assignDefaultIfNull(glcbal.getRateFcCurBal(), BigDecimal.ZERO));
			}

			if (gLCBalProvisionalMap.containsKey(correspondentId)) {
				ViewExGLCBalProvisional provisional = gLCBalProvisionalMap.get(correspondentId);
				info.setProvisionalTrnxAmountLocal(
						ArgUtil.assignDefaultIfNull(provisional.getRateCurBal(), BigDecimal.ZERO));
				info.setProvisionalTrnxAmountForeign(
						ArgUtil.assignDefaultIfNull(provisional.getRateFcCurBal(), BigDecimal.ZERO));
			}

			if (info.getGlcbalLocal() != null || info.getProvisionalTrnxAmountLocal() != null
					|| info.getGlcbalForeign() != null || info.getProvisionalTrnxAmountForeign() != null) {

				BigDecimal netLocal = (info.getGlcbalLocal() == null ? BigDecimal.ZERO : info.getGlcbalLocal())
						.add(info.getProvisionalTrnxAmountLocal() == null ? BigDecimal.ZERO
								: info.getProvisionalTrnxAmountLocal());

				BigDecimal netForeign = (info.getGlcbalForeign() == null ? BigDecimal.ZERO : info.getGlcbalForeign())
						.add(info.getProvisionalTrnxAmountForeign() == null ? BigDecimal.ZERO
								: info.getProvisionalTrnxAmountForeign());

				info.setNetBalanceLocal(netLocal);
				info.setNetBalanceForeign(netForeign);
			}

			if (fundCur != null) {
				info.setFundingCurrencyId(fundCur.getCurrencyId());
				info.setFundingCurrencyQuote(fundCur.getQuoteName());

				if (fundingGlBalMap.containsKey(correspondentId)) {
					info.setFundingCurrencyBalLocal(fundingGlBalMap.get(correspondentId).getRateCurBal());
					info.setFundingCurrencyBalForeign(fundingGlBalMap.get(correspondentId).getRateFcCurBal());
				}
			}

			// Prepare Remit Mode Product Info List
			List<RemitModeStatusInfo> remitModesStatus = new ArrayList<RemitModeStatusInfo>();

			if (bankRemitDeliveryModeProdStatus.containsKey(correspondentId)) {
				Map<BigDecimal, Map<BigDecimal, VwExRoutingProductStatus>> remitModeProdStatusMap = bankRemitDeliveryModeProdStatus
						.get(correspondentId);

				Map<BigDecimal, VwExGlcbalProvByProduct> productBalMap;
				if (glcBalProvProductMap.containsKey(correspondentId)) {
					productBalMap = glcBalProvProductMap.get(correspondentId);
				} else {
					productBalMap = new HashMap<BigDecimal, VwExGlcbalProvByProduct>();
				}

				for (Map<BigDecimal, VwExRoutingProductStatus> delModeStatusMap : remitModeProdStatusMap.values()) {

					// Single Delivery Mode
					if (delModeStatusMap.size() == 1) {

						VwExRoutingProductStatus prodStatus = delModeStatusMap.values().iterator().next();

						RemitModeStatusInfo remitModeInfo = new RemitModeStatusInfo();

						remitModeInfo.setRemitModeId(prodStatus.getRemitModeId());
						remitModeInfo.setRemitModeDesc(prodStatus.getProductShortName());
						remitModeInfo.setRoutingStatus(prodStatus.getRouting());
						remitModeInfo.setProductStatus(prodStatus.getProductStatus());

						VwExGlcbalProvByProduct provBal = productBalMap.get(prodStatus.getRemitModeId());
						if (provBal != null) {
							remitModeInfo.setProvisionalTrnxAmountLocal(
									provBal.getRateCurBal() == null ? BigDecimal.ZERO : provBal.getRateCurBal());
							remitModeInfo.setProvisionalTrnxAmountForeign(
									provBal.getRateFcCurBal() == null ? BigDecimal.ZERO : provBal.getRateFcCurBal());
						}

						remitModesStatus.add(remitModeInfo);
					} else { // else

						boolean remitStatusFilled = false;
						RemitModeStatusInfo remitModeInfo = new RemitModeStatusInfo();
						remitModeInfo.setDeliveryModesStatus(new ArrayList<DeliveryModeStatusInfo>());

						for (VwExRoutingProductStatus prodStatus : delModeStatusMap.values()) {// 1 for
							if (!remitStatusFilled) { // 1.1
								remitModeInfo.setRemitModeId(prodStatus.getRemitModeId());
								remitModeInfo.setRemitModeDesc(prodStatus.getProductShortName());
								// remitModeInfo.setRoutingStatus(prodStatus.getRouting());
								// remitModeInfo.setProductStatus(prodStatus.getProductStatus());

								VwExGlcbalProvByProduct provBal = productBalMap.get(prodStatus.getRemitModeId());
								if (provBal != null) { // 1.1.1
									remitModeInfo.setProvisionalTrnxAmountLocal(
											provBal.getRateCurBal() == null ? BigDecimal.ZERO
													: provBal.getRateCurBal());
									remitModeInfo.setProvisionalTrnxAmountForeign(
											provBal.getRateFcCurBal() == null ? BigDecimal.ZERO
													: provBal.getRateFcCurBal());
								} // 1.1.1

							} // 1.1

							DeliveryModeStatusInfo delModeInfo = new DeliveryModeStatusInfo();
							delModeInfo.setDeliveryModeId(prodStatus.getDeliveryModeId());
							delModeInfo.setRoutingStatus(prodStatus.getRouting());
							delModeInfo.setProductStatus(prodStatus.getProductStatus());

							// Set delivery Mode Desc
							if (delModeDescMap.containsKey(prodStatus.getDeliveryModeId())) {
								delModeInfo.setDeliveryModeDesc(
										delModeDescMap.get(prodStatus.getDeliveryModeId()).getDeliveryDesc());
							}

							remitModeInfo.getDeliveryModesStatus().add(delModeInfo);
						} // 1 for

						remitModesStatus.add(remitModeInfo);

					} // else

				}

			}

			info.setRemitModesStatus(remitModesStatus);

			resp.getRoutingProductsStatus().add(info);
		}

		return resp;

	}

}
