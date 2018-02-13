package com.amx.jax.ui.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;
import com.bootloaderjs.Constants;
import com.bootloaderjs.Random;

/**
 * To Save Values to Session Use this class, only if these values are not
 * related to Valid user Request
 * 
 * @author lalittanwar
 *
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GuestSession implements Serializable {

	private static final long serialVersionUID = -8825493107883952226L;
	private Logger log = LoggerFactory.getLogger(getClass());

	private Map<String, String> nextTokenMap = new HashMap<String, String>();

	public String getNextToken(String key) {
		String nextToken = Random.randomAlpha(6);
		nextTokenMap.put(key, nextToken);
		log.info("Created {} = {}", key, nextToken);
		return nextToken;
	}

	public boolean isValidToken(String key, String value) {
		log.info("Validating {} = {}", key, value);
		if (nextTokenMap.containsKey(key)) {
			return nextTokenMap.getOrDefault(key, Constants.BLANK).equalsIgnoreCase(value);
		}
		return false;
	}

	private Integer hits = 0;

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Integer hitCounter() {
		return this.hits++;
	}

	private CustomerModel customerModel = null;

	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	public Integer quesIndex = 0;

	public Integer getQuesIndex() {
		return quesIndex;
	}

	public void setQuesIndex(Integer quesIndex) {
		this.quesIndex = quesIndex;
	}

	public void nextQuesIndex() {
		quesIndex++;
	}

}