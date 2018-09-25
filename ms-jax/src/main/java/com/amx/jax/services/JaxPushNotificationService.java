package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.PushNotificationRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.repository.IJaxPushNotificationDao;
import com.amx.jax.util.DateUtil;

@Service
public class JaxPushNotificationService extends AbstractService {

	private Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	IJaxPushNotificationDao jaxPushNotificationDao;
	
	@Autowired
	DateUtil dateUtil;

	public AmxApiResponse<CustomerNotificationDTO, ?> get(BigDecimal customerId, BigDecimal nationalityId,
			BigDecimal countryId) {

		List<PushNotificationRecord> notificationList = null;
		List<CustomerNotificationDTO> notificationDtoList = new ArrayList<CustomerNotificationDTO>();

		try {
			notificationList = jaxPushNotificationDao.getJaxNotification(customerId, nationalityId, countryId, dateUtil.getMidnightToday());

			if (!notificationList.isEmpty()) {

				for (PushNotificationRecord notification : notificationList) {
					CustomerNotificationDTO notificationDto = new CustomerNotificationDTO();

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

	public AmxApiResponse<Object, Object> save(List<PushNotificationRecord> jaxPushNotifications) {

		try {
			for (PushNotificationRecord jaxPushNotification : jaxPushNotifications) {
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
