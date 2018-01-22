package com.amx.jax.logger.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.mongodb.core.query.TextCriteria;
//import org.springframework.data.mongodb.core.query.TextCriteria;
//import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.logger.model.ApplicationLog;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public interface ApplicationLogRepository
		extends CrudRepository<ApplicationLog, BigInteger>, QueryDslPredicateExecutor<ApplicationLog> {
	@Override
	List<ApplicationLog> findAll();

	@Override
	List<ApplicationLog> findAll(Iterable<BigInteger> bigIntegers);

	@Override
	List<ApplicationLog> findAll(Predicate predicate);

	@Override
	List<ApplicationLog> findAll(Predicate predicate, OrderSpecifier<?>... orders);

	List<ApplicationLog> findByApplicationName(String applicationName);

	List<ApplicationLog> findAllBy(TextCriteria criteria);

	List<ApplicationLog> findAllByOrderByScoreDesc(TextCriteria criteria);

}
