package com.amx.jax.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component

public class DeviceManager {

	@Autowired
	DeviceDao deviceDao;

	public void activateDevice(Integer countryBranchSystemInventoryId, String deviceType) {
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		if (device == null) {
			throw new GlobalException("No device found");
		}
		device.setStatus(ConstantDocument.Yes);
		deviceDao.saveDevice(device);
	}

}
