package com.amx.jax.broker.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.broker.entity.EventNotificationEntity;
import com.amx.jax.broker.entity.EventNotificationView;
import com.amx.jax.broker.repository.EventNotificationEntityRepository;
import com.amx.jax.broker.repository.EventNotificationViewRepository;

@Component
public class EventNotificationDao {
	
	@Autowired
	private EventNotificationEntityRepository eventNotificationEntityRepository;
	
	@Autowired
	private EventNotificationViewRepository eventNotificationViewRepository;
	
	@Transactional
	public List<EventNotificationView> getNewlyInserted_EventNotificationRecords()
	{
		return eventNotificationViewRepository.getNewlyInserted_EventNotificationRecords();
	}
	
	@Transactional
	public List<EventNotificationEntity> getEventNotificationRecordsToDelete()
	{
		return eventNotificationEntityRepository.getEventNotificationRecordsToDelete();
	}
	
	@Transactional
	public EventNotificationEntity getEventNotificationRecordById(BigDecimal event_notification_id)
	{
		return eventNotificationEntityRepository.findOne(event_notification_id);
	}
	
	@Transactional
	public void saveEventNotificationRecordUpdates(EventNotificationEntity event_record)
	{
		eventNotificationEntityRepository.save(event_record);
	}
	
	@Transactional
	public void deleteEventNotificationRecord(EventNotificationEntity event_record)
	{
		eventNotificationEntityRepository.delete(event_record.getEvent_notification_id());
	}
	
	@Transactional
	public void deleteEventNotificationRecordList(List<EventNotificationEntity> event_record_list)
	{
		eventNotificationEntityRepository.delete(event_record_list);
	}
}
