package com.amx.jax.ui.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;
import com.amx.jax.ui.config.HttpUnauthorizedException;
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

	public static enum AuthFlow {
		LOGIN, ACTIVATION, RESET_PASS
	}

	public static enum AuthStep {
		USERPASS, SECQUES, IDVALID, DOTPVFY, COMPLETED,
		// Reg
		MOTPVFY
	}

	@Autowired
	TenantContext<AuthLib> tenantContext;

	AuthFlow flow = null;
	AuthStep authStep = null;
	String seqId = null;

	public AuthStep getAuthStep() {
		return authStep;
	}

	public AuthStep getNextAuthStep() {
		return tenantContext.get().getNextAuthStep(flow, authStep);
	}

	public void setAuthStep(AuthStep authStep) {
		this.authStep = authStep;
	}

	public boolean isAuthStep(AuthStep authStep) {
		return this.authStep == authStep;
	}

	public AuthFlow getFlow() {
		return flow;
	}

	public boolean isFlow(AuthFlow flow) {
		return this.flow == flow;
	}

	public void setFlow(AuthFlow authFlow) {
		this.flow = authFlow;
	}

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private HttpServletRequest request;

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

	public void validate(String curEnd, String[] validEnds) {
		Cookie kooky = new Cookie(UIConstants.SEQ_KEY, this.getNextToken(curEnd));
		kooky.setMaxAge(300);
		// kooky.setPath("/");
		response.addCookie(kooky);
	}

	private void validateSeqCookie(String seqKey, String seqValue) {
		if (this.isValidToken(UIConstants.SEQ_KEY_STEP_LOGIN, seqValue)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			throw new HttpUnauthorizedException();
		}
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