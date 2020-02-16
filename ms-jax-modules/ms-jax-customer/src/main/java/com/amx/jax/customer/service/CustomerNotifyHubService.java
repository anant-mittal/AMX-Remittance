package com.amx.jax.customer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.CustomerNotifyHubDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.customer.CustomerNotifyHubRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.customer.IJaxPushNotificationDao;
import com.amx.jax.services.AbstractService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.DateUtil;

@Service
public class CustomerNotifyHubService extends AbstractService {

	private Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	IJaxPushNotificationDao jaxPushNotificationDao;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	MetaData metaData;

	@Autowired
	UserService userService;

	public AmxApiResponse<CustomerNotifyHubDTO, ?> get(BigDecimal customerId) {
		Customer customer = userService.getCustById(customerId);
		BigDecimal nationalityId = customer.getNationalityId();
		BigDecimal countryId = metaData.getCountryId();
		return this.get(customerId, nationalityId, countryId);
	}

	public AmxApiResponse<CustomerNotifyHubDTO, ?> get(BigDecimal customerId, BigDecimal nationalityId,
			BigDecimal countryId) {

		List<CustomerNotifyHubRecord> notificationList = null;
		List<CustomerNotifyHubDTO> notificationDtoList = new ArrayList<CustomerNotifyHubDTO>();

		try {
			notificationList = jaxPushNotificationDao.getJaxNotification(customerId, nationalityId, countryId,
					dateUtil.getMidnightToday());

			if (!notificationList.isEmpty()) {

				for (CustomerNotifyHubRecord notification : notificationList) {
					CustomerNotifyHubDTO notificationDto = new CustomerNotifyHubDTO();

					notificationDto.setNotificationId(notification.getNotificationId());
					notificationDto.setCustomerId(notification.getCustomerId());
					notificationDto.setCurrencyId(notification.getCurrencyId());
					notificationDto.setNationalityId(notification.getNationalityId());
					notificationDto.setTitle(notification.getTitle());
					notificationDto.setMessage(notification.getMessage());
					notificationDto.setNotificationDate(notification.getNotificationDate());
					notificationDto.setCountryId(notification.getCountryId());

					notificationDtoList.add(notificationDto);
				}
				logger.info("In GET Jax Push Notification Service ------ ");
			}
		} catch (Exception e) {
			logger.error("Error while fetching Notification List ", e);
		}
		return AmxApiResponse.buildList(notificationDtoList);
	}

	public AmxApiResponse<Object, Object> save(List<CustomerNotifyHubRecord> jaxPushNotifications) {

		try {
			for (CustomerNotifyHubRecord jaxPushNotification : jaxPushNotifications) {
				jaxPushNotification.setNotificationDate(new Date());
				jaxPushNotificationDao.save(jaxPushNotification);
			}
			logger.info("In SAVE Push Notification Service Data Added ------ ");
		} catch (Exception e) {
			logger.error("Error while saving Push Notification.", e);
		}
		return AmxApiResponse.build();
	}

}
