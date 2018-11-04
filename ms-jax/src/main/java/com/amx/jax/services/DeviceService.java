package com.amx.jax.services;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.DeviceState;
import com.amx.jax.constants.DeviceStateDataType;
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.error.JaxError;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.DeviceManager;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateInfo;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.model.response.customer.CustomerContactDto;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.model.response.customer.CustomerIdProofDto;
import com.amx.jax.services.AbstractService;
import com.amx.jax.validation.DeviceValidation;
import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonUtil;
import com.amx.jax.dict.UserClient.ClientType;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeviceService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(DeviceService.class);

	@Autowired
	DeviceDao deviceDao;
	@Autowired
	DeviceManager deviceManager;
	@Autowired
	DeviceValidation deviceValidation;
	@Autowired
	JaxConfigService jaxConfigService;
	@Autowired
	CustomerService customerService;

	public static final long DEVICE_SESSION_TIMEOUT = 8 * 60 * 60; // in seconds

	@Transactional
	public DeviceDto registerNewDevice(DeviceRegistrationRequest request) {
		logger.info("In register device with request: {}", request);
		deviceValidation.validateDeviceRegRequest(request);
		DeviceDto newDevice = deviceDao.saveDevice(request);
		DeviceState deviceState;
		if (ConstantDocument.Yes.equals(newDevice.getStatus())) {
			deviceState = DeviceState.REGISTERED;
		} else {
			deviceState = DeviceState.REGISTERED_NOT_ACTIVE;
		}
		deviceDao.saveDeviceState(newDevice, deviceState);
		logger.info("device registered with id: {}", newDevice.getRegistrationId());
		return newDevice;
	}

	public BoolRespModel updateDeviceState(DeviceStateInfoChangeRequest request, Integer registrationId,
			String paireToken, String sessionToken) {
		if (registrationId == null) {
			throw new GlobalException("Device registration id can not be blank");
		}
		deviceValidation.validatePaireToken(paireToken, registrationId);
		deviceManager.validateSessionToken(sessionToken, registrationId);
		Device device = deviceDao.findDevice(new BigDecimal(registrationId));
		deviceValidation.validateDevice(device);
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		deviceStateInfo.setState(request.getState());
		deviceDao.saveDeviceInfo(deviceStateInfo);
		return new BoolRespModel(Boolean.TRUE);
	}

	public BoolRespModel activateDevice(Integer countryBranchSystemInventoryId, ClientType deviceType) {
		logger.info("In activateDevice with countryBranchSystemInventoryId: {}", countryBranchSystemInventoryId);
		deviceManager.validateDeviceActivationRequest(countryBranchSystemInventoryId, deviceType);
		deviceManager.activateDevice(countryBranchSystemInventoryId, deviceType);
		return new BoolRespModel(Boolean.TRUE);
	}

	public DevicePairOtpResponse sendOtpForPairing(Integer deviceRegId, String paireToken) {
		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		deviceValidation.validatePaireToken(paireToken, deviceRegId);
		deviceValidation.validateDevice(device);
		DevicePairOtpResponse response = deviceManager.generateOtp(device);
		return response;
	}

	public BoolRespModel validateOtpForPairing(ClientType deviceType, Integer countryBranchSystemInventoryId,
			String otp) {
		deviceValidation.validateOtp(otp);
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		deviceValidation.validateDevice(device);
		deviceValidation.validateDeviceToken(device, otp);
		// device login success
		createSession(device);
		return new BoolRespModel(Boolean.TRUE);
	}

	private void createSession(Device device) {

		DeviceStateInfo deviceInfo = deviceDao.getDeviceStateInfo(device);
		deviceInfo.setState(DeviceState.SESSION_PAIRED);
		deviceDao.saveDeviceInfo(deviceInfo);
	}

	/**
	 * @param registrationId
	 * @param sessionToken
	 * @param paireToken
	 * @return device's status like loggedIn, signing etc
	 * 
	 */
	public DeviceStatusInfoDto getStatus(Integer registrationId, String paireToken, String sessionToken) {

		if (registrationId == null) {
			throw new GlobalException("Device registration id can not be blank");
		}
		deviceValidation.validateDevice(registrationId);
		deviceValidation.validatePaireToken(paireToken, registrationId);
		deviceManager.validateSessionToken(sessionToken, registrationId);
		Device device = deviceDao.findDevice(new BigDecimal(registrationId));
		deviceValidation.validateDevice(device);
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		DeviceStatusInfoDto dto = new DeviceStatusInfoDto();
		dto.setStateDataType(deviceStateInfo.getStateDataType());
		dto.setDeviceState(deviceStateInfo.getState());
		if (!deviceManager.isLoggedIn(device)) {
			dto.setDeviceState(DeviceState.REGISTERED);
		}
		if (deviceStateInfo.getStateDataType() != null) {
			switch (deviceStateInfo.getStateDataType()) {
			case REMITTANCE:
				SignaturePadRemittanceInfo stateData = JsonUtil.fromJson(deviceStateInfo.getStateData(),
						SignaturePadRemittanceInfo.class);
				dto.setSignaturePadRemittanceInfo(stateData);
				break;

			case FC_PURCHASE:
				SignaturePadFCPurchaseSaleInfo stateDataPurchase = JsonUtil.fromJson(deviceStateInfo.getStateData(),
						SignaturePadFCPurchaseSaleInfo.class);
				dto.setSignaturePadFCPurchaseInfo(stateDataPurchase);
				break;

			case FC_SALE:
				SignaturePadFCPurchaseSaleInfo stateDataSale = JsonUtil.fromJson(deviceStateInfo.getStateData(),
						SignaturePadFCPurchaseSaleInfo.class);
				dto.setSignaturePadFCSaleInfo(stateDataSale);
				break;
			case CUSTOMER_REGISTRATION:
				SignaturePadCustomerRegStateMetaInfo metaInfo = JsonUtil.fromJson(deviceStateInfo.getStateData(),
						SignaturePadCustomerRegStateMetaInfo.class);
				dto.setSignaturePadCustomerRegStateInfo(getCustomerRegData(metaInfo.getCustomerId()));
				break;
			default:
				break;
			}
		}
		return dto;
	}

	private SignaturePadCustomerRegStateInfo getCustomerRegData(Integer customerId) {

		SignaturePadCustomerRegStateInfo info = new SignaturePadCustomerRegStateInfo();
		BigDecimal customerIdBd = new BigDecimal(customerId);
		info.setCustomerContactDto(customerService.getCustomerContactDto(customerIdBd));
		info.setCustomerDto(customerService.getCustomerDto(customerIdBd));
		info.setCustomerIdProofDto(
				customerService.getCustomerIdProofDto(customerIdBd, ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID));
		info.setCustomerIncomeRangeDto(customerService.getCustomerIncomeRangeDto(customerIdBd));
		return info;
	}

	public BoolRespModel updateDeviceStateData(ClientType deviceType, Integer countryBranchSystemInventoryId,
			IDeviceStateData deviceStateData, DeviceStateDataType type) {
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		deviceValidation.validateDevice(device);
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		deviceManager.validateLogIn(device);
		logger.debug("updating device state D id {} ", device.getRegistrationId());
		String deviceStateDataStr = JsonUtil.toJson(deviceStateData);
		deviceStateInfo.setStateData(deviceStateDataStr);
		deviceStateInfo.setStateDataType(type);
		deviceDao.saveDeviceInfo(deviceStateInfo);
		return new BoolRespModel(Boolean.TRUE);

	}

	public BoolRespModel updateSignatureStateData(Integer deviceRegId, String imageUrlStr) {
		URL imageUrl = deviceValidation.validateImageUrl(imageUrlStr);
		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		try {
			InputStream stream = imageUrl.openStream();
			deviceStateInfo.setSignature(IOUtils.toByteArray(stream));
			deviceDao.saveDeviceInfo(deviceStateInfo);
		} catch (IOException e) {
			logger.error("error in updateSignatureStateData url", e);
		}
		return new BoolRespModel(Boolean.TRUE);
	}
}
