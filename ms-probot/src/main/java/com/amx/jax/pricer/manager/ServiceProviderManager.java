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
import org.springframework.stereotype.Service;

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

@Service
public class ServiceProviderManager {

	@Autowired
	PartnerExchDataService partnerDataService;

	@Autowired
	BankMasterDao bankMasterDao;

	@Resource
	ExchRateAndRoutingTransientDataCache transientDataCache;

	@Async(ExecutorConfig.EXECUTER_PRICER)
	public Future<SrvPrvFeeInqResDTO> getServiceProviderQuote(ViewExRoutingMatrix homeSendMatrix,
			ExchangeRateAndRoutingRequest request) {

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

		SrvPrvFeeInqResDTO partnerResp = partnerDataService.getPartnerFeeinquiry(partnerReq);

		return CompletableFuture.completedFuture(partnerResp);

	}

	public void processServiceProviderData(ViewExRoutingMatrix homeSendMatrix, SrvPrvFeeInqResDTO partnerResp) {

		ExchangeRateBreakup sellRateBase = new ExchangeRateBreakup();

		sellRateBase.setInverseRate(partnerResp.getExchangeRateWithPips());

		if (partnerResp.getExchangeRateWithPips() != null
				&& partnerResp.getExchangeRateWithPips().compareTo(BigDecimal.ZERO) != 0) {

			sellRateBase.setRate(BigDecimal.ONE.divide(partnerResp.getExchangeRateWithPips(), DEF_CONTEXT));
		} else {

			sellRateBase.setRate(BigDecimal.ZERO);
		}

		sellRateBase.setConvertedLCAmount(partnerResp.getGrossAmount());
		sellRateBase.setConvertedFCAmount(partnerResp.getForeignAmount());

		ExchangeRateDetails exchRateDetails = new ExchangeRateDetails();
		exchRateDetails.setBankId(homeSendMatrix.getRoutingBankId());

		exchRateDetails.setCostRateLimitReached(false);

		// TODO : Check
		exchRateDetails.setDiscountAvailed(true);
		exchRateDetails.setLowGLBalance(false);
		exchRateDetails.setSellRateBase(sellRateBase);
		// exchRateDetails.setSellRateNet(sellRateBase);
		exchRateDetails.setServiceIndicatorId(homeSendMatrix.getServiceMasterId());

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
