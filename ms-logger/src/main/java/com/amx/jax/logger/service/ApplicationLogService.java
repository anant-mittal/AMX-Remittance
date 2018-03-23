package com.amx.jax.logger.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.model.ApplicationLog;
import com.amx.jax.logger.repository.ApplicationLogRepository;

@Service
public class ApplicationLogService {

	Logger logger = Logger.getLogger(ApplicationLogService.class);

	@Autowired
	private ApplicationLogRepository applicationLogRepository;

	@Async
	public void saveApplicationLog(ApplicationLog applicationLog) {
		applicationLogRepository.save(applicationLog);
	}

	/**
	 * Finds all logs for a specific application
	 *
	 * @param applicationName
	 * @return
	 */
	public List<ApplicationLog> findAll(String applicationName) {
		return applicationLogRepository.findByApplicationName(applicationName);
	}

	/**
	 * Deep search in all application log entries for any match with the passed
	 * texts. This uses the full text search of MongoDB.
	 *
	 * @param text
	 *            search terms
	 * @return matched log records sorted by match score
	 */
	public List<ApplicationLog> searchForText(String... text) {
		logger.info("searchForText(" + text + ")");
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(text);
		return applicationLogRepository.findAllByOrderByScoreDesc(criteria);
	}
}
