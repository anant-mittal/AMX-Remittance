package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.AppContextUtil;
import com.amx.jax.broker.BrokerConstants;
import com.amx.jax.dbmodel.EventNotificationEntity;
import com.amx.jax.dbmodel.EventNotificationView;
import com.amx.jax.dict.Tenant;
import com.amx.jax.event.Event;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.service.dao.EventNotificationDao;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.StringUtils;
import com.amx.utils.UniqueID;

@Configuration
@EnableScheduling
@Component
@Service
public class BrokerService {

	Logger logger = LoggerService.getLogger(BrokerService.class);

	@Autowired
	private EventNotificationDao eventNotificationDao;

	@Autowired
	TunnelService tunnelService;

	@Scheduled(fixedDelay = BrokerConstants.PUSH_NOTIFICATION_FREQUENCY)
	public void pushNewEventNotifications() {

		AppContextUtil.setTenant(Tenant.KWT);
		AppContextUtil.generateTraceId(true);

		logger.info("pushNewEventNotifications Job started ...");

		List<EventNotificationView> event_list = eventNotificationDao.getNewlyInserted_EventNotificationRecords();

		for (EventNotificationView current_event_record : event_list) {

			try {
				logger.info("------------------ current_event_record DB Data --------------------");
				logger.info(current_event_record.toString());

				Map<String, String> event_data_map = StringUtils.getMapFromString(BrokerConstants.SPLITTER_CHAR,
						BrokerConstants.KEY_VALUE_SEPARATOR_CHAR, current_event_record.getEvent_data());

				// Push to Message Queue
				Event event = new Event();
				event.setEvent_code(current_event_record.getEvent_code());
				event.setPriority(current_event_record.getEvent_priority());
				event.setData(event_data_map);
				event.setDescription(current_event_record.getEvent_desc());

				logger.info("------------------ Event Data to push to Message Queue --------------------");
				logger.info(event.toString());

				tunnelService.send(current_event_record.getEvent_code(), event);

				// Mark event record as success
				EventNotificationEntity temp_event_record = eventNotificationDao
						.getEventNotificationRecordById(current_event_record.getEvent_notification_id());
				temp_event_record.setStatus(new BigDecimal(BrokerConstants.SUCCESS_STATUS));

				eventNotificationDao.saveEventNotificationRecordUpdates(temp_event_record);

			} catch (Exception e) {
				logger.error("Error in pushNewEventNotifications", e);

				// Mark event record as failure
				EventNotificationEntity temp_event_record = eventNotificationDao
						.getEventNotificationRecordById(current_event_record.getEvent_notification_id());
				temp_event_record.setStatus(new BigDecimal(BrokerConstants.FAILURE_STATUS));

				eventNotificationDao.saveEventNotificationRecordUpdates(temp_event_record);
			}
		}
	}

	@Scheduled(fixedDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY, initialDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY)
	public void cleanUpEventNotificationRecords() {
		logger.info("Delete proccess started on the table EX_EVENT_NOTIFICATION...");

		try {
			eventNotificationDao
					.deleteEventNotificationRecordList(eventNotificationDao.getEventNotificationRecordsToDelete());
		} catch (Exception e) {

			logger.error("Error in cleanUpEventNotificationRecords", e);
		}
	}
}
