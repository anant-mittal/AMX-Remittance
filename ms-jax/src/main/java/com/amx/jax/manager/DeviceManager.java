package com.amx.jax.manager;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dao.DeviceStateInfoDao;
import com.amx.jax.device.SignaturePadRemittanceManager;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.service.BranchDetailService;
import com.amx.jax.services.JaxConfigService;
import com.amx.jax.util.CryptoUtil;

/**
 * @author Prashant
 *
 */
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component

public class DeviceManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DeviceStateInfoDao deviceDao;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	JaxConfigService jaxConfigService;
	@Autowired
	SignaturePadRemittanceManager signaturePadRemittanceManager;
	@Autowired
	BranchDetailService branchDetailService;


	public IDeviceStateData getRemittanceData(BigDecimal remittanceTransactionId) {
		return signaturePadRemittanceManager.getRemittanceReceiptData(remittanceTransactionId);
	}



}
