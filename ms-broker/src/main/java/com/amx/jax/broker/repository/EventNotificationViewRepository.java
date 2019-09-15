package com.amx.jax.broker.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.broker.entity.EventNotificationView;

public interface EventNotificationViewRepository extends CrudRepository<EventNotificationView, BigDecimal> {
	@Query("select en from EventNotificationView en where en.status is null")
	public Page<EventNotificationView> getNewlyInserted_EventNotificationRecords(Pageable pageable);
}
