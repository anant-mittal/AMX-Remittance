package com.amx.jax.service.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.EventNotification;

public interface EventNotificationRepository extends CrudRepository<EventNotification, BigDecimal>{
	
	@Query("select en from EventNotification en where en.create_date>?1")
	public List<EventNotification> getNewlyInserted_EventNotificationRecords(Date last_run_time);
	
	@Query("select en from EventNotification en where en.status is null")
	public List<EventNotification> getNewlyInserted_EventNotificationRecords();
	
	@Query("select en from EventNotification en where en.status = 1")
	public List<EventNotification> getEventNotificationRecordsToDelete();

}
