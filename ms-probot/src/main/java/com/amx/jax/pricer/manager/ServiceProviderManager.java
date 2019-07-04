package com.amx.jax.pricer.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.AppContextUtil;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.multitenant.TenantContext;
import com.amx.jax.partner.dto.RoutingBankDetails;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.service.PartnerExchDataService;
import com.amx.utils.JsonUtil;

@Service
public class ServiceProviderManager {

	@Autowired
	PartnerExchDataService partnerDataService;

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

		System.out.println(" Partner data ==> " + JsonUtil.toJson(partnerResp));

		return CompletableFuture.completedFuture(partnerResp);

	}

}
