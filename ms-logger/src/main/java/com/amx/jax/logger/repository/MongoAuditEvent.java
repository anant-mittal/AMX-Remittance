package com.amx.jax.logger.repository;

import org.springframework.data.mongodb.core.mapping.Document;

import com.amx.jax.logger.AuditEvent;

@Document(collection = "Event")
public class MongoAuditEvent extends AuditEvent {

}
