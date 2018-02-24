package com.amx.jax.logger.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.amx.jax.logger.model.CustomerLog;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

/**
 * 
 */
public interface CustomerLogRepository
		extends MongoRepository<CustomerLog, BigInteger>, QueryDslPredicateExecutor<CustomerLog> {
	@Override
	List<CustomerLog> findAll();

	@Override
	List<CustomerLog> findAll(Iterable<BigInteger> bigIntegers);

	@Override
	List<CustomerLog> findAll(Predicate predicate);

	@Override
	List<CustomerLog> findAll(Predicate predicate, OrderSpecifier<?>... orders);

	List<CustomerLog> findAllBy(TextCriteria criteria);

	List<CustomerLog> findAllByOrderByScoreDesc(TextCriteria criteria);

	List<CustomerLog> findByCustomerId(String customerId);
}
