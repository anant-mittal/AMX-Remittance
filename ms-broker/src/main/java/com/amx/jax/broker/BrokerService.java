package com.amx.jax.broker;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.broker.dao.EventNotificationDao;
import com.amx.jax.broker.entity.EventNotificationEntity;
import com.amx.jax.broker.entity.EventNotificationView;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.StringUtils;
import com.amx.utils.TimeUtils;

@Component
@TenantScoped
public class BrokerService {

	Logger logger = LoggerService.getLogger(BrokerService.class);

	private static Map<String, Object> STATUS_MAP = Collections.synchronizedMap(new HashMap<String, Object>());

	@Autowired
	private EventNotificationDao eventNotificationDao;

	private Tenant serviceTenant;
	private long printDelay = 1000L;
	private long printDelayLimit = 2 * 60 * 60 * 1000L;
	private long printStamp = 0L;

	@Autowired
	TunnelService tunnelService;

	public void pushNewEventNotifications(Tenant tenant, String sessionId) {

		if (this.serviceTenant == null) {
			this.serviceTenant = tenant;
		}

		List<EventNotificationView> event_list = eventNotificationDao.getNewlyInserted_EventNotificationRecords();

		int totalEvents = event_list.size();

		// Increase Print Delay if its been long waiting for events
		if (totalEvents > 0 || TimeUtils.isDead(printStamp, printDelay)) {
			logger.info("Total {} Events fetched from DB, after waiting {} secs", totalEvents, printDelay);
			printStamp = System.currentTimeMillis();
			if (totalEvents == 0) {
				printDelay = Math.min(2 * printDelay, printDelayLimit);
			} else {
				printDelay = 1000L;
			}
		} else {
			logger.debug("Total {} Events fetched from DB, after waiting {} secs", totalEvents, printDelay);
		}

		STATUS_MAP.put(this.serviceTenant.toString(), printStamp);

		for (EventNotificationView current_event_record : event_list) {
			AppContextUtil.setTenant(tenant);
			AppContextUtil.setFlowfix(current_event_record.getEvent_code());
			AppContextUtil.setSessionId(sessionId);
			AppContextUtil.getTraceId(true, true);
			AppContextUtil.init();
			try {
				logger.debug("------------------ current_event_record DB Data --------------------");
				logger.debug(current_event_record.toString());

				Map<String, String> event_data_map = StringUtils.getMapFromString(
						BrokerConstants.SPLITTER_CHAR,
						BrokerConstants.KEY_VALUE_SEPARATOR_CHAR, current_event_record.getEvent_data());

				// Push to Message Queue
				DBEvent event = new DBEvent();
				event.setEventCode(current_event_record.getEvent_code());
				event.setPriority(current_event_record.getEvent_priority());
				event.setData(event_data_map);
				// event.setDescription(current_event_record.getEvent_desc());
				event.setText(current_event_record.getEvent_desc());

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

	public void cleanUpEventNotificationRecords(Tenant tenant, String sessionId) {
		logger.debug("Delete proccess started on the table EX_EVENT_NOTIFICATION...");
		try {
			eventNotificationDao
					.deleteEventNotificationRecordList(eventNotificationDao.getEventNotificationRecordsToDelete());
		} catch (Exception e) {

			logger.error("Error in cleanUpEventNotificationRecords", e);
		}
	}
}
