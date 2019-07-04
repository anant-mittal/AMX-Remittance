package com.amx.jax.pricer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.manager.PartnerDataManager;

@Service
//@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PartnerExchDataService {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PartnerExchDataService.class);

	@Autowired
	PartnerDataManager partnerDataManager;

	public SrvPrvFeeInqResDTO getPartnerFeeinquiry(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		
		SrvPrvFeeInqResDTO serviceProviderRespDTO = new SrvPrvFeeInqResDTO();

		partnerDataManager.validateGetQuotation(srvPrvFeeInqReqDTO);

		if (srvPrvFeeInqReqDTO.getSelectedCurrency().compareTo(srvPrvFeeInqReqDTO.getLocalCurrencyId()) == 0) {
			// local currency flow
			serviceProviderRespDTO = partnerDataManager.fetchQuotationForLocalCurrency(srvPrvFeeInqReqDTO);
		} else {
			// foreign currency flow
			serviceProviderRespDTO = partnerDataManager.fetchQuotationForForeignCurrency(srvPrvFeeInqReqDTO);
		}

		return serviceProviderRespDTO;
	}

}
