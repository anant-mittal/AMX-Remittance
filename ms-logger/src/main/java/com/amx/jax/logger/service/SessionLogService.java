package com.amx.jax.logger.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.model.CustomerLog;
import com.amx.jax.logger.model.SessionLog;
import com.amx.jax.logger.repository.SessionLogRepository;
import com.amx.jax.logger.util.CustomerLogStatistics;

/**
 * Service component That performs business operations on the CustomerLog data
 * model.
 * <p/>
 */
@Service
public class SessionLogService {

	@Autowired
	SessionLogRepository sessionLogRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * Retrieves products visited by this customer ordered by most visited one.
	 *
	 * @param customerId
	 *            The customer ID.
	 * @return List of products and the corresponding number of visits
	 */
	public List<CustomerLogStatistics> mostVisitedProducts(String customerId) {
		Aggregation aggregation = newAggregation(CustomerLog.class, match(Criteria.where("customerId").is(customerId)),
				group("productId").count().as("total"), project("total").and("productId").previousOperation(),
				sort(Sort.Direction.DESC, "total"));

		AggregationResults<CustomerLogStatistics> groupResults = mongoTemplate.aggregate(aggregation, CustomerLog.class,
				CustomerLogStatistics.class);

		return groupResults.getMappedResults();

	}

	/**
	 * Retrieves the most visited product.
	 *
	 * @param customerId
	 *            The customer ID.
	 * @return List of products and the corresponding number of visits
	 */
	public CustomerLogStatistics mostVisitedProduct(String customerId) {
		return mostVisitedProducts(customerId).get(0);

	}

	/**
	 * Deep search in all customer log entries for any match with the passed texts.
	 * This uses the full text search of MongoDB.
	 *
	 * @param text
	 *            search terms
	 * @return matched log records sorted by match score
	 */
	public List<SessionLog> searchForText(String... text) {
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(text);
		return sessionLogRepository.findAllByOrderByScoreDesc(criteria);
	}

	/**
	 * Finds all logs related to a specific customer
	 *
	 * @param customerId
	 * @return
	 */
	public List<SessionLog> findByCustomerId(String customerId) {
		return sessionLogRepository.findByCustomerId(customerId);
	}

	@Async
	public void saveCustomerLog(SessionLog sessionLog) {
		sessionLogRepository.save(sessionLog);
	}
}
