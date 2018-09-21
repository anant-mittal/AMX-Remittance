package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.PushNotificationRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.repository.IJaxPushNotificationDao;

@Service
public class JaxPushNotificationService extends AbstractService {

	private Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	IJaxPushNotificationDao jaxPushNotificationDao;

	public ApiResponse<CustomerNotificationDTO> getJaxNotification(BigDecimal customerId, BigDecimal nationalityId,
			BigDecimal countryId) {

		ApiResponse<CustomerNotificationDTO> response = getBlackApiResponse();

		List<PushNotificationRecord> notificationList = null;
		List<CustomerNotificationDTO> notificationDtoList = new ArrayList<CustomerNotificationDTO>();

		try {
			notificationList = jaxPushNotificationDao.getJaxNotification(customerId, nationalityId, countryId);

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
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().setType("jax-push-notification");
			}

		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while fetching Notification List ", e);
		}

		response.getData().getValues().addAll(notificationDtoList);
		return response;
	}

	public ApiResponse saveJaxPushNotification(PushNotificationRecord jaxPushNotification) {
		ApiResponse response = getBlackApiResponse();

		try {

			jaxPushNotification.setNotificationDate(new Date());
			jaxPushNotificationDao.save(jaxPushNotification);
			response.setResponseStatus(ResponseStatus.OK);

		} catch (Exception e) {
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			logger.error("Error while saving Push Notification.", e);
		}

		logger.info(" ------ Notification saved successfully ------ ");

		return response;
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

}
