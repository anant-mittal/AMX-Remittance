package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.broker.BrokerConstants;
import com.amx.jax.dbmodel.EventNotification;
import com.amx.jax.event.DBEvent;
import com.amx.jax.service.dao.EventNotificationDao;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.StringUtils;

@Configuration
@EnableScheduling
@Component
@Service
public class BrokerService {

	Logger logger = Logger.getLogger(BrokerService.class);

	@Autowired
	private EventNotificationDao eventNotificationDao;

	@Autowired
	TunnelService tunnelService;

	@Scheduled(fixedDelay = 2000)
	public void pushNewEventNotifications() {
		logger.info("Hello ...");
		logger.info("Fixed delay task - " + System.currentTimeMillis() / 1000);

		List<EventNotification> event_list = eventNotificationDao.getNewlyInserted_EventNotificationRecords();

		for (EventNotification event_record : event_list) {

			try {
				Map<String, String> event_data_map = StringUtils.getMapFromString(BrokerConstants.SPLITTER_CHAR,
						BrokerConstants.KEY_VALUE_SEPARATOR_CHAR, event_record.getEvent_data());

				logger.info("------------------ Map Data --------------------");
				logger.info(event_data_map);

				// TODO: Push to MQ

				DBEvent dbEvent = new DBEvent();
				dbEvent.setData(event_data_map);

				tunnelService.send("DB_EVENT", dbEvent);

				// Mark event record as success
				event_record.setStatus(new BigDecimal(BrokerConstants.SUCCESS_STATUS));

				eventNotificationDao.saveEventNotificationRecordUpdates(event_record);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);

				// Mark event record as failure
				event_record.setStatus(new BigDecimal(BrokerConstants.FAILURE_STATUS));
				eventNotificationDao.saveEventNotificationRecordUpdates(event_record);
			}

		}
	}

	@Scheduled(fixedDelay = 5000)
	public void cleanUpEventNotificationRecords() {
		logger.info("Hello I am deleting now ...");

		List<EventNotification> event_list = eventNotificationDao.getNewlyInserted_EventNotificationRecords();

		for (EventNotification event_record : event_list) {

			try {
				eventNotificationDao.deleteEventNotificationRecord(event_record);
			} catch (Exception e) {

				logger.error(e);
			}

		}
	}
}
