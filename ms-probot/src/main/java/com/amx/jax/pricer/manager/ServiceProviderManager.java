package com.amx.jax.pricer.manager;

//import static com.amx.jax.pricer.var.PricerServiceConstants.DEF_DECIMAL_SCALE;
import static com.amx.jax.pricer.var.PricerServiceConstants.DEF_CONTEXT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.cache.ExchRateAndRoutingTransientDataCache;
import com.amx.jax.multitenant.TenantContext;
import com.amx.jax.partner.dto.RoutingBankDetails;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.service.PartnerExchDataService;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;

//@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ServiceProviderManager {

	@Autowired
	PartnerExchDataService partnerDataService;

	@Autowired
	BankMasterDao bankMasterDao;

	@Resource
	ExchRateAndRoutingTransientDataCache transientDataCache;

	@Async(ExecutorConfig.EXECUTER_PRICER)
	public Future<SrvPrvFeeInqResDTO> getServiceProviderQuote(ViewExRoutingMatrix homeSendMatrix,
			ExchangeRateAndRoutingRequest request, CUSTOMER_CATEGORY custCat) {

		TenantContext.setCurrentTenant(AppContextUtil.getTenant().name());

		SrvPrvFeeInqReqDTO partnerReq = new SrvPrvFeeInqReqDTO();

		RoutingBankDetails routBankDetails = new RoutingBankDetails();
		routBankDetails.setRoutingBankId(homeSendMatrix.getRoutingBankId());
		routBankDetails.setRemittanceId(homeSendMatrix.getRemittanceModeId());
		routBankDetails.setDeliveryId(homeSendMatrix.getDeliveryModeId());

		List<RoutingBankDetails> routeList = new ArrayList<>();
		routeList.add(routBankDetails);

		partnerReq.setCustomerId(request.getCustomerId());

		partnerReq.setBeneficiaryRelationShipId(request.getBeneficiaryId());
		partnerReq.setApplicationCountryId(request.getLocalCountryId());
		partnerReq.setDestinationCountryId(request.getForeignCountryId());
		partnerReq.setLocalCurrencyId(request.getLocalCurrencyId());
		partnerReq.setForeignCurrencyId(request.getForeignCurrencyId());

		if (null != request.getLocalAmount()) {
			partnerReq.setSelectedCurrency(request.getLocalCurrencyId());
			partnerReq.setAmount(request.getLocalAmount());
		} else {
			partnerReq.setSelectedCurrency(request.getForeignCurrencyId());
			partnerReq.setAmount(request.getForeignAmount());
		}

		partnerReq.setRoutingBankDetails(routeList);

		// For Discounts
		partnerReq.setCustomerCategory(custCat);
		partnerReq.setChannel(request.getChannel());

		// Meta
		partnerReq.setEmployeeId(request.getEmployeeId());
		partnerReq.setCompanyId(request.getCompanyId());
		partnerReq.setCountryBranchId(request.getCountryBranchId());

		SrvPrvFeeInqResDTO partnerResp = partnerDataService.getPartnerFeeinquiry(partnerReq);

		return CompletableFuture.completedFuture(partnerResp);

	}

	public void processServiceProviderData(ViewExRoutingMatrix homeSendMatrix, SrvPrvFeeInqResDTO partnerResp) {

		ExchangeRateBreakup sellRateBase = new ExchangeRateBreakup();
		ExchangeRateBreakup sellRateNet = new ExchangeRateBreakup();

		sellRateBase.setInverseRate(partnerResp.getExchangeRateBase());
		sellRateNet.setInverseRate(partnerResp.getExchangeRateWithPips());

		// Base Rate
		if (partnerResp.getExchangeRateBase() != null
				&& partnerResp.getExchangeRateBase().compareTo(BigDecimal.ZERO) != 0) {

			sellRateBase.setRate(BigDecimal.ONE.divide(partnerResp.getExchangeRateBase(), DEF_CONTEXT));
		} else {

			sellRateBase.setRate(BigDecimal.ZERO);
		}

		// Net Rate
		if (partnerResp.getExchangeRateWithPips() != null
				&& partnerResp.getExchangeRateWithPips().compareTo(BigDecimal.ZERO) != 0) {

			sellRateNet.setRate(BigDecimal.ONE.divide(partnerResp.getExchangeRateWithPips(), DEF_CONTEXT));
		} else {

			sellRateNet.setRate(BigDecimal.ZERO);
		}

		sellRateBase.setConvertedLCAmount(partnerResp.getBaseLocalAmount());
		sellRateBase.setConvertedFCAmount(partnerResp.getBaseForeignAmount());
		
		sellRateNet.setConvertedLCAmount(partnerResp.getGrossAmount());
		sellRateNet.setConvertedFCAmount(partnerResp.getForeignAmount());

		ExchangeRateDetails exchRateDetails = new ExchangeRateDetails();
		exchRateDetails.setBankId(homeSendMatrix.getRoutingBankId());

		exchRateDetails.setCostRateLimitReached(false);
		exchRateDetails.setDiscountAvailed(true);
		exchRateDetails.setLowGLBalance(false);
		exchRateDetails.setFundedIntermediary(false);
		exchRateDetails.setSellRateBase(sellRateBase);
		exchRateDetails.setSellRateNet(sellRateNet);
		exchRateDetails.setServiceIndicatorId(homeSendMatrix.getServiceMasterId());
		
		// Set Discount Details
		exchRateDetails.setCustomerDiscountDetails(partnerResp.getCustomerDiscountDetails());

		if (null == transientDataCache.getSellRateDetails()) {
			List<ExchangeRateDetails> bankWiseRates = new ArrayList<ExchangeRateDetails>();
			transientDataCache.setSellRateDetails(bankWiseRates);
		}

		transientDataCache.getSellRateDetails().add(exchRateDetails);

		// Fill Bank Details
		BankMasterModel bankModel = bankMasterDao.getBankById(homeSendMatrix.getRoutingBankId());

		BankDetailsDTO bankDetailsDTO = new BankDetailsDTO();
		bankDetailsDTO.setBankCode(bankModel.getBankCode());
		bankDetailsDTO.setBankCountryId(bankModel.getBankCountryId());
		bankDetailsDTO.setBankFullName(bankModel.getBankFullName());
		bankDetailsDTO.setBankId(bankModel.getBankId());
		bankDetailsDTO.setBankShortName(bankModel.getBankShortName());

		if (null == transientDataCache.getBankDetails()) {
			Map<BigDecimal, BankDetailsDTO> bankIdDetailsMap = new HashMap<BigDecimal, BankDetailsDTO>();
			transientDataCache.setBankDetails(bankIdDetailsMap);
		}

		transientDataCache.getBankDetails().put(bankDetailsDTO.getBankId(), bankDetailsDTO);

	}

}
