package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.DeviceRepository;
import com.amx.jax.repository.DeviceStateRepository;
import com.amx.jax.service.BranchDetailService;
import com.amx.jax.util.CryptoUtil;

@Component
public class DeviceStateInfoDao {

	@Autowired
	DeviceStateRepository deviceStateRepository;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	JaxProperties jaxProperties ; 

	

	public void saveDeviceInfo(DeviceStateInfo deviceInfo) {
		deviceStateRepository.save(deviceInfo);

	}

	public DeviceStateInfo getDeviceStateInfoByPaireToken(String pairToken, Integer registrationId) {
		return deviceStateRepository.findByPairTokenAndDeviceRegId(pairToken, new BigDecimal(registrationId));
	}

	public DeviceStateInfo findBySessionToken(String sessionToken, Integer registrationId) {
		return deviceStateRepository.findBySessionTokenAndDeviceRegId(sessionToken, new BigDecimal(registrationId));
	}

	public DeviceStateInfo getDeviceStateInfo(BigDecimal registrationId) {
		return deviceStateRepository.findOne(registrationId);
	}
	
}
