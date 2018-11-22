package com.amx.jax.service.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.service.entity.EventNotificationView;

public interface EventNotificationViewRepository extends CrudRepository<EventNotificationView, BigDecimal>{
	@Query("select en from EventNotificationView en where en.status is null")
	public List<EventNotificationView> getNewlyInserted_EventNotificationRecords();
}
