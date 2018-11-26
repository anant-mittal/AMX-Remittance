package com.amx.jax.broker;

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
import com.amx.jax.broker.dao.EventNotificationDao;
import com.amx.jax.broker.entity.EventNotificationEntity;
import com.amx.jax.broker.entity.EventNotificationView;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.StringUtils;
import com.amx.utils.TimeUtils;
import com.amx.utils.UniqueID;

@Configuration
@EnableScheduling
@Component
@Service
public class BrokerService {

	Logger logger = LoggerService.getLogger(BrokerService.class);

	@Autowired
	private EventNotificationDao eventNotificationDao;

	private long printDelay = 1000L;
	private long printStamp = 0L;

	@Autowired
	TunnelService tunnelService;

	@Scheduled(fixedDelay = BrokerConstants.PUSH_NOTIFICATION_FREQUENCY)
	public void pushNewEventNotifications() {

		String sessionId = UniqueID.generateString();

		List<EventNotificationView> event_list = eventNotificationDao.getNewlyInserted_EventNotificationRecords();

		int totalEvents = event_list.size();

		// Increase Print Delay if its been long waiting for events
		if (totalEvents > 0 || TimeUtils.isDead(printStamp, printDelay)) {
			logger.info("Total {} Events fetched from DB, after waiting {} secs", totalEvents,printDelay);
			printStamp = System.currentTimeMillis();
			if (totalEvents == 0) {
				printDelay = 2 * printDelay;
			} else {
				printDelay = 1000L;
			}
		}

		for (EventNotificationView current_event_record : event_list) {
			AppContextUtil.setTenant(Tenant.KWT);
			AppContextUtil.setSessionId(sessionId);
			AppContextUtil.getTraceId(true, true);
			AppContextUtil.init();
			try {
				logger.debug("------------------ current_event_record DB Data --------------------");
				logger.debug(current_event_record.toString());

				Map<String, String> event_data_map = StringUtils.getMapFromString(
						BrokerConstants.SPLITTER_CHAR,
						BrokerConstants.KEY_VALUE_SEPARATOR_CHAR, current_event_record.getEvent_data()
				);

				// Push to Message Queue
				DBEvents event = new DBEvents();
				event.setEventCode(current_event_record.getEvent_code());
				event.setPriority(current_event_record.getEvent_priority());
				event.setData(event_data_map);
				event.setDescription(current_event_record.getEvent_desc());

				logger.debug("------------------ Event Data to push to Message Queue --------------------");
				logger.debug(event.toString());

				tunnelService.task(current_event_record.getEvent_code(), event);

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
			} finally {
				AppContextUtil.clear();
			}
		}
	}

	@Scheduled(
			fixedDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY,
			initialDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY
	)
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
