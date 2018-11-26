package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.DeviceStateDataType;
import com.amx.jax.customer.dao.EmployeeDao;
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.dao.DeviceStateInfoDao;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.LoginLogoutHistory;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.manager.DeviceManager;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateInfo;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.JsonUtil;;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeviceStateService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(DeviceStateService.class);

	@Autowired
	DeviceStateInfoDao deviceDao;
	@Autowired
	DeviceManager deviceManager;
	@Autowired
	JaxConfigService jaxConfigService;
	@Autowired
	CustomerService customerService;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	UserService userService;
	@Autowired
	RbaacServiceClient rbaacServiceClient;

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
		checkAndInitDeviceStateData(new BigDecimal(registrationId));
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(new BigDecimal(registrationId));
		DeviceStatusInfoDto dto = new DeviceStatusInfoDto();
		dto.setStateDataType(deviceStateInfo.getStateDataType());
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
		setBranchPcLogoutTime(dto, deviceStateInfo.getEmployeeId());
		dto.setLastUpdatedTime(deviceStateInfo.getStateDataModifiedDate());
		return dto;
	}

	private void setBranchPcLogoutTime(DeviceStatusInfoDto dto, BigDecimal employeeId) {
		if (employeeId != null) {
			String userName = employeeDao.getEmployeeDetails(employeeId).getUserName();
			LoginLogoutHistory logoutHistory = userService.getLastLogoutHistoryByUserName(userName);
			if (logoutHistory != null) {
				dto.setBranchPcLastLogoutTime(logoutHistory.getLogoutTime());
			}
		}
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
			IDeviceStateData deviceStateData, DeviceStateDataType type, BigDecimal employeeId) {

		BigDecimal deviceRegId = rbaacServiceClient
				.getDeviceRegIdByBranchInventoryId(deviceType, new BigDecimal(countryBranchSystemInventoryId))
				.getResult();
		checkAndInitDeviceStateData(deviceRegId);
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(deviceRegId);
		logger.debug("updating device state D id {} ", deviceRegId);
		String deviceStateDataStr = JsonUtil.toJson(deviceStateData);
		deviceStateInfo.setStateData(deviceStateDataStr);
		deviceStateInfo.setStateDataModifiedDate(new Date());
		deviceStateInfo.setStateDataType(type);
		deviceStateInfo.setEmployeeId(employeeId);
		deviceDao.saveDeviceInfo(deviceStateInfo);
		return new BoolRespModel(Boolean.TRUE);

	}

	public BoolRespModel updateSignatureStateData(Integer deviceRegId, String imageUrlStr) {
		checkAndInitDeviceStateData(new BigDecimal(deviceRegId));
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(new BigDecimal(deviceRegId));
		deviceStateInfo.setSignature(imageUrlStr);
		deviceDao.saveDeviceInfo(deviceStateInfo);

		return new BoolRespModel(Boolean.TRUE);
	}

	public BoolRespModel clearDeviceState(Integer registrationId, String paireToken, String sessionToken) {
		checkAndInitDeviceStateData(new BigDecimal(registrationId));
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(new BigDecimal(registrationId));
		logger.debug("clearDeviceState D id {} ", registrationId);
		deviceStateInfo.setStateData(null);
		deviceStateInfo.setSignature(null);
		deviceStateInfo.setStateDataType(null);
		deviceDao.saveDeviceInfo(deviceStateInfo);
		return new BoolRespModel(Boolean.TRUE);
	}

	public void checkAndInitDeviceStateData(BigDecimal registrationId) {
		logger.debug("init device state info D id {} ", registrationId);
		DeviceStateInfo deviceStateInfo = new DeviceStateInfo(registrationId);
		deviceDao.saveDeviceInfo(deviceStateInfo);
	}
}
