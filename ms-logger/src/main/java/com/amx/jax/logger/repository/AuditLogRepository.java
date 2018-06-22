package com.amx.jax.logger.repository;

import java.util.List;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.amx.jax.logger.AuditEvent;

public interface AuditLogRepository extends MongoRepository<MongoAuditEvent, String> {

	@Override
	List<MongoAuditEvent> findAll();

	@Override
	List<MongoAuditEvent> findAll(Iterable<String> traceid);

	List<AuditEvent> findAllBy(TextCriteria criteria);

}
