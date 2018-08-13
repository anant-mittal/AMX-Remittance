package com.amx.jax.service.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.EventNotification;
import com.amx.jax.service.repository.EventNotificationRepository;

@Component
public class EventNotificationDao {
	
	@Autowired
	private EventNotificationRepository eventNotificationRepository;
	
	@Transactional
	public List<EventNotification> getNewlyInserted_EventNotificationRecords(Date last_run_time)
	{
		return eventNotificationRepository.getNewlyInserted_EventNotificationRecords(last_run_time);
	}
	
	@Transactional
	public List<EventNotification> getNewlyInserted_EventNotificationRecords()
	{
		return eventNotificationRepository.getNewlyInserted_EventNotificationRecords();
	}
	
	@Transactional
	public List<EventNotification> getEventNotificationRecordsToDelete()
	{
		return eventNotificationRepository.getEventNotificationRecordsToDelete();
	}
	
	@Transactional
	public void saveEventNotificationRecordUpdates(EventNotification event_record)
	{
		eventNotificationRepository.save(event_record);
	}
	
	@Transactional
	public void deleteEventNotificationRecord(EventNotification event_record)
	{
		eventNotificationRepository.delete(event_record.getEvent_notification_id());
	}

}
