package com.amx.jax.logger.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.amx.jax.logger.model.SessionLog;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public interface SessionLogRepository
		extends MongoRepository<SessionLog, BigInteger>, QueryDslPredicateExecutor<SessionLog> {
	@Override
	List<SessionLog> findAll();

	@Override
	List<SessionLog> findAll(Iterable<BigInteger> bigIntegers);

	@Override
	List<SessionLog> findAll(Predicate predicate);

	@Override
	List<SessionLog> findAll(Predicate predicate, OrderSpecifier<?>... orders);

	List<SessionLog> findAllBy(TextCriteria criteria);

	List<SessionLog> findAllByOrderByScoreDesc(TextCriteria criteria);

	List<SessionLog> findByCustomerId(String customerId);
}
