package com.amx.jax.mx.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.mx.entity.EventNotificationEntity;

public interface EventNotificationEntityRepository extends CrudRepository<EventNotificationEntity, BigDecimal>{
	
	@Query("select en from EventNotificationEntity en where en.status = 1")
	public List<EventNotificationEntity> getEventNotificationRecordsToDelete();
}
