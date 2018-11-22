package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.DeviceStateInfoDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.device.SignaturePadRemittanceManager;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.service.BranchDetailService;
import com.amx.jax.services.DeviceStateService;
import com.amx.jax.services.JaxConfigService;
import com.amx.jax.util.CryptoUtil;
import com.amx.utils.Random;

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
